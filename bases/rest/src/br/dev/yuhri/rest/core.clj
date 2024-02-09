(ns yuhrao.rest.core
  (:require [yuhrao.webserver.core :as webserver]
            [yuhrao.rest.routes :as routes]))

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

