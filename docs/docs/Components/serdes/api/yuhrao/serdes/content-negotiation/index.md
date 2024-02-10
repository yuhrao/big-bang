---
sidebar_label: content-negotiation
title: yuhrao.serdes.content-negotiation
toc_min_heading_level: 2
toc_max_heading_level: 4
custom_edit_url:
---





### create\-instance {#create-instance}
``` clojure
(create-instance {:keys [json-opts]})
```

<p><sub><a href="https://github.com/yuhrao/big-bang/blob/main//src/yuhrao/serdes/content_negotiation.clj#L49-L57">Source</a></sub></p>

### decode {#decode}
``` clojure
(decode format v)
(decode muuntaja-or-opts format v)
```

<p><sub><a href="https://github.com/yuhrao/big-bang/blob/main//src/yuhrao/serdes/content_negotiation.clj#L67-L73">Source</a></sub></p>

### default\-opts {#default-opts}

<p><sub><a href="https://github.com/yuhrao/big-bang/blob/main//src/yuhrao/serdes/content_negotiation.clj#L32-L43">Source</a></sub></p>

### encode {#encode}
``` clojure
(encode format v)
(encode muuntaja-or-opts format v)
```

<p><sub><a href="https://github.com/yuhrao/big-bang/blob/main//src/yuhrao/serdes/content_negotiation.clj#L59-L65">Source</a></sub></p>

### extract\-accept {#extract-accept}
``` clojure
(extract-accept req)
```

<p><sub><a href="https://github.com/yuhrao/big-bang/blob/main//src/yuhrao/serdes/content_negotiation.clj#L22-L30">Source</a></sub></p>

### extract\-content\-type {#extract-content-type}
``` clojure
(extract-content-type req)
```

<p><sub><a href="https://github.com/yuhrao/big-bang/blob/main//src/yuhrao/serdes/content_negotiation.clj#L12-L20">Source</a></sub></p>

### muuntaja {#muuntaja}

<p><sub><a href="https://github.com/yuhrao/big-bang/blob/main//src/yuhrao/serdes/content_negotiation.clj#L46-L47">Source</a></sub></p>
