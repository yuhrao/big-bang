(ns br.dev.yuhri.webserver.interface
  (:require [br.dev.yuhri.webserver.server :as server]))

(defonce servers (atom {}))

(defn app [config]
  (server/app config))

(defn start! [{:keys [server-id] :as config}]
  (when-not (@servers server-id)
    (let [s (server/start config)]
      (swap! servers assoc server-id s)
      [server-id :started])))

(defn stop! [server-id]
  (if-let [s (-> servers deref (get server-id))]
    (do
      (server/stop s)
      (swap! servers dissoc server-id)
      :stopped)
    (throw (ex-info "Server not found" {:server-id server-id}))))
