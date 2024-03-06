(ns yuhrao.serdes.core.content-negotiation-test
  (:require [yuhrao.serdes.core.content-negotiation :as content-negotiation]
            [camel-snake-kebab.core :as csk]
            [camel-snake-kebab.extras :as cske]
            [clojure.test :as t]
            [matcher-combinators.test]
            [tick.core :as tick]
            [medley.core :as medley]))

(t/deftest json
  (t/testing "primitives"
    (let [payload          {:nocase     "nocase"
                            :kebab-case "kebab-case"
                            :camelCase  "camelCase"
                            :snake_case "snake_case"}
          expected-payload (cske/transform-keys csk/->kebab-case-keyword payload)]
      (t/is (match? expected-payload (->> payload
                                          (content-negotiation/encode "application/json")
                                          (content-negotiation/decode "application/json"))))
      (t/is (match? {} (->> {}
                            (content-negotiation/encode "application/json")
                            (content-negotiation/decode "application/json"))))))
  (t/testing "nested structures"
    (let [payload          {:vec [1 2 [1 2 "three"]]
                            :map {:s "string"
                                  :n 1
                                  :nested {:s "string"
                                           :n 1
                                           :vec [1 2 [1 2 "three"]]}}}
          expected-payload (cske/transform-keys csk/->kebab-case-keyword payload)]
      (t/is (match? expected-payload (->> payload
                                          (content-negotiation/encode "application/json")
                                          (content-negotiation/decode "application/json"))))))
  (t/testing "tick dates"
    (let [payload          {:date      (tick/date)
                            :date-time (tick/date-time)
                            :duration  (tick/new-duration 1 :days)
                            :instant   (tick/instant)}
          expected-payload (->> payload
                                (medley/map-vals tick/format)
                                (cske/transform-keys csk/->kebab-case-keyword))]
      (t/is (match? expected-payload (->> payload
                                          (content-negotiation/encode "application/json")
                                          (content-negotiation/decode "application/json")))))))

(t/deftest edn
  (t/testing "prmitives"
    (let [payload          {:nocase     "nocase"
                            :kebab-case "kebab-case"
                            :camelCase  "camelCase"
                            :snake_case "snake_case"}]
      (t/is (match? payload (->> payload
                                 (content-negotiation/encode "application/edn")
                                 (content-negotiation/decode "application/edn"))))
      (t/is (match? {} (->> {}
                            (content-negotiation/encode "application/edn")
                            (content-negotiation/decode "application/edn")))))))

(t/deftest plain-text
  (t/testing "prmitives"
    (let [payload          {:nocase     "nocase"
                            :kebab-case "kebab-case"
                            :camelCase  "camelCase"
                            :snake_case "snake_case"}]
    (t/is (match? (str payload) (->> payload
                                     (content-negotiation/encode "plain/text")
                                     (content-negotiation/decode "plain/text"))))
    (t/is (match? "{}" (->> {}
                            (content-negotiation/encode "plain/text")
                            (content-negotiation/decode "plain/text")))))))
