---
sidebar_label: client
title: yuhrao.http-client.client
toc_min_heading_level: 2
toc_max_heading_level: 4
custom_edit_url:
---





### client {#client}
``` clojure
(client {:keys [base-url interceptors], :as opts})
```


Create a new HTTP Client. Opts:
  - :base-url: Base URL for all requests
  - :interceptors: a list of babashka.http-client interceptors. These interceptors will be placed after default interceptors
  - :req: a request map to be passed to all requests
<p><sub><a href="https://github.com/yuhrao/big-bang/blob/main//src/yuhrao/http_client/client.clj#L13-L24">Source</a></sub></p>
