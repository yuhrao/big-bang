(ns yuhrao.testing.core
  (:require [polylith-kaocha.kaocha-wrapper.config :as config]))

(defn test-with-focus-meta [id metadata]
  [{:kaocha.testable/type :kaocha.type/clojure.test,
    :kaocha.testable/id id,
    :kaocha/ns-patterns ["-test$"],
    :kaocha/source-paths ["src"],
    :kaocha/test-paths ["test"],
    :kaocha.filter/focus-meta metadata}])

(defn test-with-skip-meta [id metadata]
  [{:kaocha.testable/type :kaocha.type/clojure.test,
    :kaocha.testable/id id,
    :kaocha/ns-patterns ["-test$"],
    :kaocha/source-paths ["src"],
    :kaocha/test-paths ["test"],
    :kaocha.filter/skip-meta metadata}])

(def tests-key :kaocha/tests)

(defn unit-post-load-config [config opts]
  (-> (config/default-post-load-config config opts)
      (assoc tests-key (test-with-skip-meta :unit [:integration]))))

(defn integration-post-load-config [config opts]
  (-> (config/default-post-load-config config opts)
      (assoc tests-key (test-with-focus-meta :integration [:integration]))))
