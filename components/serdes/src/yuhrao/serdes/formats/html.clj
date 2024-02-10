(ns yuhrao.serdes.formats.html
  (:require [yuhrao.serdes.content-negotiation :as content-negotiation]))

(def ^:private default-muuntaja content-negotiation/muuntaja)

(def ^:private content-type "text/html")

(defn html->clj
  ([v]
   (content-negotiation/decode
    default-muuntaja
    content-type v))
  ([v opts]
   (content-negotiation/decode
    {:html-opts {:decoder opts}}
    content-type v)))

(defn clj->html-stream
  ([v]
   (content-negotiation/encode
    default-muuntaja
    content-type v))
  ([v opts]
   (content-negotiation/encode
    {:html-opts {:encoder opts}}
    content-type v)))

(defn clj->html
  ([v]
   (slurp (clj->html-stream v)))
  ([v opts]
   (slurp (clj->html-stream v opts))))

