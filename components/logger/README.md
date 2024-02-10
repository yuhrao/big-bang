# Logger

Standardized logs with various targets (Publishers)

<!-- markdown-toc start - Don't edit this section. Run M-x markdown-toc-refresh-toc -->
**Table of Contents**

- [Logger](#logger)
    - [Libs](#libs)
    - [Usage](#usage)
        - [Setup your logger](#setup-your-logger)
        - [Publishers](#publishers)
        - [Console Publisher](#console-publisher)
        - [File Publisher](#file-publisher)
        - [Obscurer](#obscurer)

<!-- markdown-toc end -->



## Libs

| Lib  | Description                            | Docs                                                                                                                        |
|------|----------------------------------------|:----------------------------------------------------------------------------------------------------------------------------|
| uLog | Async log streaming to various targets | [cljdoc](https://cljdoc.org/d/com.brunobonacci/mulog/0.9.0/doc/readme)<br/> [github](https://github.com/BrunoBonacci/mulog) |

## Usage

### Setup your logger

```clojure
(require '[yuhrao.logger.core :as logger])

(logger/setup! {:publishers {:console {}}
                :min-level  :debug})
```

### Publishers

All built-in publishers are based on µLog default publishers.
Because of that, their configuration is slightly different.

- Instead of a `:transform` option that receives a function to handle logs, we have a `:xfn`
  option that receives a transducer to manipulate logs.

### Console Publisher

Config: same parameters as
µLog's [Simple Console Publisher](https://cljdoc.org/d/com.brunobonacci/mulog/0.9.0/doc/publishers/simple-console-publisher)
> Logger already includes `:type :console` parameter

> You can pass an empty map as config to start a standard console publisher

```clojure
(require '[yuhrao.logger.core :as logger])

(logger/setup! {:publishers {:console {:pretty? false
                                       :xfn     (map identity)}}})
```

### File Publisher

Config: same parameters as
µLog's [Simple File Publisher](https://cljdoc.org/d/com.brunobonacci/mulog/0.9.0/doc/publishers/simple-file-publisher)
> Logger already includes `:type :simple-file` parameter

```clojure
(require '[yuhrao.logger.core :as logger])

(logger/setup! {:publishers {:file {:filename "/tmp/app/server.log"
                                    :xfn      (map identity)}}})
```

### Obscurer
Obscure sensitive data. Pass a function that receives a map (that's the log's data) and return a map with data obscured.
It's recommended to use `yuhrao.data-cloak.core.map/obscurer`

```clojure
(require '[yuhrao.logger.core :as logger]
         '[yuhrao.data-cloak.core.map :as dc.map]
         '[yuhrao.data-cloak.core.string :as dc.string])


(logger/setup! {:publishers {:file {:filename "/tmp/app/server.log"}}
                :obscurer (dc.map/obscurer {:email dc.string/email})})

(logger/info ::my-log "something to be logged"
             {:email "some-mail@gmail.com"
              :random "value"})
;; =>> {...logfields :data {:email "so*****il@gmail.com" :random "value"}}
```
