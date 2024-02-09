(ns yuhrao.feature-flag.unleash.client
  (:require [camel-snake-kebab.core :as csk])
  (:import (io.getunleash.util UnleashConfig UnleashConfig$Builder)
           (io.getunleash DefaultUnleash FakeUnleash)
           (io.getunleash.strategy Strategy)
           (java.net URI)))

(defn- add-custom-headers [^UnleashConfig$Builder builder headers]
  (for [[k v] headers]
    (.customHttpHeader builder (csk/->camelCaseString k) v))
  builder)

(defn- parse-config [{:keys [api-key
                             app-name
                             unleash-api
                             custom-http-headers
                             custom-http-headers-provider
                             disable-pooling?
                             disable-metrics?
                             enable-proxy-authentication-by-jvm-properties?
                             environment
                             fallback-strategy
                             fetch-toggles-interval
                             instance-id
                             name-prefix
                             project-name
                             send-metrics-interval
                             sync-on-init?] :as _opts}]
  (let [builder (UnleashConfig/builder)]
    (cond-> builder
            api-key (.apiKey api-key)
            app-name (.appName app-name)
            (string? unleash-api) (.unleashAPI ^String unleash-api)
            (instance? URI unleash-api) (.unleashAPI ^URI unleash-api)
            (not (empty? custom-http-headers)) (add-custom-headers custom-http-headers)
            custom-http-headers-provider (.customHttpHeadersProvider custom-http-headers)
            disable-pooling? (.disablePolling)
            disable-metrics? (.disableMetrics)
            enable-proxy-authentication-by-jvm-properties? (.enableProxyAuthenticationByJvmProperties)
            environment (.environment environment)
            fallback-strategy (.fallbackStrategy ^Strategy fallback-strategy)
            fetch-toggles-interval (.fetchTogglesInterval (long fetch-toggles-interval))
            instance-id (.instanceId instance-id)
            name-prefix (.namePrefix name-prefix)
            project-name (.projectName project-name)
            send-metrics-interval (.sendMetricsInterval (long send-metrics-interval))
            sync-on-init? (.synchronousFetchOnInitialisation sync-on-init?)
            true (.build))))

(defn- create-instance [{unleash-type :type
                         :as opts
                         :or          {unleash-type :default}}]
  (condp = unleash-type
    :default (let [config (parse-config opts)]
               (DefaultUnleash. config nil))
    :fake (FakeUnleash.)))

(defn unleash [opts]
  (let [instance (create-instance opts)]
    {:instance instance}))
