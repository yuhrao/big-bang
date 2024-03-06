(ns yuhrao.http-client.core-test
  (:require [yuhrao.http-client.core :as http]
            [clojure.test :as t]
            [matcher-combinators.matchers :as matcher]
            [matcher-combinators.test]
            [matcher-combinators.matchers :as matchers])
  (:import (java.net URI)))

(t/deftest ^:integration request
  (let [cli (http/client {:base-url "https://postman-echo.com"})]
   (t/testing "requests without body"
     (let [req {:method :get
                :headers {:x-custom-header "value"}
                :path "/get"}]
       (t/is (match?
               {:request {:headers {"X-Custom-Header" "value"}
                          :uri       (URI/create "https://postman-echo.com/get")
                          :method :get}
                :status  200
                :success? true?
                :headers map?
                :body {:args    map?
                       :headers {:x-custom-header any?}
                       :url     "https://postman-echo.com/get"}}
               (http/request cli req)))))
   (t/testing "requests with body"
     (let [req-body {:test true}
           req {:method  :post
                :headers {:x-custom-header "value"
                          :accept "application/json"
                          :content-type "application/json"}
                :save-request? true
                :body req-body
                :path "/post"}]
       (t/is (match?
               {:request {:headers {"X-Custom-Header" "value"
                                    "Accept" "application/json"
                                    "Content-Type" "application/json"}
                          :uri       (URI/create "https://postman-echo.com/post")
                          :method    :post}
                :status  200
                :success? true?
                :headers map?
                :body {:args    map?
                       :headers {:x-custom-header any?}
                       :json    (matcher/equals req-body)
                       :data    (matcher/equals req-body)
                       :url     "https://postman-echo.com/post"}}
               (http/request cli req)))))))
