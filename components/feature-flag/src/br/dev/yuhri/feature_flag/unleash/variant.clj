(ns br.dev.yuhri.feature-flag.unleash.variant
  (:require [br.dev.yuhri.feature-flag.unleash.utils :as unleash.utils]
            [camel-snake-kebab.core :as csk]
            [clojure.data.json :as json])
  (:import (java.util Optional)
           (io.getunleash Unleash UnleashContext Variant)
           (io.getunleash.variant Payload)))

(defn- parse-payload-value [payload-type v]
  (cond
    (= payload-type :json) (json/read-str v :key-fn csk/->kebab-case-keyword)
    :else v))

(defn- parse-payload [^Optional payload]
  (when (.isPresent payload)
    (let [payload      ^Payload (.get payload)
          payload-type (csk/->kebab-case-keyword (.getType payload))
          value        (.getValue payload)]
      (when value
        (parse-payload-value payload-type value)))))

(defn- parse-variant [^Variant variant]
  (let [payload (parse-payload (.getPayload variant))]
    (cond->
      {:name     (csk/->kebab-case-keyword (.getName variant))
       :enabled? (.isEnabled variant)}
      payload (assoc :payload payload))))

(defn variant
  ([{:keys [^Unleash instance]} flag-name]
   (parse-variant (.getVariant instance flag-name)))
  ([^Unleash {:keys [^Unleash instance]} flag-name context-map]
   (parse-variant
     (.getVariant instance
                  ^String flag-name
                  ^UnleashContext (unleash.utils/build-context context-map)))))
