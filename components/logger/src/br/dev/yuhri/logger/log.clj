(ns br.dev.yuhri.logger.log
  (:require [com.brunobonacci.mulog :as u]))

(defmacro log
  [event-name level message data]
  `(if (seq ~data)
    (u/log ~event-name :level ~level :message ~message :data ~data)
    (u/log ~event-name :level ~level :message ~message)))

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
