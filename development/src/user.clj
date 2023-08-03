(ns user
  (:require [br.dev.yuhri.database.core :as db]))

(defn run-migrations []
  (let [opts {:main {:datastore {:dbtype "postgres"
                                 :dbname "psql"
                                 :host "localhost"
                                 :port 5432
                                 :user "app"
                                 :password "app"}}}]
    (db/run-migrations opts)))

(comment
  (run-migrations)

  )
