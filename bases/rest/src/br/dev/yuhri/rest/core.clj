(ns br.dev.yuhri.rest.core
  (:require [br.dev.yuhri.webserver.core :as webserver]
            [br.dev.yuhri.rest.routes :as routes]))

(def server-id :big-bang)

(defn start! [opts]
  (webserver/start! (merge
                     opts
                     {:routes routes/root
                      :server-id server-id})))

(defn stop! []
  (webserver/stop! server-id))

(comment
  (do
    (stop!)
    (start! {:port          3030
             :host          "0.0.0.0"
             :server-id     :big-bang
             :join?         false
             :disable-logs? true})
    )

  )

