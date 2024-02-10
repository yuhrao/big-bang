(ns yuhrao.webserver.core
  (:require [yuhrao.webserver.server :as server]))

(defonce servers (atom {}))

(defn app
  "Create a ring handler from a config map.
  Config map:
  - `:router` - A reitit router. [Reitit Docs](https://cljdoc.org/d/metosin/reitit/0.6.0/doc/basics/router)
  - `:disable-logs?` - A boolean to disable logs. Default is `false`
  - `:openapi` - A map with openapi options. [Reitit OpenAPI Docs](https://cljdoc.org/d/metosin/reitit/0.7.0-alpha7/doc/ring/openapi-support?q=openapi#openapi-support)
  - `:swagger` - A map with swagger options. [Reitit Swagger Docs](https://cljdoc.org/d/metosin/reitit/0.7.0-alpha7/doc/ring/swagger-support)
  - `:middlewares` - A vector of middlewares.

  Observations:
  - When neither `:openapi` nor `:swagger` are provided, the server will not serve any openapi or swagger documentation.
  - Middlewares are applied using sieppari.
  "

  [config]
  (server/app config))

(defn start!
  "Start a server from a config map.
  Config map:
  - `:server-id` - A unique identifier for the server. Used to stop the server.
  - `:join?` - A boolean to join the server thread. Default is `false`
  - `:port` - A port number. No default is provided
  - `:host` - A host name. Defaults to `0.0.0.0`
  - Accpts any parameter that `app` function accepts.

  Notes:
  - Ring handler is created internally using `app` function.
  - Shutdown hook is automatically added to stop the server when the JVM is stopped."
  [{:keys [server-id] :as config}]
  (when-not (@servers server-id)
    (let [s (server/start config)]
      (swap! servers assoc server-id s)
      [server-id :started])))

(defn stop!
  "Stop a server by its id."
  [server-id]
  (if-let [s (-> servers deref (get server-id))]
    (do
      (server/stop s)
      (swap! servers dissoc server-id)
      :stopped)
    (throw (ex-info "Server not found" {:server-id server-id}))))

(.addShutdownHook (Runtime/getRuntime)
                  (Thread.
                   ^Runnable
                   (fn []
                     (doseq [server (keys @servers)]
                       (stop! server)))))
