(ns br.dev.yuhri.midi-hub.lib.openmidi
  (:require [br.dev.yuhri.serdes.core.json :as json]
            [camel-snake-kebab.core :as csk]
            [camel-snake-kebab.extras :as cske]
            [clojure.string :as string]
            [medley.core :as medley]))

(defn- get-brands []
  (->> "https://raw.githubusercontent.com/Morningstar-Engineering/openmidi/master/data/mapping.json"
       slurp
       json/json->clj
       :brands
       (map #(update % :value csk/->kebab-case-keyword))))

(defn- extract-kv [[k v]]
  (let [letters-re #"([a-zA-Z]+)"]
    (if (and (re-find letters-re k)
             (not (re-find letters-re v)))
      [v k]
      [k v])))

(defn- assoc-value-mappings [{:keys [description] :as param}]
  (cond
    (string/blank? description)
    param

    (re-find #"(?i)value.?\d.?-.?+|(\w+).?=.\w+" description)
    (->> (string/split description #"\n|,\n?")
         (map #(string/split % #".?-.?|.?=.?"))
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
    param))

(defn deep-remove-empty [v]
  (let [empty-fn #(cond
                    (coll? %) empty?
                    (string? %) string/blank?
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
      :else (when (not ((empty-fn v) v))
              v))))

(defn- deep-trim-strings [v]
  (cond
    (string? v) (->> v
                     string/trim
                     string/trim-newline)
    (map? v) (medley/map-vals deep-trim-strings v)
    (coll? v) (map deep-trim-strings v)))


(defn- get-all-devices-specs []
  (->> "https://raw.githubusercontent.com/Morningstar-Engineering/openmidi/master/data/all.json"
       slurp
       (#(json/json->clj % {:key-fn identity}))
       (mapcat (fn [[brand devices]]
                 (map #(assoc (second %)
                         :value (csk/->snake_case_string (first %))
                         :brand brand) devices)))
       (map (partial cske/transform-keys csk/->kebab-case-keyword))
       (map deep-trim-strings)
       (map (fn [{:keys [cc] :as device}]
              (assoc device :cc (map assoc-value-mappings cc))))
       (map #(-> %
                 (select-keys [:brand :device-name :value])
                 (assoc :io-specs (select-keys % [:midi-in :midi-thru :phantom-power])
                        :midi-specs (select-keys % [:midi-clock :pc :cc :midi-channel]))))
       (map deep-remove-empty)))

(defn- get-specs []
  (let [brands        (get-brands)
        devices-specs (get-all-devices-specs)]
    {:brands  brands
     :devices devices-specs}))
