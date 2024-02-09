(ns yuhrao.config.core)

(defn- apply-parse-fn [parse-fn v]
  (when (and parse-fn v)
    (parse-fn v)))

(defn- extract-value [{:keys [env prop parse-fn default]}]
  (let [env-val  (when env
                   (System/getenv env))
        prop-val (when prop
                   (System/getProperty prop))
        [parsed-prop parsed-env] (cond->> [prop-val env-val]
                                          parse-fn (map (partial apply-parse-fn parse-fn)))]
    (or parsed-prop parsed-env default)))

(defn create [opts]
  (->> opts
       (map (juxt first (comp extract-value second)))
       (remove (comp nil? second))
       (into {})))
