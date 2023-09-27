(ns br.dev.yuhri.http-client.request
  (:require [camel-snake-kebab.core :as csk]
            [camel-snake-kebab.extras :as cske]
            [clj-http.client :as http.client]
            [clojure.string :as string])
  (:import (clojure.lang ExceptionInfo)))

(defn- prepare-path [path]
  (if (string/starts-with? path "/")
    path
    (str "/" path)))

(defn- encode-request [{{encode :encode-fn} :content-negotiation} req]
  (encode req))

(defn- normalize-req-header [{:keys [headers] :as req}]
  (let [{:keys [content-type] :as headers}
        (cond-> req
                headers
                (update :headers
                        (partial cske/transform-keys csk/->HTTP-Header-Case-String)))]
    (if content-type
      headers
      (assoc headers :content-type "application/json"))))

(defn- normalize-res-header [{:keys [headers] :as res}]
  (cond-> res
          headers (update :headers
                          (partial cske/transform-keys csk/->kebab-case-keyword))))

(defn- prepare-request [client {:keys [path headers]
                                :as   req}]
  (let [base-url (->> [client req]
                      (map :base-url)
                      (remove nil?)
                      first)
        url      (format "%s%s" base-url (prepare-path path))
        body     (encode-request client req)]
    (cond-> req
            true (dissoc :path)
            true (assoc :url url)
            true (normalize-req-header)
            body (assoc :body body))))

(defn- handle-response [{{decode :decode-fn} :content-negotiation} res]
  (let [body (decode res)]
    (cond-> res
            body (assoc :body body)
            true normalize-res-header)))

(defn- safe-request [req]
  (try
    (assoc (http.client/request req) :success? true)
    (catch ExceptionInfo e
      (assoc (ex-data e)
        :success?
        false
        :exception {:message (ex-message e)
                    :cause   (ex-cause e)}))))

(defn execute [client req]
  (->> (prepare-request client req)
       safe-request
       (handle-response client)))
