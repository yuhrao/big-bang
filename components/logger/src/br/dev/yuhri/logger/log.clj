(ns br.dev.yuhri.logger.log
  (:require [com.brunobonacci.mulog :as u]))

(defmacro log
  [event-name level message data]
  (if data
    `(u/log ~event-name :level ~level :message ~message :data ~data)
    `(u/log ~event-name :level ~level :message ~message)))

(defn ->standard-trace-opts [trace-opts]
  (let [trace-opts (if (map? trace-opts)
                     (update trace-opts :pairs #(conj % :level :info))
                     (conj trace-opts :level :info))
        capture-fn (:capture trace-opts)
        capture-fn (fn [data]
                     (when capture-fn
                       (when-let [res (capture-fn data)]
                         {:data res})))
        trace-opts (if (map? trace-opts)
                     (assoc trace-opts :capture
                                       capture-fn)
                     {:pairs   trace-opts
                      :capture capture-fn})]
    trace-opts))

(defmacro trace
  ""
  {:style/indent 1}
  ([event-name trace-opts & body]
   `(let [opts#    ~trace-opts
          capture# (:capture ~trace-opts)
          capture# (fn [data#]
                     (when capture#
                       (when-let [res# (capture# data#)]
                         {:data res#})))
          opts#    (if (map? opts#)
                     (-> opts#
                         (update :pairs conj :level :info)
                         (assoc :capture capture#))
                     (conj opts# :level :info))]
      (u/trace ~event-name opts# ~@body))))
