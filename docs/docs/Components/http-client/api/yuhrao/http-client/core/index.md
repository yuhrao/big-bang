---
sidebar_label: core
title: yuhrao.http-client.core
toc_min_heading_level: 2
toc_max_heading_level: 4
custom_edit_url:
---





### client {#client}
``` clojure
(client)
(client opts)
```


Create a new HTTP Client. Opts:
  - `:base-url`: Base URL for all requests
  - `:interceptors`: a list of babashka.http-client interceptors. These interceptors will be placed after default interceptors
  - `:req`: a request map to be passed to all requests
<p><sub><a href="https://github.com/yuhrao/big-bang/blob/main//src/yuhrao/http_client/core.clj#L5-L12">Source</a></sub></p>

### request {#request}
``` clojure
(request client opts)
```


Executes a HTTP request. Opts:
  - `:path` - Path to be appended to base-url
  - `:method` - HTTP method
  - `:headers` - HTTP headers
  - `:body` - HTTP body
  - `:bearer-token` - Token to be added in Authorization header

  Observations:
  - Request and response bodies are automatically serialized/deserialized based on content type. See `yuhrao.serdes.core` for more information
  - Headers are normalized to HTTP-Header-Case. You can send any kv format in there
<p><sub><a href="https://github.com/yuhrao/big-bang/blob/main//src/yuhrao/http_client/core.clj#L14-L26">Source</a></sub></p>
