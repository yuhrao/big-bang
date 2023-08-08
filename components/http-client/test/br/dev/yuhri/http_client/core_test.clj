(ns br.dev.yuhri.http-client.core-test
  (:require [br.dev.yuhri.http-client.core :as http]
            [clojure.test :as t]
            [matcher-combinators.matchers :as matcher]
            [matcher-combinators.test]
            [matcher-combinators.matchers :as matchers]
            [muuntaja.core :as mtj]))

(t/deftest ^:integration client
  (let [base-url "https://postman-echo.com"]
    (t/testing "client with defaults"
      (t/is (match?
              (matchers/equals
                {:muuntaja            mtj/muuntaja?
                 :content-negotiation {:encode-fn fn?
                                       :decode-fn fn?}})
              (http/client)))
      (t/is (match?
              (matchers/equals
                {:base-url            base-url
                 :muuntaja            mtj/muuntaja?
                 :content-negotiation {:encode-fn fn?
                                       :decode-fn fn?}})
              (http/client {:base-url base-url}))))
    (t/testing "client with overrides"
      (let [new-encode-fn (constantly :encode)
            new-decode-fn (constantly :decode)]
        (t/is (match?
                (matchers/equals
                  {:muuntaja            mtj/muuntaja?
                   :content-negotiation {:encode-fn new-encode-fn
                                         :decode-fn new-decode-fn}})
                (http/client {:content-negotiation {:encode-fn new-encode-fn
                                                    :decode-fn new-decode-fn}})))))))

(t/deftest ^:integration request
  (let [cli (http/client {:base-url "https://postman-echo.com"})]
   (t/testing "requests without body"
     (let [req {:method :get
                :headers {:x-custom-header "value"}
                :save-request? true
                :path "/get"}]
       (t/is (match?
               {:request {:body-type nil?
                          :headers {"X-Custom-Header" "value"}
                          :url       "https://postman-echo.com/get"
                          :request-method :get}
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
                :headers {:x-custom-header "value"}
                :save-request? true
                :body req-body
                :path "/post"}]
       (t/is (match?
               {:request {:body-type org.apache.http.entity.InputStreamEntity
                          :headers {"X-Custom-Header" "value"}
                          :url       "https://postman-echo.com/post"
                          :request-method :post}
                :status 200
                :success? true?
                :headers map?
                :body {:args map?
                       :headers {:x-custom-header any?}
                       :json (matcher/equals req-body)
                       :data (matcher/equals req-body)
                       :url "https://postman-echo.com/post"}}
               (http/request cli req)))))))
