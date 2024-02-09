(ns dev.database
  (:require [babashka.process :as p]
            [yuhrao.config.core :as config]))


(def configs (config/create {:midi-hub {:env     "MIDI_HUB_JDBC_URL"
                                        :default "jdbc:postgresql://localhost:5433/the_midi_hub?user=app&password=app"}}))

(defn make-migration [component]
  (if-let [jdbc-url (configs component)]
    (p/shell {:dir (str "components/" (name component))
              :out *out*
              :err :out}
             "clojure" "-X:migration" "make" ":jdbc-url" jdbc-url)
    (println "Component not found:" (name component)))
  nil)

(defn make-migrations []
  (for [component (keys configs)]
    (do
      (println "Making migration:" (name component))
      (make-migration component)))
  nil)

(defn run-migration [component]
  (if-let [jdbc-url (configs component)]
    (do
      (p/shell {:dir (str "components/" (name component))
                :out *out*
                :err :out}
               "clojure" "-X:migration" "migrate" ":jdbc-url" jdbc-url))
    (println "Component not found:" (name component)))
  nil)

(defn run-migrations []
  (for [component (keys configs)]
    (run-migration component))
  nil)


(defn do-migration [component]
  (make-migration component)
  (run-migration component))

(defn do-migrations []
  (for [component (keys configs)]
    (do-migration component)))

(comment
  (make-migrations)

  (run-migrations)

  (do-migrations)

  )
