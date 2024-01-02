(ns br.dev.yuhri.midi-hub.lib.openmidi
  (:require [br.dev.yuhri.serdes.core.json :as json]
            [camel-snake-kebab.core :as csk]
            [camel-snake-kebab.extras :as cske]
            [clojure.string :as string]
            [br.dev.yuhri.midi-hub.lib.db :as mh.db]
            [medley.core :as medley]
            [clojure.set :as set]))

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

(defn- device-missing-data [{:keys [device-name value midi-specs io-specs] :as device}]
  (cond-> #{}

          (string/blank? device-name)
          (conj :device-name)

          (empty? midi-specs)
          (conj :midi-specs)

          (empty? io-specs)
          (conj :io-specs)))

(defn- make-device [{:keys [value] :as device}]
  (let [missing-data (device-missing-data device)]
   (cond-> device

           (seq missing-data)
           (assoc-in [:metadata :missing-data] (sort (vec missing-data)))

           (missing-data :device-name) (assoc :device-name value))))

(defn- get-all-devices-specs []
  (->> "https://raw.githubusercontent.com/Morningstar-Engineering/openmidi/master/data/all.json"
       slurp
       (#(json/json->clj % {:key-fn identity}))
       (mapcat (fn [[brand devices]]
                 (map #(assoc (second %)
                         :value (first %)
                         :brand-value brand) devices)))
       (map (partial cske/transform-keys csk/->kebab-case-keyword))
       (map deep-trim-strings)
       (map (fn [{:keys [cc] :as device}]
              (assoc device :cc (map assoc-value-mappings cc))))
       (map #(-> %
                 (select-keys [:brand-value :device-name :value])
                 (assoc :io-specs (select-keys % [:midi-in :midi-thru :phantom-power])
                        :midi-specs (select-keys % [:midi-clock :pc :cc :midi-channel]))))
       (map deep-remove-empty)
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
      (tap> (mh.db/upsert-device (-> device
                                (assoc
                                  :brand-id
                                  (brands-value-id (:brand-value device)))
                                (dissoc :brand-value)))))
    nil))
