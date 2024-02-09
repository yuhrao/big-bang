(ns yuhrao.serdes.core.json-test
  (:require [yuhrao.serdes.core.json :as json]
            [camel-snake-kebab.core :as csk]
            [camel-snake-kebab.extras :as cske]
            [clojure.test :as t]
            [matcher-combinators.test]))

(t/deftest json
  (let [payload          {:nocase     "nocase"
                          :kebab-case "kebab-case"
                          :camelCase  "camelCase"
                          :snake_case "snake_case"
                          :array      [1 "two"]}
        expected-payload (cske/transform-keys csk/->kebab-case-keyword payload)]
    (t/testing "json string"
      (t/is (match? expected-payload (-> payload
                                         json/clj->json
                                         json/json->clj)))
      (t/is (match? {} (-> {}
                           json/clj->json
                           json/json->clj)))
      (t/is (match?
              (cske/transform-keys name payload)
              (-> payload
                 json/clj->json
                 (#(json/json->clj % {:key-fn identity}))))))
    (t/testing "json stream"
      (t/is (match? expected-payload (-> payload
                                         json/clj->json-stream
                                         json/json->clj)))
      (t/is (match? {} (-> {}
                           json/clj->json-stream
                           json/json->clj)))
      (t/is (match?
              (cske/transform-keys name payload)
              (-> payload
                  json/clj->json-stream
                  (#(json/json->clj % {:key-fn identity}))))))))
