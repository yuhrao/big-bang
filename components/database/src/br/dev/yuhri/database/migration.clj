(ns
  ^{:deprecated true
    :doc "This namespace is deprecated. Use abogoyavlensky/automigrate instead."}
  br.dev.yuhri.database.migration
  (:require [ragtime.next-jdbc :as rag.jdbc]
            [ragtime.repl :as rag.repl]))

(def ^:private default-opts {:main {:migrations "migrations/main"}})

(defn- execute-rollback [migration-opts]
  (-> migration-opts
      (update :migrations rag.jdbc/load-resources)
      (update :datastore rag.jdbc/sql-database)
      rag.repl/rollback))

(defn rollback-migration [db migration-opts]
  (let [default (default-opts db)
        opts    (merge (migration-opts db) default)]
    (execute-rollback opts)))

(defn- execute-migration
  [migration-opts]
  (-> migration-opts
      (update :migrations rag.jdbc/load-resources)
      (update :datastore rag.jdbc/sql-database)
      rag.repl/migrate))

(defn- run-migration [db migration-opts]
  (let [defaults (default-opts db)
        db-opts  (merge defaults migration-opts)]
    (execute-migration db-opts)))

(defn run-migrations
  ([migrations]
   (run-migrations migrations {}))
  ([migrations {:keys [skip-main?] :as _opts}]
   (when (not skip-main?)
     (run-migration :main (:main migrations)))

   (let [migrations (dissoc migrations :main)]
     (doall (map run-migration (keys migrations) (vals migrations)))
     nil)))
