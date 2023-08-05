(ns br.dev.yuhri.http-client.core
  (:require [br.dev.yuhri.http-client.client :as http-client.client]
            [br.dev.yuhri.http-client.request :as http-client.req]))

(defn client
  "Create a new HTTP clients with"
  ([] (client {}))
  ([opts]
   (http-client.client/client opts)))

(defn request [client opts]
  (http-client.req/execute client opts))
