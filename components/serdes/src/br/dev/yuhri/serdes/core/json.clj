(ns br.dev.yuhri.serdes.core.json
  (:require [br.dev.yuhri.serdes.formats.json :as fmt.json]))

(defn json->clj
  "Parses a json string or stream into a clojure collection.
  It transforms keys to kebab case keyword automatically"
  [v]
  (fmt.json/json->clj v))

(defn clj->json
  "Parses a clojure collection into a json string"
  [v]
  (fmt.json/clj->json v))

(defn clj->json-stream
  "Parses a clojure collection into a json stream"
  [v]
  (fmt.json/clj->json-stream v))
