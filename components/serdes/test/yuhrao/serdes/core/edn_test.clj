(ns yuhrao.serdes.core.edn-test
  (:require [yuhrao.serdes.core.edn :as edn]
            [clojure.test :as t]
            [matcher-combinators.test]))

(t/deftest edn
  (let [payload {:nocase     "nocase"
                 :kebab-case "kebab-case"
                 :camelCase  "camelCase"
                 :snake_case "snake_case"}]
    (t/testing "edn string"
      (t/is (match? payload (-> payload
                                edn/clj->edn
                                edn/edn->clj)))
      (t/is (match? {} (-> {}
                           edn/clj->edn
                           edn/edn->clj))))
    (t/testing "edn stream"
      (t/is (match? payload (-> payload
                                edn/clj->edn-stream
                                edn/edn->clj)))
      (t/is (match? {} (-> {}
                           edn/clj->edn-stream
                           edn/edn->clj))))))
