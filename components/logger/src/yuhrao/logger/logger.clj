(ns yuhrao.logger.logger
  (:require [yuhrao.logger.publishers :as pubs]
            [com.brunobonacci.mulog :as u]))

(defonce current-logger (atom {:stop   nil
                               :status :down
                               :opts   nil}))

(def ^:private publisher-mappings
  {:console pubs/console-publisher
   :file    pubs/file-publisher})

(def ^:private log-levels
  {:debug 0
   :info  1
   :warn  2
   :error 3})

(defn- keep-log-by-level? [min-level {:keys [level]}]
  (if (nil? level)
    false
    (>= (log-levels level) (log-levels min-level))))

(defn filter-log-level-xfn [{:keys [min-level]
                             :or   {min-level :info}}]
  (filter (partial keep-log-by-level? min-level)))

(defn apply-obscurer-xfn [{:keys [obscurer]}]
  (map #(update % :data obscurer)))

(defn- default-transform-xfns [{:keys [obscurer] :as opts}]
  (cond-> [(filter-log-level-xfn opts)]
          obscurer (conj (apply-obscurer-xfn opts))))

(defn- create-transform-fn [xfns]
  (let [xfn (apply comp xfns)]
    (fn [events]
      (sequence xfn events))))

(defn- get-standard-publishers [pub pub-cfg]
  (if-let [pub-fn (publisher-mappings pub)]
    (pub-fn pub-cfg)
    pub-cfg))

(defn- create-publishers [{:keys [publishers] :as opts}]
  (let [default-xfns (default-transform-xfns opts)]
    (->> publishers
         (keep (fn [[pub pub-cfg]]
                 (let [{:keys [xfns] :as publisher} (get-standard-publishers pub pub-cfg)
                       transform-fn (create-transform-fn (concat default-xfns xfns))]
                   (-> publisher
                       (assoc :transform transform-fn))))))))

(defn setup! [opts]
  (when (= :down (-> current-logger
                     deref
                     :status))
    (let [publishers (create-publishers opts)
          publishers {:type       :multi
                      :publishers publishers}]
      (reset! current-logger
              {:stop   (u/start-publisher! publishers)
               :status :up
               :opts   opts}))))

(defn stop! []
  (let [{:keys [stop status] :as old} @current-logger]
    (when (= status :up)
      (stop)
      (reset! current-logger (assoc old :status :down)))))

(defn restart! []
  (let [{:keys [status opts]} @current-logger]
    (when (= status :up)
      (stop!)
      (setup! opts))))
