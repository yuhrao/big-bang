(ns br.dev.yuhri.database.column-readers
  (:require [next.jdbc.result-set :as rs]
            [tick.core :as tick])
  (:import (java.sql Date Timestamp)))


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
    (tick/date-time v)))
