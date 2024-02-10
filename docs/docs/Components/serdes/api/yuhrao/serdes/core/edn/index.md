---
sidebar_label: edn
title: yuhrao.serdes.core.edn
toc_min_heading_level: 2
toc_max_heading_level: 4
custom_edit_url:
---





### clj\-&gt;edn {#clj--GT-edn}
``` clojure
(clj->edn v)
```


Parses a clojure collection into a edn string
<p><sub><a href="https://github.com/yuhrao/big-bang/blob/main//src/yuhrao/serdes/core/edn.clj#L10-L13">Source</a></sub></p>

### clj\-&gt;edn\-stream {#clj--GT-edn-stream}
``` clojure
(clj->edn-stream v)
```


Parses a clojure collection into a edn stream
<p><sub><a href="https://github.com/yuhrao/big-bang/blob/main//src/yuhrao/serdes/core/edn.clj#L15-L18">Source</a></sub></p>

### edn\-&gt;clj {#edn--GT-clj}
``` clojure
(edn->clj v)
```


Parses a edn string or stream into a clojure collection.
  It doesn't do any transformation over keys
<p><sub><a href="https://github.com/yuhrao/big-bang/blob/main//src/yuhrao/serdes/core/edn.clj#L4-L8">Source</a></sub></p>
