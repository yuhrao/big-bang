(ns yuhrao.serdes.formats.yaml
(:require [muuntaja.core :as mtj]
  [yuhrao.serdes.content-negotiation :as content-negotiation]))

(def ^:private default-muuntaja content-negotiation/muuntaja)

(defn yaml->clj
  ([v]
   (mtj/decode
     default-muuntaja
     "application/yaml" v)))

(defn clj->yaml-stream [v]
  (mtj/encode
    default-muuntaja
    "application/yaml" v))

(defn clj->yaml [v]
  (slurp (clj->yaml-stream v)))
