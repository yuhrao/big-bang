(ns dev.unleash
  (:require
    [yuhrao.config.core :as config]
    [yuhrao.http-client.core :as http]
    [camel-snake-kebab.core :as csk]
    [camel-snake-kebab.extras :as cske]
    [clojure.string :as string]))

(def cfg (config/create {:local-host   {:default "http://localhost:4242"}
                         :remote-host  {:default "https://dev-yuhri-unleash.fly.dev"}
                         :local-token  {:default "*:*.2b675a0884f3b350e8acf211dda68ea441df6527658202eeb58ea98f"}
                         :remote-token {:env "UNLEASH_TOKEN"}}))

(def ^:private remote-client (http/client {:base-url (:remote-host cfg)
                                           :req      {:headers {:authorization (:remote-token cfg)}}}))
(def ^:private local-client (http/client {:base-url (:local-host cfg)
                                          :req      {:bearer-token (:local-token cfg)}}))

(defn- list-flags []
  (some->
    (http/request remote-client
                  {:method "get"
                   :path   "/api/admin/projects/default/features"})

    :body
    :features))

(defn- export-flags [flag-names]
  (some->
    (http/request remote-client
                  {:path   "/api/admin/features-batch/export"
                   :method :post
                   :body
                   {:environment  "development"
                    :downloadFile false
                    :features     flag-names}})
    :body))

(defn- get-session-token []
  (some->
    (http/request
      local-client
      {:path   "/auth/simple/login"
       :method :post
       :body   {:username "admin"
                :password "unleash4all"}})
    :headers
    :set-cookie
    (string/split #";")
    first
    string/trim))

(defn- import-flags [exported-flags]
  (let [session-cookie (get-session-token)
        payload        (cske/transform-keys csk/->camelCase {:project     "default"
                                                             :environment "development"
                                                             :data        exported-flags})]
    (http/request local-client
                  {:path    "/api/admin/features-batch/validate"
                   :method  :post
                   :headers {:cookie session-cookie}
                   :body    payload})
    (http/request local-client
                  {:path    "/api/admin/features-batch/import"
                   :method  :post
                   :headers {:cookie session-cookie}
                   :body    payload})))

(defn run-import []
  (->> (list-flags)
       (map :name)
       export-flags
       import-flags))
