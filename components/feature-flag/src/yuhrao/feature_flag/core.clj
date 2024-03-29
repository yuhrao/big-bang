(ns yuhrao.feature-flag.core
  (:require [yuhrao.feature-flag.unleash.client :as unleash.client]
            [yuhrao.feature-flag.unleash.flags :as unleash.flags]
            [yuhrao.feature-flag.unleash.variant :as unleash.variant]))

(defn client
  "Create an unleash client. You can provide any option described in [unleash doc](https://docs.getunleash.io/reference/sdks/java#configuration-options)
  Keep in mind that all keys must be kebab-case, all `Map<I, K>` must be conventional clojure maps. Values for fields that require specific classes (e.g. CustomHttpHeadersProviderImpl)
  must be provided via interop outside this function.

  Fields with specific treatment:
  - custom-http-headers: keys will be converted to camelCase strings. For each key, `.addCustomHttpHeader` will be called for the config builder
  - sync-on-init?: will be used for builder.synchronousFetchOnInitialisation(boolean)

  Unleash docs:
  - [Example Configuration](https://docs.getunleash.io/reference/sdks/java#example-configurations)"
  [opts]
  (unleash.client/unleash opts))

(defn enabled?
  "Get flag status. you can optionally send a context map.
  All standard keys in context-map must be kebab-key. They'll be converte do camelCase internally

  Unleash docs:
  - [Feature Toggle](https://docs.getunleash.io/reference/feature-toggles)"
  ([client flag-name]
   (unleash.flags/enabled? client flag-name))
  ([client flag-name context-map]
   (unleash.flags/enabled? client flag-name context-map)))

(defn variant
  "Get flag variant. you can optionally send a context map.
  All standard keys in context-map must be kebab-key. They'll be converte do camelCase internally

  Unleash docs:
  - [Feature Toggle Variants](https://docs.getunleash.io/reference/feature-toggle-variants)
  - [Strategy Variants](https://docs.getunleash.io/reference/strategy-variants)"
  ([client flag-name]
   (unleash.variant/variant client flag-name))
  ([client flag-name context-map]
   (unleash.variant/variant client flag-name context-map)))

(comment

  (def c (client {:app-name "test"
                  :api-key "*:development.6b0c432c74902cceaab1e79888572e59941da50ee39cd868b8d0ff72"
                  :unleash-api "https://dev-yuhri-unleash.fly.dev/api/"
                  :fetch-toggles-interval 2}))


  (variant c "love-playground_history-menu")

  )
