(ns br.dev.yuhri.feature-flag.unleash.client-test
  (:require [clojure.test :as t]
            [br.dev.yuhri.feature-flag.unleash.client :as unleash.client])
  (:import (io.getunleash DefaultUnleash FakeUnleash)))

(t/deftest create-client
  (t/testing "default client"
    (let [{:keys [instance]} (unleash.client/unleash {:api-key "fake"
                                                      :app-name "test"
                                                      :unleash-api "https://google.com"})]
      (t/is (instance? DefaultUnleash instance))))
  (t/testing "fake client"
    (let [{:keys [instance]} (unleash.client/unleash {:type :fake})]
      (t/is (instance? FakeUnleash instance)))))
