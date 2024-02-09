(ns br.dev.yuhri.database.column-readers
  (:require [next.jdbc.result-set :as rs]
            [next.jdbc.prepare :as prepare]
            [tick.core :as tick]
            [br.dev.yuhri.serdes.core.json :as json])
  (:import (java.sql Date Timestamp Array PreparedStatement)
           (org.postgresql.util PGobject)
           (clojure.lang ISeq IPersistentMap IPersistentVector Keyword)))


(defn- pgobject->clj [^PGobject v]
  (when-let [value (.getValue v)]
    (case (.getType v)
      ("jsonb" "json") (json/json->clj value)
      value)))

(defn- clj->pgobject [v]
  (doto (PGobject.)
    (.setType "json")
    (.setValue (json/clj->json v))))

(extend-protocol rs/ReadableColumn
  Date
  (read-column-by-label [^Date v _]
    (tick/date (.toLocalDate v)))
  (read-column-by-index [^Date v _2 _3]
    (tick/date (.toLocalDate v)))

  Timestamp
  (read-column-by-label [^Timestamp v _]
    (tick/date-time v))
  (read-column-by-index [^Timestamp v _2 _3]
    (tick/date-time v))

  PGobject
  (read-column-by-label [^PGobject v _]
    (pgobject->clj v))
  (read-column-by-index [^PGobject v _2 _3]
    (pgobject->clj v))

  Array
  (read-column-by-label [^Array v _]
    (into [] (.getArray v)))
  (read-column-by-index [^Array v _2 _3]
    (into [] (.getArray v))))

(extend-protocol prepare/SettableParameter
  IPersistentMap
  (set-parameter [m ^PreparedStatement s i]
    (.setObject s i (clj->pgobject m)))

  ISeq
  (set-parameter [v ^PreparedStatement stmt ^long i]
    (prepare/set-parameter (with-meta (vec v) (meta v)) stmt i))

  IPersistentVector
  (set-parameter [v ^PreparedStatement stmt i]
    (let [conn      (.getConnection stmt)
          type-name (-> stmt
                        (.getParameterMetaData)
                        (.getParameterTypeName i)
                        (clojure.string/replace #"[^A-Za-z]" ""))]
      (.setObject stmt i (.createArrayOf conn type-name (to-array v)))))

  Keyword
  (set-parameter [v ^PreparedStatement stmt ^long i]
    (prepare/set-parameter (name v) stmt i)))
