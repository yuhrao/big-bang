(ns br.dev.yuhri.data-cloak.core.string-test
  (:require [clojure.string :as string]
            [clojure.test :as t]
            [matcher-combinators.test]
            [br.dev.yuhri.data-cloak.core.string :as dc.string]))

(t/deftest offsets
  (t/testing "asymmetric offset"
    (t/is (match? "test***asymmetric"
                  (dc.string/offset "testabcasymmetric"
                                    {:start (count "test")
                                     :end   (count "asymmetric")}))))
  (t/testing "symmetric offset"
    (t/is (match? "test***test"
                  (dc.string/symmetric-offset
                    "testabctest"
                    (count "test")))))
  (t/testing "obscure everything"
    (let [sample "aslgaslkdfsdf"]
      (t/is (match? (string/replace sample #"\w" "*")
                    (dc.string/all sample))))))

(t/deftest email
  (t/testing "short email"
    (t/is (match? "b**@gmail.com"
                  (dc.string/email "bob@gmail.com")))
    (t/is (match? "s****@gmail.com"
                  (dc.string/email "small@gmail.com"))))
  (t/testing "larger email"
    (t/testing "short email"
      (t/is (match? "me**um@gmail.com"
                    (dc.string/email "medium@gmail.com")))
      (t/is (match? "re**************il@gmail.com"
                    (dc.string/email "really.large-email@gmail.com"))))))

(t/deftest phone-number
  (t/testing "valid phone number"
    (t/is (match? "55119****1234"
                  (dc.string/phone "+55 (11) 91234-1234")))
    (t/is (match? "119****1234"
                  (dc.string/phone "(11) 91234-1234")))
    (t/is (match? "*****1234"
                  (dc.string/phone "91234-1234"))))
  (t/testing "invalid phone number"
    (t/is (match? "55?11*****1234"
                  (dc.string/phone "+55? (11) 91234-1234")))
    (t/is (match? "%$T*****1234"
                  (dc.string/phone "%$T 91234-1234")))
    (t/is (match? "******1234"
                  (dc.string/phone "91234&1234")))))

(t/deftest empty-values
  (t/testing "empty strings"
    (let [sample ""]
     (t/is (match? sample (dc.string/offset sample {:start 3
                                            :end   3})))
     (t/is (match? sample (dc.string/symmetric-offset sample 3)))
     (t/is (match? sample
                   (dc.string/all sample)))
     (t/is (match? sample
                   (dc.string/email sample)))
     (t/is (match? sample
                   (dc.string/phone sample)))))
  (t/testing "nil value"
    (let [sample nil]
      (t/is (match? sample (dc.string/offset sample {:start 3
                                                     :end   3})))
      (t/is (match? sample (dc.string/symmetric-offset sample 3)))
      (t/is (match? sample
                    (dc.string/all sample)))
      (t/is (match? sample
                    (dc.string/email sample)))
      (t/is (match? sample
                    (dc.string/phone sample))))))
