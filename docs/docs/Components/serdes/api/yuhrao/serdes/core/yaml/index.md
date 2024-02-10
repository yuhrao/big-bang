---
sidebar_label: yaml
title: yuhrao.serdes.core.yaml
toc_min_heading_level: 2
toc_max_heading_level: 4
custom_edit_url:
---





### clj\-&gt;yaml {#clj--GT-yaml}
``` clojure
(clj->yaml v)
```


Parses a clojure collection into a yaml string
<p><sub><a href="https://github.com/yuhrao/big-bang/blob/main//src/yuhrao/serdes/core/yaml.clj#L10-L13">Source</a></sub></p>

### clj\-&gt;yaml\-stream {#clj--GT-yaml-stream}
``` clojure
(clj->yaml-stream v)
```


Parses a clojure collection into a yaml stream
<p><sub><a href="https://github.com/yuhrao/big-bang/blob/main//src/yuhrao/serdes/core/yaml.clj#L15-L18">Source</a></sub></p>

### yaml\-&gt;clj {#yaml--GT-clj}
``` clojure
(yaml->clj v)
```


Parses a yaml string or stream into a clojure collection.
  It transforms keys to kebab case keyword automatically
<p><sub><a href="https://github.com/yuhrao/big-bang/blob/main//src/yuhrao/serdes/core/yaml.clj#L4-L8">Source</a></sub></p>
