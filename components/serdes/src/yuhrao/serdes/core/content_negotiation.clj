(ns yuhrao.serdes.core.content-negotiation
  (:require [yuhrao.serdes.content-negotiation :as content-negotiation]))

(defn muuntaja
  "Gets the default muuntaja instance for content negotiation"
  []
  content-negotiation/muuntaja)

(defn extract-content-type
  "Extract content type from request or response.
  If it's not present, defaults to application/json"
  [req-or-res]
  (content-negotiation/extract-content-type req-or-res))

(defn encode
  "Encode a value into a specified format (e.g. application/json)"
  ([format v]
   (encode (muuntaja) format v))
  ([muuntaja format v]
   (content-negotiation/encode muuntaja format v)))

(defn decode
  "Decode various formats into a clojure value (usually a coll)"
  ([format v]
   (decode (muuntaja) format v))
  ([muuntaja format v]
   (content-negotiation/decode muuntaja format v)))
