(ns br.dev.yuhri.http-client.request
  (:require [babashka.http-client :as bb.http]
            [clojure.string :as string])
  (:import (clojure.lang ExceptionInfo)))

(defn- prepare-path [path]
  (if (string/starts-with? path "/")
    path
    (str "/" path)))


(defn- prepare-request [client {:keys [path]
                                :as   req}]
  (let [base-url (->> [client req]
                      (keep :base-url)
                      first)
        url      (str base-url (prepare-path path))]
    (merge
      (:req client)
      (-> req
          (dissoc :path)
          (assoc :interceptors (:interceptors client))
          (assoc :throw false)
          (assoc :uri url)))))

(defn- safe-request [req]
  (try
    (assoc (bb.http/request req) :success? true)
    (catch ExceptionInfo e
      (assoc (ex-data e)
        :success?
        false
        :exception {:message (ex-message e)
                    :cause   (ex-cause e)}))))

(defn execute [client req]
  (->> (prepare-request client req)
       safe-request))
