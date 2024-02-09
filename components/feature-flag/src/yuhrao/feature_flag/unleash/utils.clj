(ns yuhrao.feature-flag.unleash.utils
  (:require [camel-snake-kebab.core :as csk])
  (:import (io.getunleash UnleashContext UnleashContext$Builder)))

(defn- add-additional-properties [^UnleashContext$Builder builder context-map]
  (let [context-map (dissoc context-map
                            :user-id
                            :app-name
                            :environment
                            :remote-address
                            :current-time)]
    (when (not (empty? context-map))
      (for [[k v] context-map]
        (.addProperty builder (csk/->camelCaseString k) v))))
  builder)

(defn build-context [{:keys [user-id
                              app-name
                              environment
                              remote-address
                              current-time]
                       :as   context-map}]
  (cond-> (UnleashContext/builder)
          user-id (.userId user-id)
          app-name (.appName app-name)
          environment (.environment environment)
          remote-address (.remoteAddress remote-address)
          current-time (.currentTime current-time)
          true (add-additional-properties context-map)
          true (.build)))
