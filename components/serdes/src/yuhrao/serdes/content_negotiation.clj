(ns yuhrao.serdes.content-negotiation
  (:require [yuhrao.serdes.content-negotiation.json :as cn.json]
            [yuhrao.serdes.content-negotiation.yaml :as cn.yaml]
            [yuhrao.serdes.content-negotiation.html :as cn.html]
            [yuhrao.serdes.content-negotiation.plain-text :as cn.plain-text]
            [camel-snake-kebab.core :as csk]
            [camel-snake-kebab.extras :as cske]
            [clojure.string :as string]
            [muuntaja.core :as mtj]
            [muuntaja.format.form :as mtj.form]))

;; Accept what the user want to receive (consider Accept header)
(defn extract-content-type [req-or-res]
  (let [headers (some->> req-or-res
                         :headers
                         (cske/transform-keys csk/->kebab-case-keyword))]
    (->> [(:content-type req-or-res)
          (:content-type headers)]
         (remove empty?)
         (map #(first (string/split % #";")))
         first)))

(defn extract-accept [req]
  (let [headers (some->> req
                         :headers
                         (cske/transform-keys csk/->kebab-case-keyword))]
    (->> [(:accept req)
          (:accept headers)]
         (remove empty?)
         (map #(first (string/split % #";")))
         first)))

(def default-opts
  (-> mtj/default-options
      (assoc :default-format cn.plain-text/mime-type)
      (assoc-in [:formats cn.json/mime-type]
                cn.json/json-format)
      (assoc-in [:formats "application/x-www-form-urlencoded"]
                mtj.form/format)
      (assoc-in [:formats cn.yaml/mime-type]
                cn.yaml/yaml-format)
      (assoc-in [:formats cn.html/mime-type]
                cn.html/html-format)
      (assoc-in [:formats cn.plain-text/mime-type]
                cn.plain-text/plain-text-format)
      (assoc-in [:http :extract-content-type] extract-content-type)
      (assoc-in [:http :extract-accept] extract-accept)))

(def muuntaja
  (mtj/create default-opts))

(defn create-instance
  [{:keys [json-opts]}]
  (cond-> default-opts
          (:encoder json-opts)
          (assoc-in [:formats "application/json" :encoder-opts] (:encoder json-opts))
          (:decoder json-opts)
          (assoc-in [:formats "application/json" :decoder-opts] (:decoder json-opts))
          true mtj/create))

(defn encode
  ([format v]
   (encode muuntaja format v))
  ([muuntaja-or-opts format v]
   (if (mtj/muuntaja? muuntaja-or-opts)
     (mtj/encode muuntaja-or-opts format v)
     (mtj/encode (create-instance muuntaja-or-opts) format v))))

(defn decode
  ([format v]
   (decode muuntaja format v))
  ([muuntaja-or-opts format v]
   (if (mtj/muuntaja? muuntaja-or-opts)
     (mtj/decode muuntaja-or-opts format v)
     (mtj/decode (create-instance muuntaja-or-opts) format v))))
