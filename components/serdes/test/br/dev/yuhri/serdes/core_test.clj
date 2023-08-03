(ns br.dev.yuhri.serdes.core-test
  (:require [clojure.test :as t]
            [matcher-combinators.test]
            [camel-snake-kebab.core :as csk]
            [camel-snake-kebab.extras :as cske]
            [br.dev.yuhri.serdes.core :as serdes]))

(t/deftest json
           (let [payload          {:nocase     "nocase"
                                   :kebab-case "kebab-case"
                                   :camelCase  "camelCase"
                                   :snake_case "snake_case"}
                 expected-payload (cske/transform-keys csk/->kebab-case-keyword payload)]
             (t/testing "json string"
                        (t/is (match? expected-payload (-> payload
                                                           serdes/clj->json
                                                           serdes/json->clj))))
             (t/testing "json stream"
                        (t/is (match? expected-payload (-> payload
                                                           serdes/clj->json-stream
                                                           serdes/json->clj))))))

(t/deftest edn
           (let [payload {:nocase     "nocase"
                          :kebab-case "kebab-case"
                          :camelCase  "camelCase"
                          :snake_case "snake_case"}]
             (t/testing "edn string"
                        (t/is (match? payload (-> payload
                                                  serdes/clj->edn
                                                  serdes/edn->clj))))
             (t/testing "edn stream"
                        (t/is (match? payload (-> payload
                                                  serdes/clj->edn-stream
                                                  serdes/edn->clj))))))
