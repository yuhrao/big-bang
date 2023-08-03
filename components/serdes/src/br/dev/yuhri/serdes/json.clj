(ns br.dev.yuhri.serdes.json
  (:require [muuntaja.core :as mtj]
            [br.dev.yuhri.serdes.content-negotiation :as content-negotiation]))

(def ^:private default-muuntaja (content-negotiation/muuntaja))

(defn json->clj
  ([v]
   (mtj/decode
     default-muuntaja
     "application/json" v)))

(defn clj->json-stream [v]
  (mtj/encode
    default-muuntaja
    "application/json" v))

(defn clj->json [v]
  (slurp (clj->json-stream v)))
