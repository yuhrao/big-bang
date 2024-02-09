(ns br.dev.yuhri.midi-hub.lib.openmidi
  (:require [br.dev.yuhri.serdes.core.json :as json]
            [camel-snake-kebab.core :as csk]
            [camel-snake-kebab.extras :as cske]
            [clojure.string :as string]
            [br.dev.yuhri.midi-hub.lib.db :as mh.db]
            [medley.core :as medley]
            [clojure.set :as set]
            [malli.core :as malli]))

(defn- get-brands []
  (->> "https://raw.githubusercontent.com/Morningstar-Engineering/openmidi/master/data/mapping.json"
       slurp
       json/json->clj
       :brands
       (map #(set/rename-keys % {:name :brand-name}))))

(defn- extract-kv [[k v]]
  (let [letters-re #"([a-zA-Z]+)"]
    (if (and (re-find letters-re k)
             (not (re-find letters-re v)))
      [v k]
      [k v])))

;; CC values has some standards
;; CC list can have multiple parameters with same "value".
;; That means that it can be a range or a value mapping
;; if min = max => value_mapping
;; if min != max => range
(defn- assoc-value-mappings [{:keys [description] :as param}]
  (try
    (cond
      (not (string? description)) param

      (string/blank? description)
      param

      (re-find #"(?i)(value)?.?\d\s?-\s?+|(\w+)\s?=\s?\w+" description)
      (->> (string/split description #"\n|,\n?|\s?,\s?")
           (map #(string/split % #"-|="))
           (map (fn [kv]
                  (let [[k v] (extract-kv kv)]
                    [(-> k
                         (string/replace #"(?i)value." "")
                         string/trim
                         string/trim-newline)

                     (-> v string/trim string/trim-newline)])))
           (into {})
           (assoc param :value-mapping))

      :else
      param)
    (catch Throwable e
      param)))

(defn deep-remove-empty [v]
  (let [empty-fn #(cond
                    (string? %) string/blank?
                    (coll? %) empty?
                    :else nil?)]
    (cond
      (map? v) (->> v
                    (reduce-kv (fn [m k mv]
                                 (if ((empty-fn mv) mv)
                                   m
                                   (assoc m k (deep-remove-empty mv))))
                               {})
                    (medley/remove-vals #((empty-fn %) %)))
      (coll? v) (->> v
                     (map deep-remove-empty)
                     (remove #((empty-fn %) %)))
      (string? v) (when (not ((empty-fn v) v))
                    v)
      :else (when (not (nil? v))
              v))))

(defn deep-trim-strings [v]
  (cond
    (string? v) (->> v
                     string/trim
                     string/trim-newline)
    (map? v) (medley/map-vals deep-trim-strings v)
    (coll? v) (map deep-trim-strings v)
    :else v))

(defonce dbg (atom nil))

(def ^:private IoSpecs
  [:map
   [:midi-thru :boolean]
   [:midi-in :string]
   [:pahtom-power :boolean]])

(def ^:private MidiSpecs
  [:map
   [:midi-clock :boolean]
   [:cc [:sequential any?]]])

(def ^:private DeviceSpecs
  [:map
   [:io-specs IoSpecs]
   [:midi-specs MidiSpecs]
   [:device-name :string]])

(defn- device-missing-data [device]
  (->> (malli/explain DeviceSpecs device)
       :errors
       (map (comp last :path))
       set))

;; TODO: put in utils component
(defn- safe-re-find [re s]
  (when (not (string/blank? s))
    (re-find re s)))

(defn yes-no-none->boolean [v]
  (when (not (string/blank? v))
    (cond
      (safe-re-find #"(?i)yes" v) true
      (safe-re-find #"(?i)no|none" v) false
      :else nil)))

(defn- assoc-metadata [{:keys [value] :as device}]
  (let [missing-data (device-missing-data device)]
    (cond-> device

      (seq missing-data)
      (assoc-in [:metadata :missing-data] (sort (vec missing-data)))

      (missing-data :device-name) (assoc :device-name value))))

(defn- make-device [{:keys [midi-in midi-thru phantom-power midi-clock
                            cc]
                     :as   device}]
  (let [io-specs   {:midi-in       (safe-re-find #"(?i)TRS|Tip.Active|Ring.Active|DIN5|USB" midi-in)
                    :midi-thru     (cond
                                     (nil? midi-thru)  midi-thru
                                     (string? midi-thru) (yes-no-none->boolean midi-thru)
                                     :else               midi-thru)
                    :phantom-power (cond
                                     (nil? phantom-power)  phantom-power
                                     (string? phantom-power) (yes-no-none->boolean phantom-power)
                                     :else                   phantom-power)}
        midi-specs {:cc         (map assoc-value-mappings cc)
                    :midi-clock (cond
                                  (nil? midi-clock)  midi-clock
                                  (string? midi-clock) (yes-no-none->boolean midi-clock)
                                  :else                midi-clock)}]
    (-> device
        (select-keys [:brand-value :device-name :value])
        (assoc
         :io-specs io-specs
         :midi-specs midi-specs)
        deep-trim-strings
        deep-remove-empty
        assoc-metadata)))

(defn- get-all-devices-specs []
  (->> "https://raw.githubusercontent.com/Morningstar-Engineering/openmidi/master/data/all.json"
       slurp
       (#(json/json->clj % {:key-fn identity}))
       (mapcat (fn [[brand devices]]
                 (map #(assoc (second %)
                              :value (first %)
                              :brand-value brand) devices)))
       (map (partial cske/transform-keys csk/->kebab-case-keyword))
       (map make-device)))

(defn- get-specs []
  (let [brands        (get-brands)
        devices-specs (get-all-devices-specs)]
    {:brands  brands
     :devices devices-specs}))

(defn import-specs []
  (let [{:keys [devices brands]} (get-specs)
        brands          (for [brand brands]
                          (merge brand (mh.db/upsert-brand (dissoc brand :models))))
        brands-value-id (->> brands
                             (map (juxt :value :id))
                             (into {}))]
    (doseq [device devices]
      (mh.db/upsert-device (-> device
                               (assoc
                                :brand-id
                                (brands-value-id (:brand-value device)))
                               (dissoc :brand-value))))
    nil))

;; TODO: get list of devices with incomplete data
(comment

  (import-specs)

  (tap> (take 10 (get-all-devices-specs)))

  (take 10 (get-all-devices-specs))

  @dbg

  (->> (mh.db/list-devices
        {:select [:d.metadata, [:d.value :device_value] [:b.value :brand_value]]
         :join [[:brands :b] [:= :b.id :brand_id]]
         :where [[:is-not :metadata nil]]})
       (sort-by :brand_value)
       (group-by :brand_value)
       (map (juxt first (comp (partial into {}) (partial map (juxt :device_value (comp :missing-data :metadata))) second)))
       (into (sorted-map))
       tap>)

  (->> (get-all-devices-specs)
       count)

  )
