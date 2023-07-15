(ns br.dev.yuhri.database.interface-test
  (:require [br.dev.yuhri.database.interface :as database]
            [clojure.java.io :as io]
            [clojure.test :as t]
            [matcher-combinators.matchers :as matcher]
            [matcher-combinators.test]
            [next.jdbc :as jdbc]
            [tick.core :as tick]
            [br.dev.yuhri.config.interface :as config]))

(defn ^:private gen-db-spec
  []
  (let [cfg (config/create {:host     {:env     "HOST"
                                       :default "localhost"}
                            :port     {:env      "PORT"
                                       :parse-fn #(Integer/parseInt %)
                                       :default  5432}
                            :dbname   {:env     "DB_NAME"
                                       :default "psql"}
                            :user     {:env     "DB_USER"
                                       :default "app"}
                            :password {:env     "DB_PASSWORD"
                                       :default "app"}})]
    (merge
      {:dbtype   "postgres"}
      cfg)))

(defn prepare-db [db-spec]
  (database/run-migrations {:test {:datastore  db-spec
                                   :migrations "migrations/test"}}
                           {:skip-main? true}))

(defn cleanup-db [db-spec]
  (database/rollback-migration :test {:test {:datastore  db-spec
                                             :migrations "migrations/test"}}))

(t/deftest ^:integration migration
  (let [db-spec    (gen-db-spec)
        datasource (jdbc/get-datasource db-spec)]

    (t/testing "migration up"
      (prepare-db db-spec)
      (with-open [conn (jdbc/get-connection datasource)]
        (let [res (->> (jdbc/execute! conn ["select * from ragtime_migrations"]))]
          (t/is (> (count res) 0)))

        (let [res (->> (jdbc/execute! conn ["SELECT table_name FROM information_schema.tables WHERE table_schema = 'public';"])
                       (map :tables/table_name)
                       set)]
          (t/is (res "ragtime_migrations") "migrations' table should exist")
          (t/is (res "test_table")))))

    (t/testing "migration down"
      (database/rollback-migration :test {:test {:datastore  db-spec
                                                 :migrations "migrations/test"}})
      (with-open [conn (jdbc/get-connection datasource)]
        (let [res (->> (jdbc/execute! conn ["select * from ragtime_migrations"]))]
          (t/is (zero? (count res))))

        (let [res (->> (jdbc/execute! conn ["SELECT table_name FROM information_schema.tables WHERE table_schema = 'public';"])
                       (map :tables/table_name)
                       set)]
          (t/is (res "ragtime_migrations") "migrations' table should exist")
          (t/is (not (res "test_table"))))))
    (cleanup-db db-spec)))

(t/deftest ^:integration dml-dql-operations
  (let [db-spec    (gen-db-spec)
        table-name :test_table]
    (prepare-db db-spec)
    (let [entity {:id         (random-uuid)
                  :name       "honey-sql"
                  :created-at (tick/date)}
          _      (database/insert! db-spec table-name entity)
          res    (first (database/execute! db-spec {:select [:*]
                                                    :from   [table-name]}))]
      (t/is (match? (matcher/equals entity)
                    res)))
    (cleanup-db db-spec)))
