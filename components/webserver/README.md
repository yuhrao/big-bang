# Webserver

Component for create WebServers

## Embeded features
- Swagger & Openapi docs
- Serialization/Deserialization for most common content types
- Request logging/tracing

## Server opts

### Ring specific configs
You can pass the following configs for ring webserver

- `:port`: Server port
- `:host`: Server host (defaults to `0.0.0.0`)
- `:join?`: decide whether to block the main thread or not (defaults to false)

### Router
Accepts a `reitit` route spec 

```clojure
(require '[br.dev.yuhri.webserver.core :as ws])

(def routes [["/test"
              ["" {:get {:handler (fn [req]
                                    (t/is (match? uuid? (:trace-id req)))
                                    {:status  :ok
                                     :headers {:custom-header "true"}
                                     :body    {:test true}})}}]
              ["/validate/:id" {:get {:parameters {:body [:map
                                                          [:name :string]]
                                                   :path [:map
                                                          [:id :int]]}
                                      :handler    (fn [_req]

                                                    {:status 200})}}]]])

(ws/server {:routes routes
            :port   80})
```

### Middlewares
Put your `reitit` middlewares under the `:middlewares` key. They will be put **after all default middlewares**

### Swagger & Openapi 3

When `:openapi` map is present in opts, it enables Swagger 2 & Openapi 3 resources and swagger-ui.

Doc routes:
- `/docs`: Swagger UI
- `/openapi.json`: Openapi 3 specs
- `/swagger.json`: Swagger 2 specs

```clojure
(def openapi {:info {:title       "My app"
                     :version     "0.0.0"
                     :description "My beautiful app"}})

(def routes [["/test" {:get {:parameters {:body [:map
                                                 [:name :string]]}
                             :responses  {200 {:body [:map
                                                      [:status :string]]}}

                             :handler    (constantly {:status 200
                                                      :body   {:status "healthy"}})}}]
             ["/test/:id" {:get {:parameters {:body [:map
                                                     [:name :string]]
                                              :path [:map
                                                     [:id :string]]}
                                 :handler    (constantly {:status 200
                                                          :body   {:status :healthy}})}}]])
(def server-opts {:server-id :swagger-test
                  :port      1234
                  :openapi   openapi
                  :routes    routes})

(ws/server server-opts)

```

### Logs
Logs are enabled by default and it uses the `logger` component internally. You can disable logs passing `:disable-logs?` to server options

Notice that you **must** start logger to be able to see logs being produced
