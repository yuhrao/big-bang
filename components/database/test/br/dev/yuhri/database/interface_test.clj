(ns br.dev.yuhri.database.interface-test
  (:require [clojure.test :as t]
            [next.jdbc :as jdbc]
            [clojure.java.io :as io]
            [matcher-combinators.test]
            [br.dev.yuhri.database.interface :as database]))

(def ^:private db-spec {:dbtype "sqlite"
                        :dbname "test.db"})

(t/deftest ^:integration migration
  (let [migrations {:test {:datastore  db-spec
                           :migrations "migrations/test"}}
        datasource (jdbc/get-datasource db-spec)]

    (t/testing "database is empty"
      (with-open [conn (jdbc/get-connection datasource)]
        (let [res (->> (jdbc/execute! conn ["select name from sqlite_master where type='table' order by name;"]))]
          (t/is (zero? (count res))))))

    (t/testing "migration up"
      (database/run-migrations migrations
                               {:skip-main? true})
      (with-open [conn (jdbc/get-connection datasource)]
        (let [res (->> (jdbc/execute! conn ["select * from ragtime_migrations"]))]
          (t/is (> (count res) 0)))

        (let [res (->> (jdbc/execute! conn ["SELECT name FROM sqlite_master WHERE type='table' ORDER BY name;"])
                       (map :sqlite_master/name)
                       set)]
          (t/is (res "ragtime_migrations") "migrations' table should exist")
          (t/is (res "test_table")))))

    (t/testing "migration down"
        (database/rollback-migration :test migrations)
        (with-open [conn (jdbc/get-connection datasource)]
        (let [res (->> (jdbc/execute! conn ["select * from ragtime_migrations"]))]
          (t/is (zero? (count res))))

        (let [res (->> (jdbc/execute! conn ["SELECT name FROM sqlite_master WHERE type='table' ORDER BY name;"])
                       (map :sqlite_master/name)
                       set)]
          (t/is (res "ragtime_migrations") "migrations' table should exist")
          (t/is (not (res "test_table"))))))
    (io/delete-file (:dbname db-spec))))
