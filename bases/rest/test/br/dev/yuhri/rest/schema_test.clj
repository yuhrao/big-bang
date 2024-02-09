(ns br.dev.yuhri.rest.schema-test
  (:require
   [br.dev.yuhri.rest.schema :as sut]
   [clojure.test :as t]
   [malli.core :as malli]
   [malli.transform :as m.transform]
   [matcher-combinators.test]
   [tick.core :as tick]))

(t/deftest uuid
  (t/testing "validation"
    (t/is (match? true? (malli/validate sut/UUID (random-uuid))))
    (t/is (match? false? (malli/validate sut/UUID "invalid-uuid"))))
  (t/testing "coercion"
    (let [sample (random-uuid)]
      (t/is (match? sample
                    (malli/decode sut/UUID
                                  (str sample)
                                  (m.transform/string-transformer))))
      (t/is (match? nil?
                    (malli/decode sut/UUID
                                  "invalid"
                                  (m.transform/string-transformer))))
      (t/is (match? (str sample)
                    (malli/encode sut/UUID
                                  sample
                                  (m.transform/string-transformer)))))))

(t/deftest datetime
  (t/testing "validation"
    (t/is (match? true? (malli/validate sut/DateTime (tick/date-time))))
    (t/is (match? false? (malli/validate sut/DateTime "invalid-date-time"))))
  (t/testing "coercion"
    (let [sample (tick/date-time)]
      (t/is (match? sample
                    (malli/decode sut/DateTime
                                  (tick/format sample)
                                  (m.transform/string-transformer))))
      (t/is (match? (tick/format sample)
                    (malli/encode sut/DateTime
                                  sample
                                  (m.transform/string-transformer)))))))
