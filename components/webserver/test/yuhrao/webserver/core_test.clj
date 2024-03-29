(ns yuhrao.webserver.core-test
  (:require
   [yuhrao.serdes.core.content-negotiation :as content-negotiation]
   [yuhrao.webserver.core :as ws]
   [yuhrao.http-client.core :as http]
   [clojure.test :as t]
   [matcher-combinators.matchers :as matcher]
   [matcher-combinators.test]
   [muuntaja.core :as mtj]

   [yuhrao.data-cloak.core.string :as dc.string]))

(defn- prepare-request [{:keys [body headers] :as req}]
  (cond-> req
    body (assoc :body (mtj/encode
                       (content-negotiation/muuntaja)
                       (get "Content-Type" headers "application/json")
                       body))))

(defn- parse-response
  [{:keys [body headers] :as res}]
  (cond-> res
    body (assoc :body (mtj/decode
                       (content-negotiation/muuntaja)
                       (get "Content-Type" headers "application/json")
                       body))))

(t/deftest app-tests
  (let [routes [["/test"
                 ["" {:get {:handler (fn [req]
                                       (t/is (match? uuid? (:trace-id req)))
                                       {:status  :ok
                                        :headers {:custom-header "true"}
                                        :body    {:test true}})}}]
                 ["/validate/:id" {:get {:parameters {:body [:map
                                                             [:name :string]]
                                                      :path [:map
                                                             [:id :int]]}
                                         :handler    (fn [_req]

                                                       {:status 200})}}]]]
        app (ws/app {:routes        routes
                     :disable-logs? true})]
    (t/testing "route request"
      (let [req {:request-method :get
                 :headers        {"Accept" "application/json"}
                 :uri            "/test"}
            res (-> req prepare-request app parse-response)]
        (t/is (match? {:status  200
                       :body    {:test true}
                       :headers {"Custom-Header" "true"
                                 "X-Trace-Id"    (comp uuid? parse-uuid)}}
                      res))))

    (t/testing "request validation"
      (let [req {:request-method :get
                 :uri            "/test/validate/1234"
                 :headers        {"Accept" "application/json"
                                  "Content-Type" "application/json"}
                 :body           {:name "john"}}
            res (-> req prepare-request app parse-response)]
        (t/is (match? {:status 200}
                      res)))
      (t/is (match?
             {:status 400}
             (let [req {:request-method :get
                        :uri            "/test/validate/abd"}]
               (app req))))
      (t/is (match?
             {:status 400}
             (let [req {:request-method :get
                        :uri            "/test/validate/412"
                        :headers        {"Accept" "application/json"}
                        :body           (content-negotiation/encode
                                         "application/json"
                                         {:name 1})}]
               (app req))))))
  (t/testing "request parsing"
    (let [payload  {"Name"       "daniel"
                    "age"        12
                    "birth_date" "1998-03-12"
                    "camelCase"  true}
          expected {:name       "daniel"
                    :age        12
                    :birth-date "1998-03-12"
                    :camel-case true}
          app      (ws/app {:routes        [["/test" {:get {:parameters {:body [:map
                                                                                [:name :string]
                                                                                [:age :int]
                                                                                [:birth-date :string]
                                                                                [:camel-case :boolean]]}
                                                            :handler    (fn [{body    :body-params
                                                                             headers :headers}]
                                                                          (t/is (match? expected body))
                                                                          (t/is {:custom-header "true"}
                                                                                headers)
                                                                          {:status 200})}}]]
                            :disable-logs? true})]
      (->
       {:request-method :get
        :uri            "/test"
        :headers        {"Custom-Header" "true"
                         "Accept"        "application/json"}
        :body           payload}
       prepare-request
       app)))
  (t/testing "custom middlewares"
    (let [assertion  (atom false)
          routes     [["/test" {:get {:handler (fn [req]
                                                 {:status 200})}}]]
          middleware {:name ::test
                      :wrap (fn [handler]
                              (fn [req]
                                (reset! assertion true)
                                (handler req)))}
          app        (ws/app {:routes        routes
                              :disable-logs? true
                              :middlewares   [middleware]})]
      (app {:request-method :get
            :uri            "/test"})
      (t/is @assertion))))

