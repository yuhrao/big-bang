(ns br.dev.yuhri.database.interface
  (:require [br.dev.yuhri.database.migration :as migrations]))

(defn run-migrations
  ([migrations]
   (run-migrations migrations {}))
  ([migrations opts]
   (migrations/run-migrations migrations opts)))

(defn rollback-migration [db migration-opts]
  (migrations/rollback-migration db migration-opts))
