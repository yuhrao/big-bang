(ns br.dev.yuhri.serdes.core
  (:require [br.dev.yuhri.serdes.json :as json]
            [br.dev.yuhri.serdes.edn :as edn]
            [br.dev.yuhri.serdes.content-negotiation :as content-negotiation]))

(defn muuntaja
  "Creates a muuntaja instance with standardized serdes options"
  []
  (content-negotiation/muuntaja))

(defn json->clj
  "Parses a json string or stream into a clojure collection.
  It transforms keys to kebab case keyword automatically"
  [v]
  (json/json->clj v))

(defn clj->json
  "Parses a clojure collection into a json string"
  [v]
  (json/clj->json v))

(defn clj->json-stream
  "Parses a clojure collection into a json stream"
  [v]
  (json/clj->json-stream v))

(defn edn->clj
  "Parses a edn string or stream into a clojure collection.
  It doesn't do any transformation over keys"
  [v]
  (edn/edn->clj v))

(defn clj->edn
  "Parses a clojure collection into a edn string"
  [v]
  (edn/clj->edn v))

(defn clj->edn-stream
  "Parses a clojure collection into a edn stream"
  [v]
  (edn/clj->edn-stream v))
