(ns br.dev.yuhri.webserver.core-test
  (:require
    [br.dev.yuhri.serdes.core.content-negotiation :as content-negotiation]
    [br.dev.yuhri.webserver.core :as ws]
    [clj-http.client :as http]
    [clojure.test :as t]
    [matcher-combinators.test]
    [muuntaja.core :as mtj])
  (:import (clojure.lang ExceptionInfo)))

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
        app    (ws/app {:routes routes})]
    (t/testing "route request"
      (let [req {:request-method :get
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
          app      (ws/app {:routes [["/test" {:get {:parameters {:body [:map
                                                                         [:name :string]
                                                                         [:age :int]
                                                                         [:birth-date :string]
                                                                         [:camel-case :boolean]]}
                                                     :handler    (fn [{body    :body-params
                                                                       headers :headers}]
                                                                   (t/is (match? expected body))
                                                                   (t/is {:custom-header "true"}
                                                                         headers)
                                                                   {:status 200})}}]]})]
      (->
        {:request-method :get
         :uri            "/test"
         :headers        {"Custom-Header" "true"}
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
          app        (ws/app {:routes      routes
                              :middlewares [middleware]})]
      (app {:request-method :get
            :uri            "/test"})
      (t/is @assertion))))

(t/deftest web-server-test
  (let [routes [["/test"
                 ["/success" {:post (fn [_req]
                                      {:status 200
                                       :body   {:success true}})}]
                 ["/bad-request" {:post {:parameters {:body [:map
                                                             [:name :string]]}
                                         :handler    (fn [_req]
                                                       {:status 200})}}]
                 ["/server-error" {:get (fn [_req]
                                          (throw (ex-info "something wrong" {:has "happened"})))}]]]]
    (ws/start! {:server-id :test
                :routes    routes
                :port      3333})
    (t/testing "success"
      (t/is {:status 200
             :body   {:succes true}}
            (parse-response
              (http/post "http://localhost:3333/test/success"))))
    (t/testing "failure"
      (t/is (thrown-match?
              ExceptionInfo
              {:status 400}
              (http/post "http://localhost:3333/test/bad-request")))
      (t/is (thrown-match?
              ExceptionInfo
              {:status 500}
              (http/post "http://localhost:3333/test/server-error")))))
  (ws/stop! :test))

;(defn- parse-response [{:keys [body] :as res}]
;  (cond-> res
;          body (update :body serdes/json->clj)))

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
        server-opts {:server-id :swagger-test
                     :port      1234
                     :openapi   openapi
                     :routes    routes}
        base-url    "http://localhost:1234"]

    (ws/start! server-opts)
    (t/testing "swagger.json"
      (let [res (->> (str base-url "/swagger.json")
                     http/get
                     parse-response)]
        (t/is (match? {:status 200
                       :body   (merge
                                 {:swagger "2.0"
                                  :paths   {(keyword "/test")      map?
                                            (keyword "/test/{id}") map?}}
                                 openapi)}
                      res))))
    (t/testing "openapi.json"
      (let [res (->> (str base-url "/openapi.json")
                     http/get
                     parse-response)]
        (t/is (match? {:status 200
                       :body   (merge
                                 {:openapi "3.0.0"
                                  :paths   {(keyword "/test")      map?
                                            (keyword "/test/{id}") map?}}
                                 openapi)}
                      res))))
    (t/testing "swagger-ui"
      (let [res (->> (str base-url "/doc/index.html")
                     http/get)]
        (t/is (match? {:status  200
                       :headers {"Content-Type" "text/html"}}
                      res))))
    (ws/stop! :swagger-test)))
