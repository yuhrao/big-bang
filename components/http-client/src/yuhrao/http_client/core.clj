(ns yuhrao.http-client.core
  (:require [yuhrao.http-client.client :as http-client.client]
            [yuhrao.http-client.request :as http-client.req]))

(defn client
  "Create a new HTTP Client. Opts:
  - `:base-url`: Base URL for all requests
  - `:interceptors`: a list of babashka.http-client interceptors. These interceptors will be placed after default interceptors
  - `:req`: a request map to be passed to all requests"
  ([] (client {}))
  ([opts]
   (http-client.client/client opts)))

(defn request
  "Executes a HTTP request. Opts:
  - `:path` - Path to be appended to base-url
  - `:method` - HTTP method
  - `:headers` - HTTP headers
  - `:body` - HTTP body
  - `:bearer-token` - Token to be added in Authorization header

  Observations:
  - Request and response bodies are automatically serialized/deserialized based on content type. See `yuhrao.serdes.core` for more information
  - Headers are normalized to HTTP-Header-Case. You can send any kv format in there"
  [client opts]
  (http-client.req/execute client opts))
