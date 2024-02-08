(ns br.dev.yuhri.midi-hub.lib.db
  (:require [br.dev.yuhri.config.core :as config]
            [br.dev.yuhri.database.core :as db]
            [camel-snake-kebab.core :as csk]
            [camel-snake-kebab.extras :as cske]
            [next.jdbc.connection :as jdbc.conn]
            [medley.core :as medley]
            [honey.sql :as hsql]
            [tick.core :as t]))

(def ^:private db-specs (config/create {:jdbcUrl     {:env     "MIDI_HUB_JDBC_URL"
                                                      :default (jdbc.conn/jdbc-url
                                                                {:dbtype   "postgres"
                                                                 :dbname   "the_midi_hub"
                                                                 :host     "localhost"
                                                                 :port     5433
                                                                 :username "app"
                                                                 :password "app"})}

                                        :username    {:env     "MIDI_HUB_JDBC_USER"
                                                      :default "app"}
                                        :password    {:env     "MIDI_HUB_JDBC_PWD"
                                                      :default "app"}
                                        :maxPoolSize {:env      "MIDI_HUB_DB_MAX_POOL_SIZE"
                                                      :default  3
                                                      :parse-fn #(Integer/parseInt %)}}))

(defonce ds (db/create-pool db-specs))

(defn create-brand [{:keys [id] :as payload}]
  (let [payload (cske/transform-keys csk/->snake_case payload)
        payload (cond-> payload
                  true
                  (assoc :created_at (t/date-time)
                         :updated_at (t/date-time))

                  (not id)
                  (assoc :id (random-uuid)))
        sql     (hsql/format {:insert-into :brands
                              :columns     (vec (keys payload))
                              :values      [(vec (vals payload))]})]
    (first (db/execute! ds sql))))

(defn upsert-brand [{:keys [id] :as payload}]
  (let [payload (cske/transform-keys csk/->snake_case payload)
        payload (cond-> payload
                  true
                  (assoc
                   :created_at (t/date-time)
                   :updated_at (t/date-time))

                  (not id)
                  (assoc :id (random-uuid)))
        sql     (hsql/format {:insert-into   :brands
                              :columns       (vec (keys payload))
                              :values        [(vec (vals payload))]
                              :on-conflict   [:value]
                              :do-update-set {:fields [:brand_name :updated_at]}})]
    (first (db/execute! ds sql))))

(defn list-brands
  ([]
   (db/execute! ds {:select [:*]
                    :from   :brands}))
  ([hsql-dql]
   (db/execute! ds (merge
                    {:select [:*]}
                    hsql-dql
                    {:from [[:brands :b]]}))))

(defn- prepare-device-creation [{:keys [id midi-specs io-specs metadata] :as payload}]
  (let [payload (cske/transform-keys csk/->snake_case payload)
        payload (assoc payload
                       :created_at (t/date-time)
                       :updated_at (t/date-time))]
    (cond-> payload

      midi-specs (assoc :midi_specs [:param :midi-specs])
      io-specs (assoc :io_specs [:param :io-specs])
      metadata (assoc :metadata [:param :metadata])

      (not id)
      (assoc :id (random-uuid)))))

(defn- prepare-device-json-fields [{:keys [midi-specs io-specs metadata]}]
  (let [norm-fn (partial cske/transform-keys csk/->snake_case_keyword)]
    (medley/map-vals norm-fn
                     {:midi-specs midi-specs
                      :io-specs   io-specs
                      :metadata   metadata})))

(defn create-device [payload]
  (let [payload* (prepare-device-creation payload)
        sql     (hsql/format {:insert-into :devices
                              :columns     (vec (keys payload*))
                              :values      [(vec (vals payload*))]}
                             {:params (prepare-device-json-fields payload)})]
    (first (db/execute! ds sql))))

(defn upsert-device [payload]
  (let [payload* (prepare-device-creation payload)
        sql     (hsql/format {:insert-into   :devices
                              :columns       (vec (keys payload*))
                              :values        [(vec (vals payload*))]
                              :on-conflict   [:value]
                              :do-update-set {:fields [:midi_specs
                                                       :io_specs
                                                       :description
                                                       :device_name
                                                       :metadata]}}
                             {:params (prepare-device-json-fields payload)})]
    (first (db/execute! ds sql))))

(defn list-devices
  ([]
   (db/execute! ds {:select [:*]
                    :from   :devices}))
  ([hsql-dql]
   (db/execute! ds (merge
                    {:select [:*]}
                    hsql-dql
                    {:from [[:devices :d]]}))))
