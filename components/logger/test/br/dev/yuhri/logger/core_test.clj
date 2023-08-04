(ns br.dev.yuhri.logger.core-test
  (:require [clojure.test :as t]
            [matcher-combinators.test]
            [matcher-combinators.matchers :as matcher]
            [br.dev.yuhri.logger.test-tooling :as tooling]
            [br.dev.yuhri.logger.core :as logger]))

(def all-levels [:debug :info :warn :error])

(t/deftest poc
  (let [message    "custom-message"
        event-name :generic-logger
        out        (tooling/with-test-publisher
                     {:min-level :debug
                      :round     3}
                     (doseq [level all-levels]
                       (logger/log level event-name message)))]

    (let [produced-messages out]
      (clojure.pprint/pprint produced-messages)
      (t/is (match? (set all-levels)
                    (->> produced-messages
                         (map :level)
                         set)))
      (t/is (match? (matcher/seq-of {:message          message
                                     :mulog/event-name event-name})
                    produced-messages)))))

(t/deftest log-levels
  (t/testing "generic log function"
    (t/testing "without data"
      (let [message    "custom-message"
            event-name :generic-logger
            out        (tooling/with-test-publisher
                         {:min-level :debug
                          :round     3}
                         (doseq [level all-levels]
                           (logger/log level event-name message)))]

        (clojure.pprint/pprint out)
        (t/is (match? (set all-levels)
                      (->> out
                           (map :level)
                           set)))
        (t/is (match? (matcher/seq-of {:message          message
                                       :mulog/event-name event-name})
                      out))))))

(t/deftest logger-min-level
  (t/testing "use default min-level when none is provided by config"
    (let [message    "custom-message"
          event-name :generic-logger
          out        (tooling/with-test-publisher
                       {:round     3}
                       (doseq [level all-levels]
                         (logger/log level event-name message)))]

      (t/is (match? (disj (set all-levels) :debug)
                    (->> out
                         (map :level)
                         set)))))

  (t/testing "min-level defined"
    (doseq [{:keys [min-level
                    forbidden-levels]}
            [{:min-level        :debug
              :forbidden-levels []}
             {:min-level        :info
              :forbidden-levels [:debug]}
             {:min-level        :warn
              :forbidden-levels [:debug :info]}
             {:min-level        :error
              :forbidden-levels [:debug :info :warn]
              :expected-count   1}]]

      (let [message    "custom-message"
            event-name :generic-logger
            out        (tooling/with-test-publisher
                         {:min-level min-level
                          :round     3}
                         (doseq [level all-levels]
                           (logger/log level event-name message)))]

        (t/is (match? (apply (partial disj (set all-levels)) forbidden-levels)
                      (->> out
                           (map :level)
                           set)))))))

(t/deftest context
  (t/testing "tracer with map syntax"
    (let [context {:custom "context"}
          out     (tooling/with-test-publisher
                    {}
                    (logger/with-context
                      {:custom "context"}
                      (logger/info :event "message")))]

      (t/is (match? (matcher/any-of
                      [(merge
                         context)])
                    out)))))

(t/deftest trace
  (t/testing "tracer with vector syntax"
    (let [context {:custom "context"}
          data    {:test 1}
          out     (tooling/with-test-publisher
                    {}
                    (logger/trace ::my-event
                                  (vec (mapcat identity context))
                                  data))]

      (t/is (match? (matcher/seq-of
                      context)
                    out))
      (t/is (match? nil (-> out
                            first
                            :data)))))

  (t/testing "tracer with map syntax"
    (let [
          context {:custom "context"}
          data    {:test 1}
          out     (tooling/with-test-publisher
                    {}
                    (logger/trace
                      ::my-event
                      {:pairs   (vec (mapcat identity context))
                       :capture identity}
                      data))]

      (t/is (match? (matcher/seq-of
                      (merge
                        context
                        {:data data}))
                    out)))))
