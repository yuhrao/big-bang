---
sidebar_label: json
title: yuhrao.serdes.content-negotiation.json
toc_min_heading_level: 2
toc_max_heading_level: 4
custom_edit_url:
---





### clj\-&gt;json {#clj--GT-json}
``` clojure
(clj->json x _opts)
```

<p><sub><a href="https://github.com/yuhrao/big-bang/blob/main//src/yuhrao/serdes/content_negotiation/json.clj#L24-L25">Source</a></sub></p>

### encoder\-key\-fn {#encoder-key-fn}
``` clojure
(encoder-key-fn k)
```

<p><sub><a href="https://github.com/yuhrao/big-bang/blob/main//src/yuhrao/serdes/content_negotiation/json.clj#L12-L16">Source</a></sub></p>

### json\-&gt;clj {#json--GT-clj}
``` clojure
(json->clj json-str {:keys [key-fn], :or {key-fn csk/->kebab-case-keyword}})
```

<p><sub><a href="https://github.com/yuhrao/big-bang/blob/main//src/yuhrao/serdes/content_negotiation/json.clj#L32-L34">Source</a></sub></p>

### json\-format {#json-format}

<p><sub><a href="https://github.com/yuhrao/big-bang/blob/main//src/yuhrao/serdes/content_negotiation/json.clj#L64-L68">Source</a></sub></p>

### json\-stream\-&gt;clj {#json-stream--GT-clj}
``` clojure
(json-stream->clj reader {:keys [key-fn], :or {key-fn csk/->kebab-case-keyword}})
```

<p><sub><a href="https://github.com/yuhrao/big-bang/blob/main//src/yuhrao/serdes/content_negotiation/json.clj#L27-L30">Source</a></sub></p>

### write {#write}
``` clojure
(write x w)
```

<p><sub><a href="https://github.com/yuhrao/big-bang/blob/main//src/yuhrao/serdes/content_negotiation/json.clj#L36-L37">Source</a></sub></p>
