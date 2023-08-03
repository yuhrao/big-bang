(ns br.dev.yuhri.logger.core-test
  (:require [clojure.test :as t]
            [matcher-combinators.test]
            [matcher-combinators.matchers :as matchers]
            [br.dev.yuhri.logger.test-tooling :as tooling]
            [br.dev.yuhri.logger.core :as logger]))

(def all-levels [:debug :info :warn :error])

(t/deftest log-levels
  (let [test-id (random-uuid)]
    (logger/setup! {:publishers {:memory (tooling/memory-publisher test-id)}
                    :min-level  :debug})
    (t/testing "generic log function"
      (t/testing "without data"
        (let [message    "custom-message"
              event-name :generic-logger]
          (doseq [level all-levels]
            (logger/log level event-name message))

          (t/is (tooling/wait-for-logs test-id 4 1000))

          (let [produced-messages (tooling/retrieve-messages test-id)]
            (t/is (match? (set all-levels)
                          (->> produced-messages
                               (map :level)
                               set)))
            (t/is (every? (partial = message)
                          (->> produced-messages
                               (map :message))))
            (t/is (every? (partial = event-name)
                          (->> produced-messages
                               (map :mulog/event-name))))))
        (tooling/clear-messages test-id))
      (t/testing "without data"
        (let [message    "custom-message"
              event-name :generic-logger
              log-data   {:my-custom "data"}]
          (doseq [level all-levels]
            (logger/log level event-name message log-data))

          (t/is (tooling/wait-for-logs test-id 4 1000))

          (let [produced-messages (tooling/retrieve-messages test-id)]
            (t/is (match? (set all-levels)
                          (->> produced-messages
                               (map :level)
                               set)))
            (t/is (every? (partial = message)
                          (->> produced-messages
                               (map :message))))
            (t/is (every? (partial = event-name)
                          (->> produced-messages
                               (map :mulog/event-name))))
            (t/is (every? (partial = log-data)
                          (->> produced-messages
                               (map :data))))))
        (tooling/clear-messages test-id)))

    (t/testing "debug log"
      (t/testing "without data"
        (let [message    "custom-message"
              event-name :generic-logger]
          (logger/debug event-name message)

          (t/is (tooling/wait-for-logs test-id 1 1000))

          (let [produced-messages (tooling/retrieve-messages test-id)]
            (t/is (match? [:debug]
                          (->> produced-messages
                               (map :level))))
            (t/is (match? [message]
                          (->> produced-messages
                               (map :message))))
            (t/is (match? [event-name]
                          (->> produced-messages
                               (map :mulog/event-name))))))
        (tooling/clear-messages test-id))
      (t/testing "with data"
        (let [message    "custom-message"
              event-name :generic-logger
              log-data   {:my-custom "data"}]
          (logger/debug event-name message log-data)

          (t/is (tooling/wait-for-logs test-id 1 1000))

          (let [produced-messages (tooling/retrieve-messages test-id)]
            (t/is (match? [:debug]
                          (->> produced-messages
                               (map :level))))
            (t/is (match? [message]
                          (->> produced-messages
                               (map :message))))
            (t/is (match? [event-name]
                          (->> produced-messages
                               (map :mulog/event-name))))
            (t/is (match? [log-data]
                          (->> produced-messages
                               (map :data))))))
        (tooling/clear-messages test-id)))

    (t/testing "info log"
      (t/testing "without data"
        (let [message    "custom-message"
              event-name :generic-logger]
          (logger/info event-name message)

          (t/is (tooling/wait-for-logs test-id 1 1000))

          (let [produced-messages (tooling/retrieve-messages test-id)]
            (t/is (match? [:info]
                          (->> produced-messages
                               (map :level))))
            (t/is (match? [message]
                          (->> produced-messages
                               (map :message))))
            (t/is (match? [event-name]
                          (->> produced-messages
                               (map :mulog/event-name))))))
        (tooling/clear-messages test-id))
      (t/testing "with data"
        (let [message    "custom-message"
              event-name :generic-logger
              log-data   {:my-custom "data"}]
          (logger/info event-name message log-data)
          (t/is (tooling/wait-for-logs test-id 1 1000))
          (let [produced-messages (tooling/retrieve-messages test-id)]
            (t/is (match? [:info]
                          (->> produced-messages
                               (map :level))))
            (t/is (match? [message]
                          (->> produced-messages
                               (map :message))))
            (t/is (match? [event-name]
                          (->> produced-messages
                               (map :mulog/event-name))))
            (t/is (match? [log-data]
                          (->> produced-messages
                               (map :data))))))
        (tooling/clear-messages test-id)))

    (t/testing "warn log"
      (t/testing "without data"
        (let [message    "custom-message"
              event-name :generic-logger]
          (logger/warn event-name message)

          (t/is (tooling/wait-for-logs test-id 1 1000))

          (let [produced-messages (tooling/retrieve-messages test-id)]
            (t/is (match? [:warn]
                          (->> produced-messages
                               (map :level))))
            (t/is (match? [message]
                          (->> produced-messages
                               (map :message))))
            (t/is (match? [event-name]
                          (->> produced-messages
                               (map :mulog/event-name))))))
        (tooling/clear-messages test-id))
      (t/testing "with data"
        (let [message    "custom-message"
              event-name :generic-logger
              log-data   {:my-custom "data"}]
          (logger/warn event-name message log-data)

          (t/is (tooling/wait-for-logs test-id 1 1000))

          (let [produced-messages (tooling/retrieve-messages test-id)]
            (t/is (match? [:warn]
                          (->> produced-messages
                               (map :level))))
            (t/is (match? [message]
                          (->> produced-messages
                               (map :message))))
            (t/is (match? [event-name]
                          (->> produced-messages
                               (map :mulog/event-name))))
            (t/is (match? [log-data]
                          (->> produced-messages
                               (map :data))))))
        (tooling/clear-messages test-id)))
    (t/testing "error log"
      (t/testing "without data"
        (let [message    "custom-message"
              event-name :generic-logger]
          (logger/error event-name message)

          (t/is (tooling/wait-for-logs test-id 1 1000))

          (let [produced-messages (tooling/retrieve-messages test-id)]
            (t/is (match? [:error]
                          (->> produced-messages
                               (map :level))))
            (t/is (match? [message]
                          (->> produced-messages
                               (map :message))))
            (t/is (match? [event-name]
                          (->> produced-messages
                               (map :mulog/event-name))))))
        (tooling/clear-messages test-id))
      (t/testing "with data"
        (let [message    "custom-message"
              event-name :generic-logger
              log-data   {:my-custom "data"}]
          (logger/error event-name message log-data)

          (t/is (tooling/wait-for-logs test-id 1 1000))

          (let [produced-messages (tooling/retrieve-messages test-id)]
            (t/is (match? [:error]
                          (->> produced-messages
                               (map :level))))
            (t/is (match? [message]
                          (->> produced-messages
                               (map :message))))
            (t/is (match? [event-name]
                          (->> produced-messages
                               (map :mulog/event-name))))
            (t/is (match? [log-data]
                          (->> produced-messages
                               (map :data))))))
        (tooling/clear-messages test-id))))
  (tooling/remove-messages)
  (logger/stop!))

