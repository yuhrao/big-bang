(ns br.dev.yuhri.webserver.middlewares
  (:require [camel-snake-kebab.core :as csk]
            [camel-snake-kebab.extras :as cske]
            [br.dev.yuhri.webserver.middlewares.log :as y.mid.log]
            [br.dev.yuhri.webserver.middlewares.obscurer :as y.mid.obscurer]))

(def format-header-middleware
  {:name ::format-header
   :wrap (fn [handler]
           (fn [{headers :headers
                 :as     req}]
             (let [res (handler (->> headers
                                     (cske/transform-keys csk/->kebab-case-keyword)
                                     (assoc req :headers)))]
               (update res :headers (partial cske/transform-keys csk/->HTTP-Header-Case-String)))))})

(def ^:private status-mapper
  {:ok                   200
   :created              201
   :accepted             202
   :no-content           204

   :moved-permanently    301
   :found                302

   :bad-request          400
   :unauthorized         401
   :payment-required     402
   :forbidden            403
   :not-found            404
   :method-not-allowed   405
   :not-acceptable       406
   :request-timeout      408
   :conflict             409
   :pre-condition-failed 412})

(def status-code-middleware
  {:name ::status-code
   :wrap (fn [handler]
           (fn [req]
             (let [{:keys [status] :as res} (handler req)]
               (if (number? status)
                 res
                 (update res :status status-mapper)))))})

(def trace-id-middleware
  {:name ::trace-id
   :wrap (fn [handler]
           (fn [req]
             (let [trace-id (random-uuid)
                   res      (handler (assoc req :trace-id trace-id))]
               (update res :headers #(assoc % "X-Trace-Id" (str trace-id))))))})

(def log-middleware y.mid.log/log-middleware)

(def obscurer-middleware y.mid.obscurer/obscurer-middleware)
