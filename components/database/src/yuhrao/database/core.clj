(ns yuhrao.database.core
  (:require [yuhrao.database.migration :as migrations]
            [yuhrao.database.column-readers]
            [next.jdbc.connection :as jdbc.conn]
            [yuhrao.database.sql-execution :as sql-execution])
  (:import (com.zaxxer.hikari HikariDataSource)))

(defn ^:deprecated run-migrations
  ([migrations]
   (run-migrations migrations {}))
  ([migrations opts]
   (migrations/run-migrations migrations opts)))

(defn ^:deprecated rollback-migration [db migration-opts]
  (migrations/rollback-migration db migration-opts))

(defn insert! [db-spec table-name entity]
  (sql-execution/insert! db-spec table-name entity))

(defn execute! [db-spec sql]
  (sql-execution/execute! db-spec sql))

(defn create-pool [db-spec]
  (jdbc.conn/->pool HikariDataSource db-spec))
