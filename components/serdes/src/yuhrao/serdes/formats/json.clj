(ns br.dev.yuhri.serdes.formats.json
  (:require #_[muuntaja.core :as content-negotiation]
            [br.dev.yuhri.serdes.content-negotiation :as content-negotiation]))

(def ^:private default-muuntaja content-negotiation/muuntaja)

(defn json->clj
  ([v]
   (content-negotiation/decode
     default-muuntaja
     "application/json" v))
  ([v opts]
   (content-negotiation/decode
     {:json-opts {:decoder opts}}
     "application/json" v)))

(defn clj->json-stream
  ([v]
   (content-negotiation/encode
     default-muuntaja
     "application/json" v))
  ([v opts]
   (content-negotiation/encode
     {:json-opts {:encoder opts}}
     "application/json" v)))

(defn clj->json
  ([v]
   (slurp (clj->json-stream v)))
  ([v opts]
   (slurp (clj->json-stream v opts))))
