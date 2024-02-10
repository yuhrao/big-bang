---
sidebar_label: json
title: yuhrao.serdes.core.json
toc_min_heading_level: 2
toc_max_heading_level: 4
custom_edit_url:
---





### clj\-&gt;json {#clj--GT-json}
``` clojure
(clj->json v)
(clj->json v opts)
```


Parses a clojure collection into a json string
<p><sub><a href="https://github.com/yuhrao/big-bang/blob/main//src/yuhrao/serdes/core/json.clj#L12-L17">Source</a></sub></p>

### clj\-&gt;json\-stream {#clj--GT-json-stream}
``` clojure
(clj->json-stream v)
(clj->json-stream v opts)
```


Parses a clojure collection into a json stream
<p><sub><a href="https://github.com/yuhrao/big-bang/blob/main//src/yuhrao/serdes/core/json.clj#L19-L24">Source</a></sub></p>

### json\-&gt;clj {#json--GT-clj}
``` clojure
(json->clj v)
(json->clj v opts)
```


Parses a json string or stream into a clojure collection.
  It transforms keys to kebab case keyword automatically
<p><sub><a href="https://github.com/yuhrao/big-bang/blob/main//src/yuhrao/serdes/core/json.clj#L4-L10">Source</a></sub></p>
