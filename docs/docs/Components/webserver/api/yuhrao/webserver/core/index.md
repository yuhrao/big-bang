---
sidebar_label: core
title: yuhrao.webserver.core
toc_min_heading_level: 2
toc_max_heading_level: 4
custom_edit_url:
---





### app {#app}
``` clojure
(app config)
```


Create a ring handler from a config map.
  Config map:
  - `:router` - A reitit router. [Reitit Docs](https://cljdoc.org/d/metosin/reitit/0.6.0/doc/basics/router)
  - `:disable-logs?` - A boolean to disable logs. Default is `false`
  - `:openapi` - A map with openapi options. [Reitit OpenAPI Docs](https://cljdoc.org/d/metosin/reitit/0.7.0-alpha7/doc/ring/openapi-support?q=openapi#openapi-support)
  - `:swagger` - A map with swagger options. [Reitit Swagger Docs](https://cljdoc.org/d/metosin/reitit/0.7.0-alpha7/doc/ring/swagger-support)
  - `:middlewares` - A vector of middlewares.

  Observations:
  - When neither `:openapi` nor `:swagger` are provided, the server will not serve any openapi or swagger documentation.
  - Middlewares are applied using sieppari.
  
<p><sub><a href="https://github.com/yuhrao/big-bang/blob/main//src/yuhrao/webserver/core.clj#L6-L21">Source</a></sub></p>

### servers {#servers}

<p><sub><a href="https://github.com/yuhrao/big-bang/blob/main//src/yuhrao/webserver/core.clj#L4-L4">Source</a></sub></p>

### start\! {#start-BANG-}
``` clojure
(start! {:keys [server-id], :as config})
```


Start a server from a config map.
  Config map:
  - `:server-id` - A unique identifier for the server. Used to stop the server.
  - `:join?` - A boolean to join the server thread. Default is `false`
  - `:port` - A port number. No default is provided
  - `:host` - A host name. Defaults to `0.0.0.0`
  - Accpts any parameter that [[`app`](#app)](#app) function accepts.

  Notes:
  - Ring handler is created internally using [[`app`](#app)](#app) function.
  - Shutdown hook is automatically added to stop the server when the JVM is stopped.
<p><sub><a href="https://github.com/yuhrao/big-bang/blob/main//src/yuhrao/webserver/core.clj#L23-L39">Source</a></sub></p>

### stop\! {#stop-BANG-}
``` clojure
(stop! server-id)
```


Stop a server by its id.
<p><sub><a href="https://github.com/yuhrao/big-bang/blob/main//src/yuhrao/webserver/core.clj#L41-L49">Source</a></sub></p>
