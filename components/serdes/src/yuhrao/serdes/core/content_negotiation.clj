(ns yuhrao.serdes.core.content-negotiation
  (:require [yuhrao.serdes.content-negotiation :as content-negotiation]))

(defn muuntaja
  "Gets the default muuntaja instance for content negotiation"
  []
  content-negotiation/muuntaja)

(defn extract-content-type
  "Extract content type from request or response.
  If it's not present, defaults to application/json"
  ([req-or-res]
   (extract-content-type req-or-res nil))
  ([req-or-res not-found]
   (or (content-negotiation/extract-content-type req-or-res)
       not-found)))

(defn extract-accept
  "Extract accept from request.
  If it's not present, defaults to application/json"
  ([req]
   (extract-accept req nil))
  ([req not-found]
   (or (content-negotiation/extract-accept req)
       not-found)))

(defn encode
  "Encode a value into a specified format (e.g. application/json)"
  ([format v]
   (encode (muuntaja) format v))
  ([muuntaja format v]
   (content-negotiation/encode muuntaja format v)))

(defn decode
  "Decode various formats into a clojure value (usually a collection)"
  ([format v]
   (decode (muuntaja) format v))
  ([muuntaja format v]
   (content-negotiation/decode muuntaja format v)))

;; TODO: unify this because it's declared in various places
(def mime-types
  {:json "application/json"
   :html "text/html"
   :yaml "application/yaml"
   :edn "application/edn"})
