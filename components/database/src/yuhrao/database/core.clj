(ns yuhrao.database.core
  (:require [yuhrao.database.migration :as migrations]
            [yuhrao.database.column-readers]
            [next.jdbc.connection :as jdbc.conn]
            [yuhrao.database.sql-execution :as sql-execution])
  (:import (com.zaxxer.hikari HikariDataSource)))

(defn ^:deprecated run-migrations
  "Run all migrations in the migrations map.
  DEPRECATED USE [abogoyavlensky/automigrate](https://github.com/abogoyavlensky/automigrate) INSTEAD"
  ([migrations]
   (run-migrations migrations {}))
  ([migrations opts]
   (migrations/run-migrations migrations opts)))

(defn ^:deprecated rollback-migration
  "Rollback a migration in the migrations map.
  DEPRECATED USE [abogoyavlensky/automigrate](https://github.com/abogoyavlensky/automigrate) INSTEAD"

  [db migration-opts]
  (migrations/rollback-migration db migration-opts))

(defn insert!
  "Inserts an entity into the table"
  [db-spec table-name entity]
  (sql-execution/insert! db-spec table-name entity))

(defn execute!
  "Executes a sql statement.
  `sql` can be a string or a honeysql query"
  [db-spec sql]
  (sql-execution/execute! db-spec sql))

(defn create-pool
  "Creates a connection pool from a next.jdbc db-spec"
  [db-spec]
  (jdbc.conn/->pool HikariDataSource db-spec))
