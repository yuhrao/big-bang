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

(defn build-component [component]
  (let [lib (symbol group-name component)
        class-dir (format "components/%s/target/classes" component)
        jar-file (format "components/%s/target/%s.jar" component (name lib))
        src-dir (format "components/%s/src" component)
        resource-dir (format "components/%s/resources" component)]
    (b/write-pom {:class-dir class-dir
                  :lib lib
                  :version version
                  :basis (b/create-basis {:project (format "components/%s/deps.edn" component)})
                  :src-dirs [src-dir]
                  :src-pom (format "components/%s/pom.xml" component)
                  :pom-data [[:licenses
                              [:license
                               [:name "Eclipse Public License 1.0"]
                               [:url "https://opensource.org/license/epl-1-0/"]]]]})
    (b/copy-dir {:src-dirs [src-dir resource-dir]
                 :target-dir class-dir})
    (b/jar {:class-dir class-dir
            :jar-file jar-file})))

(def components ["config" "data-cloak" "logger" "serdes" "feature-flag" "http-client" "database" "webserver"])

(defn clean [_]
  (println "Cleaning up builds")
  (doseq [component components]
  (println "Cleaning" component)
    (fs/delete-tree (fs/file (format "components/%s/target" component)))
    (fs/delete-if-exists (format "%s-%s.pom.asc" component version))))

(defn deploy [component]
  (println "Deploying:" component "=====================")
  (d/deploy
   {:installer      :remote
    :sign-releases? false
    :pom-file       (b/pom-path {:lib       (symbol group-name component)
                                 :class-dir (format "components/%s/target/classes" component)})
    :artifact       (b/resolve-path (format "components/%s/target/%s.jar" component component))})
  (println "============================================"))

(defn build [{:keys [deploy?]
              :or {deploy? false}
              :as _opts}]
  (clean nil)
  (doseq [component components]
    (println "Building:" component)
    (build-component component)
    (when deploy?
      (deploy component))))
