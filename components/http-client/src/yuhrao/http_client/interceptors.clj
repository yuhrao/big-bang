(ns yuhrao.http-client.interceptors
  (:require
   [yuhrao.serdes.core.content-negotiation :as content-negotiation]
   [babashka.http-client.interceptors :as bb.interceptors]
   [babashka.http-client]
   [camel-snake-kebab.core :as csk]
   [camel-snake-kebab.extras :as cske])
  (:import (clojure.lang ExceptionInfo)))

(def format-headers
  {:name        ::format-headers
   :description "Handle headers to allow usage of keywords in :headers map"
   :request     (fn [req]
                  (update req :headers (partial cske/transform-keys csk/->HTTP-Header-Case-String)))
   :response    (fn [res]
                  (update res :headers (partial cske/transform-keys csk/->kebab-case-keyword)))})

(def ^:private mime-types content-negotiation/mime-types)

(def content-negotiation
  {:name        ::content-negotiation
   :description "Handle content negotiation through Muuntaja"
   :request     (fn [{:keys [content-type] :as req}]
                  (if (:body req)
                    (let [fmt (or
                               (mime-types content-type)
                               (content-negotiation/extract-content-type req "plain/text"))
                          req (if content-type
                                (assoc-in req [:headers "Content-Type"] fmt)
                                req)]
                      (try
                        (-> req
                            (assoc-in [:headers :content-type] fmt)
                            (update :body #(content-negotiation/encode fmt %)))
                        (catch ExceptionInfo e
                          (throw (ex-info "Failed to encode body"
                                          (assoc (ex-data e) :payload (:body req)))))))
                    req))
   :response    (fn [res]
                  (tap> res)
                  (if (:body res)
                    (let [fmt (content-negotiation/extract-content-type res "plain/text")]
                      (try
                        (update res :body #(content-negotiation/decode fmt %))
                        (catch ExceptionInfo e
                          (throw (ex-info "Failed to decode body"
                                          (assoc (ex-data e) :payload (:body res))
                                          (ex-cause e))))))
                    res))})

(def authorization
  {:name        ::authorization
   :description "Handle authorization header"
   :request     (fn [{:keys [bearer-token] :as req}]
                  (cond
                    bearer-token (assoc-in req [:headers :authorization] (format "Bearer %s" bearer-token))
                    :else req))})

(def default-interceptors [bb.interceptors/throw-on-exceptional-status-code
                           bb.interceptors/construct-uri
                           bb.interceptors/accept-header
                           bb.interceptors/basic-auth
                           bb.interceptors/query-params
                           bb.interceptors/form-params
                           bb.interceptors/multipart

                           format-headers
                           content-negotiation
                           authorization

                           bb.interceptors/decompress-body])
