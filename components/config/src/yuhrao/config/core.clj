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

(defn create
  "Creates a configuration map from options.
  Example:
  ```clojure
  (config/create {:my-env  {:env \"PWD\"}
                  :my-prop {:prop \"java.vm.version\"}
                  :both    {:env  \"PWD\"
                            :prop \"java.version\"}})
  ;; => {:my-env  \"/my/path\"
  ;;     :my-prop \"12.1\"
  ;;     :both    \"12.1\"}
  ```

  Config parameters
  - `:env` - Get value from a
  - `:prop` - Get value from a java system property
  - `:default` - Default value if no value is provided
  - `:parse-fn` - Funtion to parse the raw value.

  Notes:
  - `:prop` takes priority over `:env` if both are provided.
  - `:parse-fn` is applied to both `:env` and `:prop` values.
  "
  [opts]
  (->> opts
       (map (juxt first (comp extract-value second)))
       (remove (comp nil? second))
       (into {})))
