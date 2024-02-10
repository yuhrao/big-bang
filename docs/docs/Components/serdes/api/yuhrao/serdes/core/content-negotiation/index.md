---
sidebar_label: content-negotiation
title: yuhrao.serdes.core.content-negotiation
toc_min_heading_level: 2
toc_max_heading_level: 4
custom_edit_url:
---





### decode {#decode}
``` clojure
(decode format v)
(decode muuntaja format v)
```


Decode various formats into a clojure value (usually a collection)
<p><sub><a href="https://github.com/yuhrao/big-bang/blob/main//src/yuhrao/serdes/core/content_negotiation.clj#L28-L33">Source</a></sub></p>

### encode {#encode}
``` clojure
(encode format v)
(encode muuntaja format v)
```


Encode a value into a specified format (e.g. application/json)
<p><sub><a href="https://github.com/yuhrao/big-bang/blob/main//src/yuhrao/serdes/core/content_negotiation.clj#L21-L26">Source</a></sub></p>

### extract\-accept {#extract-accept}
``` clojure
(extract-accept req)
```


Extract accept from request.
  If it's not present, defaults to application/json
<p><sub><a href="https://github.com/yuhrao/big-bang/blob/main//src/yuhrao/serdes/core/content_negotiation.clj#L15-L19">Source</a></sub></p>

### extract\-content\-type {#extract-content-type}
``` clojure
(extract-content-type req-or-res)
```


Extract content type from request or response.
  If it's not present, defaults to application/json
<p><sub><a href="https://github.com/yuhrao/big-bang/blob/main//src/yuhrao/serdes/core/content_negotiation.clj#L9-L13">Source</a></sub></p>

### muuntaja {#muuntaja}
``` clojure
(muuntaja)
```


Gets the default muuntaja instance for content negotiation
<p><sub><a href="https://github.com/yuhrao/big-bang/blob/main//src/yuhrao/serdes/core/content_negotiation.clj#L4-L7">Source</a></sub></p>
