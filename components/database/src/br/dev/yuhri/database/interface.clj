(ns br.dev.yuhri.database.interface
  (:require [br.dev.yuhri.database.migration :as migrations]
            [br.dev.yuhri.database.column-readers]
            [br.dev.yuhri.database.sql-execution :as sql-execution]))

(defn run-migrations
  ([migrations]
   (run-migrations migrations {}))
  ([migrations opts]
   (migrations/run-migrations migrations opts)))

(defn rollback-migration [db migration-opts]
  (migrations/rollback-migration db migration-opts))

(defn insert! [db-spec table-name entity]
  (sql-execution/insert! db-spec table-name entity))

(defn execute! [db-spec sql]
  (sql-execution/execute! db-spec sql))
