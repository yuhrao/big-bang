(ns br.dev.yuhri.config.core-test
  (:require [clojure.test :as t]
            [br.dev.yuhri.config.core :as config]
            [matcher-combinators.test]
            [clojure.string :as string]))

(defn- random-uuid-str []
  (str (random-uuid)))

(t/deftest create-config
  (t/testing "getting non existent values"
    (t/is (empty? (config/create {:non-existent-env  {:env (random-uuid-str)}
                                  :non-existing-prop {:prop (random-uuid-str)}
                                  :both-non-existent {:env  (random-uuid-str)
                                                      :prop (random-uuid-str)}}))))
  (t/testing "getting config"
    (t/is (match? {:my-env  (System/getenv "PWD")
                   :my-prop (System/getProperty "java.vm.version")
                   :both    (System/getProperty "java.version")
                   :both2   (System/getenv "PWD")
                   :both3   (System/getProperty "java.version")}
                  (config/create {:my-env  {:env "PWD"}
                                  :my-prop {:prop "java.vm.version"}
                                  :both    {:env  "PWD"
                                            :prop "java.version"}
                                  :both2   {:env  "PWD"
                                            :prop (random-uuid-str)}
                                  :both3   {:env  (random-uuid-str)
                                            :prop "java.version"}}))))
  (t/testing "default values"
    (t/is (match? {:env1  "default-env"
                   :env2  (System/getenv "PWD")
                   :prop1 "default-prop1"
                   :prop2 (System/getProperty "java.vm.version")
                   :both1 "default-prop2"
                   :both2 (System/getenv "PWD")
                   :both3 (System/getProperty "java.vm.version")}
                  (config/create {:env1  {:env     (random-uuid-str)
                                          :default "default-env"}
                                  :env2  {:env     "PWD"
                                          :default "default-env"}
                                  :prop1 {:prop    (random-uuid-str)
                                          :default "default-prop1"}
                                  :prop2 {:prop "java.vm.version"}
                                  :both1 {:env     (random-uuid-str)
                                          :prop    (random-uuid-str)
                                          :default "default-prop2"}
                                  :both2 {:env     "PWD"
                                          :prop    (random-uuid-str)
                                          :default "default-prop2"}
                                  :both3 {:env     (random-uuid-str)
                                          :prop    "java.vm.version"
                                          :default "default-prop2"}}))))
  (t/testing "custom parsing"
    (let [env-parse-fn  #(-> %
                             (string/split #"\/")
                             last
                             keyword)
          prop-parse-fn string/upper-case]
      (t/is (match? {:my-env  (env-parse-fn (System/getenv "PWD"))
                     :my-prop (prop-parse-fn (System/getProperty "os.arch"))
                     :both    (prop-parse-fn (System/getProperty "os.arch"))}
                    (config/create {:my-env  {:env      "PWD"
                                              :parse-fn env-parse-fn}
                                    :my-prop {:prop     "os.arch"
                                              :parse-fn prop-parse-fn}
                                    :both    {:env      "PWD"
                                              :prop     "os.arch"
                                              :parse-fn prop-parse-fn}}))))))