(t/deftest web-server-test
  (let [routes [["/test"
                 ["/success/json" {:post (fn [_req]
                                           {:status 200
                                            :body   {:success true}})}]
                 ["/success/yaml" {:post (fn [_req]
                                           {:status 200
                                            :body   {:success true}
                                            :content-type "application/yaml"})}]
                 ["/bad-request" {:post {:parameters {:body [:map
                                                             [:name :string]]}
                                         :handler    (fn [_req]
                                                       {:status 200})}}]
                 ["/server-error" {:get (fn [_req]
                                          (throw (ex-info "something wrong" {:has "happened"})))}]]]

        http-client (http/client {:base-url "http://localhost:3333"})]
    (ws/start! {:server-id     :test
                :routes        routes
                :disable-logs? true
                :port          3333})
    (t/testing "success"
      (t/is {:status 200
             :headers {"Content-Type" "application/json"}
             :body   {:succes true}}
            (http/request http-client {:path   "/test/success/json"
                                       :method :post})))
    (t/testing "success"
      (t/is {:status 200
             :headers {"Content-Type" (partial re-find #"(?i)application/yaml")}
             :body   {:succes true}}
            (http/request http-client {:path   "/test/success/yaml"
                                       :method :post})))
    (tap> (http/request http-client {:path   "/test/success/yaml"
                                     :method :post}))
    ;; TODO: fix these tests
    #_(t/testing "failure"
        (t/is (thrown-match?
               ExceptionInfo
               {:status 400}
               (http/post "http://localhost:3333/test/bad-request")))
        (t/is (thrown-match?
               ExceptionInfo
               {:status 500}
               (http/post "http://localhost:3333/test/server-error")))))
  (ws/stop! :test))

(t/deftest swagger
  (let [openapi     {:info {:title       "My app"
                            :version     "0.0.0"
                            :description "My beautiful app"}}
        routes      [["/test" {:get {:parameters {:body [:map
                                                         [:name :string]]}
                                     :responses  {200 {:body [:map
                                                              [:status :string]]}}

                                     :handler    (constantly {:status 200
                                                              :body   {:status "healthy"}})}}]
                     ["/test/:id" {:get {:parameters {:body [:map
                                                             [:name :string]]
                                                      :path [:map
                                                             [:id :string]]}
                                         :handler    (constantly {:status 200
                                                                  :body   {:status :healthy}})}}]]
        server-opts {:server-id     :swagger-test
                     :port          1234
                     :openapi       openapi
                     :routes        routes
                     :disable-logs? true}
        http-client (http/client {:base-url "http://localhost:1234"})]

    (ws/start! server-opts)
    (t/testing "swagger.json"
      (let [res (http/request http-client {:path "/swagger.json"
                                           :headers {"Accept" "application/json"}})]
        (t/is (match? {:status 200
                       :body   (merge
                                {:swagger "2.0"
                                 :paths   {(keyword "/test")      map?
                                           (keyword "/test/{id}") map?}}
                                openapi)}
                      res))))
    (t/testing "openapi.json"
      (let [res (http/request http-client {:path "/openapi.json"
                                           :headers {"Accept" "application/json"}})]
        (t/is (match? {:status 200
                       :body   (merge
                                {:openapi "3.0.0"
                                 :paths   {(keyword "/test")      map?
                                           (keyword "/test/{id}") map?}}
                                openapi)}
                      res))))
    (t/testing "swagger-ui"
      (let [res (http/request http-client {:path "/doc/index.html"})]
        (t/is (match? {:status  200
                       :headers {:content-type "text/html"}}
                      res))))
    (ws/stop! :swagger-test)))

(t/deftest obscurer
  (let [app (ws/app {:routes [["/test/a" {:get {:obscurers {:body {:email dc.string/email}
                                                            :headers {:x-custom dc.string/all}}
                                                :handler   (constantly {:status :ok
                                                                        :headers {:x-custom "test"}
                                                                        :body   {:email "random-mail@gmail.com"}})}}]
                              ["/test/b" {:get {:obscurers {:body    {:email dc.string/email}
                                                            :headers {:x-custom dc.string/all}}
                                                :handler   (constantly {:status :no-content})}}]]
                     :disable-logs? true})]
    (t/is (match?
           {:status 200
            :body   (matcher/equals {:email "ra*******il@gmail.com"})
            :headers {"X-Custom" "****"}}
           (-> {:request-method :get
                :headers       {"Accept" "application/json"}
                :uri            "/test/a"}
               app
               parse-response)))
    (t/is (match?
           {:status 204
            :headers {"X-Custom" matcher/absent}}
           (-> {:request-method :get
                :uri            "/test/b"}
               app
               parse-response)))))
