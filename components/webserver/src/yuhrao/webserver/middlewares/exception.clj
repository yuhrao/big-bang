(ns yuhrao.webserver.middlewares.exception
  (:require [reitit.ring.middleware.exception :as r.m.exception]))

r.m.exception/default-handlers

(def exception-middleware
  (r.m.exception/create-exception-middleware
   (merge
    {::r.m.exception/default (fn [ex _req]
                               (println (type ex))
                               (println (ex-data ex))
                               {:status 500})})))
