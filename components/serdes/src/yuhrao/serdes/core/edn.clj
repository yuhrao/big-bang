(ns br.dev.yuhri.serdes.core.edn
  (:require [br.dev.yuhri.serdes.formats.edn :as fmt.edn]))

(defn edn->clj
  "Parses a edn string or stream into a clojure collection.
  It doesn't do any transformation over keys"
  [v]
  (fmt.edn/edn->clj v))

(defn clj->edn
  "Parses a clojure collection into a edn string"
  [v]
  (fmt.edn/clj->edn v))

(defn clj->edn-stream
  "Parses a clojure collection into a edn stream"
  [v]
  (fmt.edn/clj->edn-stream v))
