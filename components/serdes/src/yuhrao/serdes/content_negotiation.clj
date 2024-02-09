(ns yuhrao.serdes.content-negotiation
  (:require [yuhrao.serdes.content-negotiation.json :as cn.json]
            [yuhrao.serdes.content-negotiation.yaml :as cn.yaml]
            [camel-snake-kebab.core :as csk]
            [camel-snake-kebab.extras :as cske]
            [clojure.string :as string]
            [muuntaja.core :as mtj]
            [muuntaja.format.form :as mtj.form]))

(defn extract-content-type [req-or-res]
  (let [headers (some->> req-or-res
                         :headers
                         (cske/transform-keys csk/->kebab-case-keyword))]
    (->> [(:content-type req-or-res)
          (:content-type headers)
          "application/json"]
         (remove empty?)
         (map #(first (string/split % #";")))
         first)))

(def default-opts
  (-> mtj/default-options
      (assoc-in [:formats "application/json"]
                cn.json/json-format)
      (assoc-in [:formats "application/x-www-form-urlencoded"]
                mtj.form/format)
      (assoc-in [:formats "application/yaml"]
                cn.yaml/yaml-format)
      (assoc-in [:http :extract-content-type] extract-content-type)))

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
