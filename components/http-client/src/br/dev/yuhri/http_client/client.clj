(ns br.dev.yuhri.http-client.client
  (:require [clojure.string :as string]
            [br.dev.yuhri.http-client.interceptors :as http.interceptors]))

(defn- remove-trailing-slash [url]
  (if (string/ends-with? url "/")
    (->> url
         seq
         drop-last
         (apply str))
    url))

(defn client
  "Create a new HTTP Client. Opts:
  - :base-url: Base URL for all requests
  - :interceptors: a list of babashka.http-client interceptors. These interceptors will be placed after default interceptors
  - :req: a request map to be passed to all requests"
  [{:keys [base-url
           interceptors]
    :as   opts}]
  (cond-> (assoc opts
            :interceptors http.interceptors/default-interceptors)
          base-url (update :base-url remove-trailing-slash)
          interceptors (update :interceptors #(concat % interceptors))))
