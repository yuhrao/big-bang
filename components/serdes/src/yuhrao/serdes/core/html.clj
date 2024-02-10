(ns yuhrao.serdes.core.html
  (:require [yuhrao.serdes.formats.html :as fmt.html]))

(defn clj->html
  "Parses a clojure collection (hiccup) into a html string"
  ([v]
   (fmt.html/clj->html v))
  ([v opts]
   (fmt.html/clj->html v opts)))

(defn clj->html-stream
  "Parses a clojure collection (hiccup) into a html stream"
  ([v]
   (fmt.html/clj->html-stream v))
  ([v opts]
   (fmt.html/clj->html-stream v opts)))