(t/deftest logger-min-level
  (let [test-id (random-uuid)]
    (t/testing "use default min-level when none is provided by config"
      (logger/setup! {:publishers {:memory (tooling/memory-publisher test-id)}})
      (let [message    "custom-message"
            event-name :generic-logger]
        (doseq [level all-levels]
          (logger/log level event-name message))

        (t/is (tooling/wait-for-logs test-id 3 1000))

        (let [produced-messages (tooling/retrieve-messages test-id)]
          (t/is (match? (disj (set all-levels) :debug)
                        (->> produced-messages
                             (map :level)
                             set)))))
      (tooling/clear-messages test-id)
      (logger/stop!))
    (t/testing "min-level defined"
      (doseq [{:keys [min-level
                      forbidden-levels
                      expected-count]}
              [{:min-level        :debug
                :forbidden-levels []
                :expected-count   4}
               {:min-level        :info
                :forbidden-levels [:debug]
                :expected-count   3}
               {:min-level        :warn
                :forbidden-levels [:debug :info]
                :expected-count   2}
               {:min-level        :error
                :forbidden-levels [:debug :info :warn]
                :expected-count   1}]]

        (logger/setup! {:publishers {:memory (tooling/memory-publisher test-id)}
                        :min-level  min-level})
        (let [message    "custom-message"
              event-name :generic-logger]
          (doseq [level all-levels]
            (logger/log level event-name message))

          (t/is (tooling/wait-for-logs test-id expected-count 1000))

          (let [produced-messages (tooling/retrieve-messages test-id)]
            (t/is (match? (apply (partial disj (set all-levels)) forbidden-levels)
                          (->> produced-messages
                               (map :level)
                               set)))))
        (tooling/clear-messages test-id)
        (logger/stop!))))
  (tooling/remove-messages)
  (logger/stop!))

(t/deftest context
  (let [test-id (random-uuid)]
    (t/testing "tracer with map syntax"
      (logger/setup! {:publishers {:memory (tooling/memory-publisher test-id)}})
      (let [context {:custom "context"}
            data    {:test 1}]
        (logger/with-context
          {:custom "context"}
          (logger/info :event "message"))

        (t/is (tooling/wait-for-logs test-id 1 1000))

        (let [produced-messages (tooling/retrieve-messages test-id)]
          (t/is (match? (matchers/any-of
                          [(merge
                             context)])
                        produced-messages))))
      (logger/stop!))))

(t/deftest trace
  (let [test-id (random-uuid)]
    (t/testing "tracer with vector syntax"
      (logger/setup! {:publishers {:memory (tooling/memory-publisher test-id)}})
      (let [context {:custom "context"}
            data    {:test 1}]
        (logger/trace ::my-event
                      (vec (mapcat identity context))
                      data)

        (t/is (tooling/wait-for-logs test-id 1 1000))

        (let [produced-messages (tooling/retrieve-messages test-id)]
          (t/is (match? (matchers/any-of
                          [(merge
                             context)])
                        produced-messages))
          (t/is (match? nil (-> produced-messages
                                first
                                :data)))))
      (tooling/clear-messages test-id))

    (t/testing "tracer with map syntax"
      (logger/setup! {:publishers {:memory (tooling/memory-publisher test-id)}})
      (let [context {:custom "context"}
            data    {:test 1}]
        (logger/trace ::my-event
                      {:pairs   (vec (mapcat identity context))
                       :capture identity}
                      data)
        (t/is (tooling/wait-for-logs test-id 1 1000))
        (let [produced-messages (tooling/retrieve-messages test-id)]
          (t/is (match? (matchers/any-of
                          [(merge
                             context
                             {:data data})])
                        produced-messages))))
      (logger/stop!))))
