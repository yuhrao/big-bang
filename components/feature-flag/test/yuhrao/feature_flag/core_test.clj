(ns br.dev.yuhri.feature-flag.core-test
  (:require [br.dev.yuhri.feature-flag.core :as ff]
            [camel-snake-kebab.core :as csk]
            [clojure.data.json :as json]
            [clojure.test :as t]
            [matcher-combinators.test])
  (:import (io.getunleash FakeUnleash Variant)
           (io.getunleash.variant Payload)))

(t/deftest enabled?
  (let [{:keys [^FakeUnleash instance]
         :as   client} (ff/client {:type :fake})
        flag-name    "fake-flag"
        fake-user-id "fake-id"]
    (t/is (false? (ff/enabled? client flag-name)))
    (.enable instance (into-array [flag-name]))
    (t/is (true? (ff/enabled? client flag-name)))

    (.resetAll instance)

    (t/is (false? (ff/enabled? client flag-name {:user-id     fake-user-id
                                                 :app-name    "test-app"
                                                 :session-id  "abd"
                                                 :addditional "prop"})))
    (.enable instance (into-array [flag-name]))
    (t/is (true? (ff/enabled? client flag-name {:user-id     fake-user-id
                                                :app-name    "test-app"
                                                :session-id  "abd"
                                                :addditional "prop"})))))

(t/deftest variants
  (let [{:keys [^FakeUnleash instance]
         :as   client} (ff/client {:type :fake})
        flag-name    "fake-flag"
        variant-name "my-variant"
        fake-user-id "fake-id"]

    (t/testing "string variant"
      (let [variant-type  "string"
            variant-value "some value"]

        (.enable instance (into-array [flag-name]))
        (.setVariant instance flag-name (Variant. variant-name (Payload. variant-type, variant-value) true))
        (t/is (true? (ff/enabled? client flag-name)))
        (t/is (match?
                {:enabled? true?
                 :name     (csk/->kebab-case-keyword variant-name)
                 :payload  variant-value}
                (ff/variant client flag-name)))

        (.resetAll instance)

        (t/is (false? (ff/enabled? client flag-name {:user-id     fake-user-id
                                                     :app-name    "test-app"
                                                     :session-id  "abd"
                                                     :addditional "prop"})))

        (.enable instance (into-array [flag-name]))
        (.setVariant instance flag-name (Variant. variant-name (Payload. variant-type, variant-value) true))
        (t/is (true? (ff/enabled? client flag-name {:user-id     fake-user-id
                                                    :app-name    "test-app"
                                                    :session-id  "abd"
                                                    :addditional "prop"})))
        (t/is (match?
                {:enabled? true?
                 :name     (csk/->kebab-case-keyword variant-name)
                 :payload  variant-value}
                (ff/variant client flag-name {:user-id     fake-user-id
                                              :app-name    "test-app"
                                              :session-id  "abd"
                                              :addditional "prop"})))))

    (t/testing "json variant"
      (let [variant-value     {:some       "value"
                               :some-other "different value"}
            variant-raw-value (json/write-str variant-value :key-fn csk/->camelCaseString)]

        (.enable instance (into-array [flag-name]))
        (.setVariant instance flag-name (Variant. variant-name (Payload. "json", variant-raw-value) true))
        (t/is (true? (ff/enabled? client flag-name)))
        (t/is (match?
                {:enabled? true?
                 :name     (csk/->kebab-case-keyword variant-name)
                 :payload  variant-value}
                (ff/variant client flag-name)))

        (.resetAll instance)

        (t/is (false? (ff/enabled? client flag-name {:user-id     fake-user-id
                                                     :app-name    "test-app"
                                                     :session-id  "abd"
                                                     :addditional "prop"})))

        (.enable instance (into-array [flag-name]))
        (.setVariant instance flag-name (Variant. variant-name (Payload. "json", variant-raw-value) true))
        (t/is (true? (ff/enabled? client flag-name {:user-id     fake-user-id
                                                    :app-name    "test-app"
                                                    :session-id  "abd"
                                                    :addditional "prop"})))
        (t/is (match?
                {:enabled? true?
                 :name     (csk/->kebab-case-keyword variant-name)
                 :payload  variant-value}
                (ff/variant client flag-name {:user-id     fake-user-id
                                              :app-name    "test-app"
                                              :session-id  "abd"
                                              :addditional "prop"})))))))
