(ns yuhrao.serdes.core.yaml
  (:require [yuhrao.serdes.formats.yaml :as fmt.yaml]))

(defn yaml->clj
  "Parses a yaml string or stream into a clojure collection.
  It transforms keys to kebab case keyword automatically"
  [v]
  (fmt.yaml/yaml->clj v))

(defn clj->yaml
  "Parses a clojure collection into a yaml string"
  [v]
  (fmt.yaml/clj->yaml v))

(defn clj->yaml-stream
  "Parses a clojure collection into a yaml stream"
  [v]
  (fmt.yaml/clj->yaml-stream v))
