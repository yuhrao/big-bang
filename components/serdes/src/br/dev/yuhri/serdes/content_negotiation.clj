(ns br.dev.yuhri.serdes.content-negotiation
  (:require [br.dev.yuhri.serdes.content-negotiation.json :as cn.json]
            [br.dev.yuhri.serdes.content-negotiation.yaml :as cn.yaml]
            [camel-snake-kebab.core :as csk]
            [camel-snake-kebab.extras :as cske]
            [clojure.string :as string]
            [muuntaja.core :as mtj]
            [muuntaja.format.form :as mtj.form]
            [muuntaja.format.yaml :as mtj.yaml]))

mtj.yaml/format

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

(defn encode
  ([format v]
   (encode muuntaja format v))
  ([muuntaja format v]
   (mtj/encode muuntaja format v)))

(defn decode
  ([format v]
   (encode muuntaja format v))
  ([muuntaja format v]
   (mtj/decode muuntaja format v)))
