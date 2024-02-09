(ns yuhrao.webserver.server
  (:require [yuhrao.webserver.middlewares :as y.middlewares]
            [yuhrao.serdes.core.content-negotiation :as content-negotiation]
            [reitit.coercion.malli :as r.malli.coercion]
            [reitit.interceptor.sieppari :as r.sieppari]
            [reitit.openapi :as r.openapi]
            [reitit.ring :as ring]
            [reitit.ring.coercion :as ring.coercion]
            [reitit.ring.middleware.exception :as r.m.exception]
            [reitit.ring.middleware.muuntaja :as r.m.muuntaja]
            [reitit.ring.middleware.parameters :as r.m.params]
            [reitit.swagger :as r.swagger]
            [reitit.swagger-ui :as r.swagger-ui]
            [ring.adapter.jetty :as jetty])
  (:import (org.eclipse.jetty.server Server)))


(defn- create-router [{:keys [routes
                              middlewares
                              openapi
                              disable-logs?]}]
  (let [middlewares (->> middlewares
                         (concat
                           [y.middlewares/trace-id-middleware
                            (when (not disable-logs?) y.middlewares/log-middleware)
                            r.swagger/swagger-feature
                            r.openapi/openapi-feature
                            r.m.params/parameters-middleware
                            y.middlewares/format-header-middleware
                            r.m.muuntaja/format-negotiate-middleware
                            r.m.muuntaja/format-response-middleware
                            r.m.muuntaja/format-request-middleware
                            r.m.exception/exception-middleware
                            ring.coercion/coerce-response-middleware
                            ring.coercion/coerce-request-middleware
                            y.middlewares/obscurer-middleware
                            y.middlewares/status-code-middleware])
                         (remove empty?))]
    (ring/router
      (cond->> routes
               openapi (concat [["/openapi.json"
                                 {:get {:no-doc  true
                                        :openapi openapi
                                        :handler (fn [& args]
                                                   (-> (r.openapi/create-openapi-handler)
                                                       (apply args)
                                                       ;; Hacky way to make it work because current swagger-ui
                                                       ;; supports only open api 3.0.n
                                                       (assoc-in [:body :openapi] "3.0.0")))}}]
                                ["/swagger.json"
                                 {:get {:no-doc  true
                                        :swagger openapi
                                        :handler (r.swagger/create-swagger-handler)}}]
                                ["/doc/*" {:get {:no-doc  true
                                                 :handler (r.swagger-ui/create-swagger-ui-handler
                                                            {:config {:validatorUrl     nil
                                                                      :urls             [{:name "swagger" :url "/swagger.json"}
                                                                                         {:name "openapi" :url "/openapi.json"}]
                                                                      :urls.primaryName "openapi"
                                                                      :operationsSorter "alpha"}})}}]]))
      {:data {:coercion   r.malli.coercion/coercion
              :muuntaja   (content-negotiation/muuntaja)
              :middleware middlewares}})))

(defn app [opts]
  (let [router (create-router opts)]
    (ring/ring-handler router
                       {:executor r.sieppari/executor})))

(defn start [{:keys [join?
                     port
                     host]
              :or   {join? false
                     host  "0.0.0.0"}
              :as   opts}]
  (jetty/run-jetty (app opts) {:join? join?
                               :port  port
                               :host  host}))

(defn stop [^Server server]
  (.stop server))
