(ns br.dev.yuhri.config.interface-test
  (:require [clojure.test :as t]
            [br.dev.yuhri.config.interface :as config]
            [matcher-combinators.test]
            [clojure.string :as string]))

(t/deftest create-config
  (t/testing "getting non existent values"
    (t/is (match? {}
                  (config/create {:non-existent-env  {:env "DOES_NOT_EXISTS"}
                                  :non-existing-prop {:prop "some.random.prop"}
                                  :both-non-existent {:env  "TEST123_asdf"
                                                      :prop "something.else"}}))))
  (t/testing "getting config"
    (t/is (match? {:my-env  (System/getenv "PWD")
                   :my-prop (System/getProperty "java.vm.version")
                   :both    (System/getProperty "java.version")
                   :both2   (System/getenv "PWD")
                   :both3 (System/getProperty "java.version")}
                  (config/create {:my-env  {:env "PWD"}
                                  :my-prop {:prop "java.vm.version"}
                                  :both    {:env  "PWD"
                                            :prop "java.version"}
                                  :both2   {:env  "PWD"
                                            :prop "non-existent"}
                                  :both3 {:env  "non-existent"
                                          :prop "java.version"}}))))
  (t/testing "default values"
    (t/is (match? {:env1  "default-env"
                   :prop1 "default-prop1"
                   :both1 "default-prop2"
                   :prop2 (System/getProperty "java.vm.version")
                   :env2  (System/getenv "PWD")}
                  (config/create {:env1  {:env     "DOES_NOT_EXISTS"
                                          :default "default-env"}
                                  :env2  {:env     "PWD"
                                          :default "default-env"}
                                  :prop1 {:prop    "some.random.prop"
                                          :default "default-prop1"}
                                  :prop2 {:prop "java.vm.version"}
                                  :both1 {:env     (str (random-uuid))
                                          :prop    "something.else"
                                          :default "default-prop2"}}))))
  (t/testing "custom parsing"
    (let [env-parse-fn  #(-> %
                             (string/split #"\/")
                             last
                             keyword)
          prop-parse-fn string/upper-case]
      (t/is (match? {:my-env             (env-parse-fn (System/getenv "PWD"))
                     :my-prop            (prop-parse-fn (System/getProperty "os.arch"))
                     :prop-high-priority (prop-parse-fn (System/getProperty "os.arch"))}
                    (config/create {:my-env             {:env      "PWD"
                                                         :parse-fn env-parse-fn}
                                    :my-prop            {:prop     "os.arch"
                                                         :parse-fn prop-parse-fn}
                                    :prop-high-priority {:env      "PWD"
                                                         :prop     "os.arch"
                                                         :parse-fn prop-parse-fn}}))))))
