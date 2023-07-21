(ns br.dev.yuhri.webserver.middlewares.log
  (:require [br.dev.yuhri.logger.interface :as logger]
            [clojure.java.io :as io])
  (:import (java.io ByteArrayInputStream
                    ByteArrayOutputStream)))

(defn- route-data [{{:keys [result]} :reitit.core/match
                    method           :request-method}]
  (when (and result method)
    (let [{:keys [middleware]} (method result)
          middlewares (some->> middleware
                               (map :name))]
      {:middlewares middlewares})))

(defn- clone-body [{:keys [body]}]
  (let [byte-array-output-stream (ByteArrayOutputStream.)]
    (.transferTo body byte-array-output-stream)
    [(-> byte-array-output-stream
         (.toByteArray)
         (ByteArrayInputStream.))
     (-> byte-array-output-stream
         (.toByteArray)
         (ByteArrayInputStream.))]))


(defn- assoc-body-str [{:keys [body] :as payload}]
  (cond
    (string? body)
    (assoc payload :body-str body)

    (nil? body)
    payload

    :else
    (let [[original-body cloned-body] (clone-body payload)
          out-stream ^ByteArrayOutputStream (ByteArrayOutputStream.)]
      (io/copy cloned-body out-stream)
      (-> payload
          (assoc :body original-body)
          (assoc :body-str (.toString out-stream))))))

(defn- log-request [req]
  (let [route-data (route-data req)
        log-data   (-> req
                       (dissoc
                         :reitit.core/match
                         :reitit.core/router)
                       (merge route-data))]
    (logger/info :request/received "request received" log-data)))

(def log-middleware
  {:name ::logger
   :wrap (fn [handler]
           (fn [{:keys [trace-id] :as req}]
             (logger/with-context {:request/trace-id trace-id}
                                  (logger/trace
                                    :server/request
                                    {:pairs   []
                                     :capture identity}
                                    (let [req (assoc-body-str req)]
                                      (log-request req)
                                      (assoc-body-str (handler req)))))))})

