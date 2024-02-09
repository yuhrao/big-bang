(ns br.dev.yuhri.rest.midi-hub.handlers
  (:require [br.dev.yuhri.midi-hub.core :as mh]))

(defn list-brands [_]
  {:status :ok
   :body   (mh/list-brands)})

(defn get-brand [{{{:keys [id]} :path} :parameters}]
  (if-let [brand (first (mh/list-brands {:where [[:= :id id]]}))]
    {:status :ok
     :body   brand}
    {:status :not-found}))

(defn list-devices [_]
  {:status :ok
   :body   (mh/list-devices)})

(defn get-device [{{{:keys [id]} :path} :parameters}]
  (if-let [device (first (mh/list-devices {:where [[:= :id id]]}))]
    {:status :ok
     :body   device}
    {:status :not-found}))

(comment
  (-> (mh/list-devices)
      first
      tap>)


  )
