(ns br.dev.yuhri.http-client.core
  (:require [br.dev.yuhri.http-client.client :as http-client.client]
            [br.dev.yuhri.http-client.request :as http-client.req]))

(defn client
  "Create a new HTTP Client. Opts:
  - :base-url: Base URL for all requests
  - :interceptors: a list of babashka.http-client interceptors. These interceptors will be placed after default interceptors
  - :req: a request map to be passed to all requests"
  ([] (client {}))
  ([opts]
   (http-client.client/client opts)))

(defn request
  "Executes a HTTP request. Opts:
  - :path - Path to be appended to base-url
  - :method - HTTP method
  - :headers - HTTP headers
  - :body - HTTP body
  - :bearer-token - Token to be added in Authorization header"
  [client opts]
  (http-client.req/execute client opts))
