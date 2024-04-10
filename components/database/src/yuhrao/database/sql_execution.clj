(ns yuhrao.database.sql-execution
  (:require [next.jdbc :as jdbc]
            [honey.sql :as honey]))

(defn- unqualify-keys [m]
  (let [unqualify-fn (comp keyword name)]
    (->> m
         (map (juxt (comp unqualify-fn first) second))
         (into {}))))

(defn- execute-stmt! [conn sql]
  (with-open [stmt (jdbc/prepare conn sql {:return-keys true})]
    (let [res (jdbc/execute! stmt {:return-generated-keys true})]
      (->> res
           (map unqualify-keys)))))

(defn- execute-batch-stmt! [conn sql vals]
  (with-open [stmt (jdbc/prepare conn sql {:return-keys true})]
    (let [res (jdbc/execute-batch! stmt vals {:return-generated-keys true})]
      (->> res
           (map unqualify-keys)))))

(defn execute! [conn sql]
  (let [sql (if (vector? sql)
              sql
              (honey/format sql))]
    (execute-stmt! conn sql)))

(defn insert! [conn table-name entity]
  (let [ks     (->> entity
                    keys
                    vec)
        values (->> entity
                    vals
                    vec)
        sql    (honey/format {:insert-into [table-name]
                              :columns     ks
                              :values      [values]})]
    (execute-stmt! conn sql)))

(defn insert-batch! [conn table-name entities]
  (let [ks     (->> entities
                    first
                    keys
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
    (execute-batch-stmt! conn sql values)))
