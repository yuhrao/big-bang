(ns br.dev.yuhri.data-cloak.core.map-test
  (:require [br.dev.yuhri.data-cloak.core.string :as dc.string]
            [clojure.test :as t]
            [br.dev.yuhri.data-cloak.core.map :as dc.map]
            [matcher-combinators.test]
            [matcher-combinators.matchers :as matcher]))

(t/deftest obscure
  (t/is
    (match?
      (matcher/equals
        {:phone  "55119****1234"
         :email  "ra*******il@gmail.com"
         :random "value"})
      (dc.map/obscure {:phone dc.string/phone
                       :email dc.string/email}
                      {:phone  "+55 1191234-1234"
                       :email  "random-mail@gmail.com"
                       :random "value"})))
  (t/is
    (match?
      (matcher/equals
        {:email  "ra*******il@gmail.com"
         :random "value"})
      (dc.map/obscure {:phone dc.string/phone
                       :email dc.string/email}
                      {:email  "random-mail@gmail.com"
                       :random "value"}))
    "should contain only fields present in log's data")
  (t/is
    (match?
      (matcher/equals
        {:phone  ""
         :email  "ra*******il@gmail.com"
         :random "value"})
      (dc.map/obscure {:phone dc.string/phone
                       :email dc.string/email}
                      {:phone  ""
                       :email  "random-mail@gmail.com"
                       :random "value"}))
    "should keep empty string values")
  (t/is
    (match?
      (matcher/equals
        {:phone  nil?
         :email  "ra*******il@gmail.com"
         :random "value"})
      (dc.map/obscure {:phone dc.string/phone
                       :email dc.string/email}
                      {:phone  nil
                       :email  "random-mail@gmail.com"
                       :random "value"}))
    "should keep nil values"))

(t/deftest obscurer
  (let [obscurer (dc.map/obscurer {:phone dc.string/phone
                                   :email dc.string/email})]
    (t/is
      (match?
        (matcher/equals
          {:phone  "55119****1234"
           :email  "ra*******il@gmail.com"
           :random "value"})
        (obscurer
          {:phone  "+55 1191234-1234"
           :email  "random-mail@gmail.com"
           :random "value"})))
    (t/is
      (match?
        (matcher/equals
          {:email  "ra*******il@gmail.com"
           :random "value"})
        (obscurer
          {:email  "random-mail@gmail.com"
           :random "value"}))
      "should contain only fields present in log's data")
    (t/is
      (match?
        (matcher/equals
          {:phone  ""
           :email  "ra*******il@gmail.com"
           :random "value"})
        (obscurer
          {:phone  ""
           :email  "random-mail@gmail.com"
           :random "value"}))
      "should keep empty string values")
    (t/is
      (match?
        (matcher/equals
          {:phone  nil?
           :email  "ra*******il@gmail.com"
           :random "value"})
        (obscurer
          {:phone  nil
           :email  "random-mail@gmail.com"
           :random "value"}))
      "should keep nil values")))
