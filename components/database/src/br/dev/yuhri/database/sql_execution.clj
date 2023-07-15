(ns br.dev.yuhri.database.sql-execution
  (:require [next.jdbc :as jdbc]
            [honey.sql :as honey]
            [camel-snake-kebab.core :as csk]))

(defn- unqualify-keys [m]
  (let [unqualify-fn (comp csk/->kebab-case-keyword name)]
    (->> m
         (map (juxt (comp unqualify-fn first) second))
         (into {}))))

(defn- execute-stmt! [db-spec sql]
  (with-open [conn (jdbc/get-connection db-spec)]
    (with-open [stmt (jdbc/prepare conn sql {:return-keys true})]
      (let [res (jdbc/execute! stmt {:return-generated-keys true})]
        (->> res
             (map unqualify-keys))))))

(defn- execute-batch-stmt! [db-spec sql vals]
  (with-open [conn (jdbc/get-connection db-spec)]
    (with-open [stmt (jdbc/prepare conn sql {:return-keys true})]
      (let [res (jdbc/execute-batch! stmt vals {:return-generated-keys true})]
        (->> res
             (map unqualify-keys))))))

(defn execute! [db-spec sql]
  (let [sql (if (vector? sql)
              sql
              (honey/format sql))]
    (execute-stmt! db-spec sql)))

(defn insert! [db-spec table-name entity]
  (let [ks     (->> entity
                    keys
                    (map csk/->snake_case_keyword)
                    vec)
        values (->> entity
                    vals
                    vec)
        sql    (honey/format {:insert-into [table-name]
                              :columns     ks
                              :values      [values]})]
    (execute-stmt! db-spec sql)))

(defn insert-batch! [db-spec table-name entities]
  (let [ks     (->> entities
                    first
                    keys
                    (map csk/->snake_case_keyword)
                    vec)
        values (->> entities
                    (map (comp vec vals))
                    vec)
        sql    (->> {:insert-into [table-name]
                     :columns     ks
                     :values      [(range (count ks))]}
                    honey/format
                    first
                    (conj []))]
    (execute-batch-stmt! db-spec sql values)))
