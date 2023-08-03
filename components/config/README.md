# Config

Read configuration from various sources

## TOC

<!-- TOC -->
* [Config](#config)
  * [TOC](#toc)
  * [Libs](#libs)
  * [Usage](#usage)
    * [Config sources](#config-sources)
    * [Getting configuration](#getting-configuration)
<!-- TOC -->

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
