(ns build
  (:require [clojure.tools.build.api :as b]
            [deps-deploy.deps-deploy :as d]
            [babashka.fs :as fs]
            [clojure.string :as string]))

(def version (as-> (b/git-process {:git-args "tag"}) $
               (string/split-lines $)
               (sort $)
               (last $)
               (string/replace $ #"v" "")))

(def group-name "io.github.yuhrao")

;; delay to defer side effects (artifact downloads)
(def basis (delay (b/create-basis {:project "deps.edn"})))

(defn build-component [component]
  (let [lib (symbol group-name component)
        class-dir (format "components/%s/target/classes" component)
        jar-file (format "components/%s/target/%s.jar" component (name lib))]
    (b/write-pom {:class-dir class-dir
                  :lib lib
                  :version version
                  :basis @basis
                  :src-dirs ["src"]
                  :src-pom (format "components/%s/pom.xml" component)
                  :pom-data [[:licenses
                              [:license
                               [:name "Eclipse Public License 1.0"]
                               [:url "https://opensource.org/license/epl-1-0/"]]]]})
    (b/copy-dir {:src-dirs ["src" "resources"]
                 :target-dir class-dir})
    (b/jar {:class-dir class-dir
            :jar-file jar-file})))

(def components ["config" "data-cloak" "database" "feature-flag" "http-client" "logger" "serdes" "webserver"])
;; (def components ["config"])

(defn clean [_]
  (println "Cleaning up builds")
  (doseq [component components]
    (fs/delete-if-exists (format "components/%s/target" component))
    (fs/delete-if-exists (format "%s-%s.pom.asc" component version))))

(defn jar [_]
  (clean nil)
  (doseq [component components]
    (println "Building:" component)
    (build-component component)))

(defn deploy [_]
  (doseq [component components]
    (println "Deploying:" component "=====================")
    (d/deploy
     {:installer      :remote
      :sign-releases? false
      :pom-file       (format "components/%s/target/classes/META-INF/maven/%s/%s/pom.xml" component group-name component)
      :artifact       (format "components/%s/target/%s.jar" component component)})
    (println "============================================")))
