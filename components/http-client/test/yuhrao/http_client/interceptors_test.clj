(ns yuhrao.http-client.interceptors-test
  (:require [yuhrao.http-client.interceptors :as http.interceptors]
            [clojure.test :as t]
            [matcher-combinators.matchers :as matcher]
            [yuhrao.serdes.core.json :as json]
            [matcher-combinators.test]))

(t/deftest format-headers
  (let [{req-fn :request
         res-fn :response} http.interceptors/format-headers]
    (t/is
      (match?
        {:headers {"Content-Type"    "application/json"
                   "X-Custom-Header" "value"}}
        (req-fn {:headers {:content-type    "application/json"
                           :x-custom-header "value"}})))
    (t/is
      (match?
        {:headers {:content-type    "application/json"
                   :x-custom-header "value"}}
        (res-fn {:headers {"Content-Type"    "application/json"
                           "X-Custom-Header" "value"}})))))

(t/deftest content-negotiation
  (let [{req-fn :request
         res-fn :response} http.interceptors/content-negotiation]
    (t/testing "content-type option"
      (let [payload           {:content-type :json}
            payload-with-body (assoc payload :body {:test true})]
        (t/is (match?
                (matcher/equals
                  payload)
                (req-fn payload)))

        (t/is (match?
                (matcher/equals
                 (-> payload-with-body
                     (update :body json/clj->json)
                     (assoc :headers {"Content-Type" "application/json"})))
                (update (req-fn payload-with-body) :body #(when % (slurp %)))))))

    (t/testing "no body"
      (let [payload {:headers {:accept       "application/json"
                               :content-type "application/json"}}]
        (t/is (match?
                (matcher/equals
                  payload)
                (req-fn payload)))
        (t/is (match?
                (matcher/equals
                  payload)
                (res-fn payload)))))

    (t/testing "no content type"
      (let [payload {:body {:test true}}]
        (t/is (match?
               {:body (str (:body payload))}
                (update (req-fn payload) :body #(when % (slurp %)))))
        (t/is (match?
               {:body (str (:body payload))}
                (res-fn (update  payload :body str))))))))

(t/deftest authorization
  (let [{req-fn :request} http.interceptors/authorization]
    (t/is (match?
            {:headers {:authorization "Bearer token"}}
            (req-fn {:bearer-token "token"})))
    (t/is (match?
            (matcher/equals {})
            (req-fn {})))))
