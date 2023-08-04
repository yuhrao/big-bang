(ns br.dev.yuhri.logger.test-tooling
  (:require [com.brunobonacci.mulog.buffer :as rb]
            [com.brunobonacci.mulog.core :as uc]
            [br.dev.yuhri.logger.core :as logger]))

(deftype MemoryPublisher [config buffer]
  com.brunobonacci.mulog.publisher.PPublisher
  (agent-buffer [_]
    buffer)

  (publish-delay [_]
    10)

  (publish [_ *buffer]
    (let [messages     (map second (rb/items *buffer))
          transform-fn (or (:transform config) identity)
          messages     (transform-fn messages)]
      (when (seq messages)
        (swap! (:out config)
               concat
               messages)))
    (rb/clear *buffer)))

(defn test-publisher [config]
  (MemoryPublisher. config (rb/agent-buffer 100)))

(defn wait-until
  "it waits for test* (thunk) to return true or for `or-at-most` millis to have passed."
  [test* or-at-most]
  (let [start (System/currentTimeMillis)]
    (loop []
      (when-not (or (test*)
                    (> (- (System/currentTimeMillis) start) or-at-most))
        (Thread/sleep (long (/ or-at-most 5)))
        (recur)))))



(defmacro with-test-publisher
  [config & body]
  `(let [cfg#    (merge {:rounds 1} ~config)
         outbox# (atom [])
         flush#  (atom 0)
         w#      (add-watch outbox# :flush
                            (fn [_# _# o# n#]
                              (when (< (count o#) (count n#))
                                (swap! flush# inc))))
         gbc#    @com.brunobonacci.mulog.core/global-context
         _#      (reset! com.brunobonacci.mulog.core/global-context {})]

     (logger/setup! (assoc ~config :publishers {:memory {:type         :custom
                                                         :fqn-function test-publisher
                                                         :out          outbox#}}))

     ~@body

     (reset! com.brunobonacci.mulog.core/global-context gbc#)
     ;; wait for the publisher to deliver the events
     (wait-until
       (fn [] (> @flush# 0))
       (* (inc (:rounds cfg#))
          uc/PUBLISH-INTERVAL))
     ;; stop the publisher
     (logger/stop!)
     @outbox#))

(comment
  (with-test-publisher
    {:min-level :debug
     :round 3}
    (logger/log :info :test "abd"))

  )
