(ns doc
  (:require [dinodoc.api :as dinodoc]
            [babashka.fs :as fs]))

(def doc-tree [["Big Bang" {:file "README.md"}]]) ;; (1)

(def components ["config" "data-cloak" "database" "feature-flag" "http-client" "logger" "serdes" "webserver"])

(dinodoc/generate
 {:inputs (concat
           [{:path "."
             :doc-tree doc-tree}] ;; (2)
           (map (fn [path]
                  {:path (str "components/" path)
                   :output-path (str "Components/" (fs/file-name path))})
                components)) ;; (3)
  :output-path "docs/docs"})
