---
{sidebar_position: 0, custom_edit_url: 'https://github.com/yuhrao/big-bang/tree/main/README.md'}
---

# Config

Read configuration from various sources

<!-- markdown-toc start - Don't edit this section. Run M-x markdown-toc-refresh-toc -->
**Table of Contents**

- [Config](#config)
    - [Libs](#libs)
    - [Usage](#usage)
        - [Config sources](#config-sources)
        - [Getting configuration](#getting-configuration)

<!-- markdown-toc end -->


## Libs

None.

## Usage

### Config sources

Order by priority

1. Java properties 
2. Environment Variables

### Getting configuration

You should provide map to specify how to get each value.
The result will be a map with the same keys as your specification, but
instead the options, will contain respective config values

```clojure
(require '[br.dev.yuhri.config.core :as cfg])
(def config
  (cfg/create {:port  {:env      "SERVER_PORT" ;; Get value from env SERVER_PORT
                       :parse-fn #(Integer/parseInt %)} ;; parses value to an integer
               :host  {:prop "app.server.port"} ;; Get value from property app.server.port
               :test1 {:env  "TEST_1"
                       :prop "random.test.1"} ;;  Get value from property app.server.port
               :abc   {:env      "ABC"
                       :parse-fn keyword
                       :default  :def}})) ;; If value didn't exists or is empty, uses default value

;; Example output
;;=>{:port 8080 :host "0.0.0.0" :test "random" :abc :def}
```
