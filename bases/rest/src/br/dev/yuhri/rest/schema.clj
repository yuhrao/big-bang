(ns br.dev.yuhri.rest.schema
  (:require [tick.core :as tick]
            [malli.core :as malli]))

(def UUID (malli/schema [uuid? {:decode/string parse-uuid
                                :encode/string str}]))

(def DateTime (malli/-simple-schema
               {:type            :date-time
                :pred            tick/date-time?
                :type-properties {:decode/string tick/date-time
                                  :encode/string tick/format}}))
