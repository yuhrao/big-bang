(ns yuhrao.serdes.core.content-negotiation-test
  (:require [yuhrao.serdes.core.content-negotiation :as content-negotiation]
            [camel-snake-kebab.core :as csk]
            [camel-snake-kebab.extras :as cske]
            [clojure.test :as t]
            [matcher-combinators.test]))

(t/deftest json
  (let [payload          {:nocase     "nocase"
                          :kebab-case "kebab-case"
                          :camelCase  "camelCase"
                          :snake_case "snake_case"}
        expected-payload (cske/transform-keys csk/->kebab-case-keyword payload)]
    (t/testing "json"
      (t/is (match? expected-payload (->> payload
                                          (content-negotiation/encode "application/json")
                                          (content-negotiation/decode "application/json"))))
      (t/is (match? {} (->> {}
                            (content-negotiation/encode "application/json")
                            (content-negotiation/decode "application/json")))))

    (t/testing "edn"
      (t/is (match? payload (->> payload
                                 (content-negotiation/encode "application/edn")
                                 (content-negotiation/decode "application/edn"))))
      (t/is (match? {} (->> {}
                            (content-negotiation/encode "application/edn")
                            (content-negotiation/decode "application/edn")))))))
