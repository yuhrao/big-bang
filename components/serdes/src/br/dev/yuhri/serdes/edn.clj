(ns br.dev.yuhri.serdes.edn
  (:require [muuntaja.core :as mtj]
            [br.dev.yuhri.serdes.content-negotiation :as content-negotiation]))

(def ^:private default-muuntaja (content-negotiation/muuntaja))


(defn edn->clj [v]
  (mtj/decode
    default-muuntaja
    "application/edn" v))

(defn clj->edn-stream [v]
  (mtj/encode
    default-muuntaja
    "application/edn" v))

(defn clj->edn [v]
  (slurp (clj->edn-stream v)))
