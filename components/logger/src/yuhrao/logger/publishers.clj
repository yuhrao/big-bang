(ns yuhrao.logger.publishers)

(defn console-publisher [pub-config]
  (when pub-config
    (merge pub-config {:type :console})))

(defn file-publisher [pub-config]
  (when pub-config
    (merge pub-config {:type :simple-file})))
