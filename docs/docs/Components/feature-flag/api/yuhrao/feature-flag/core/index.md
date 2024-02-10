---
sidebar_label: core
title: yuhrao.feature-flag.core
toc_min_heading_level: 2
toc_max_heading_level: 4
custom_edit_url:
---





### client {#client}
``` clojure
(client opts)
```


Create an unleash client. You can provide any option described in [unleash doc](https://docs.getunleash.io/reference/sdks/java#configuration-options)
  Keep in mind that all keys must be kebab-case, all `Map<I, K>` must be conventional clojure maps. Values for fields that require specific classes (e.g. CustomHttpHeadersProviderImpl)
  must be provided via interop outside this function.

  Fields with specific treatment:
  - custom-http-headers: keys will be converted to camelCase strings. For each key, `.addCustomHttpHeader` will be called for the config builder
  - sync-on-init?: will be used for builder.synchronousFetchOnInitialisation(boolean)

  Unleash docs:
  - [Example Configuration](https://docs.getunleash.io/reference/sdks/java#example-configurations)
<p><sub><a href="https://github.com/yuhrao/big-bang/blob/main//src/yuhrao/feature_flag/core.clj#L6-L18">Source</a></sub></p>

### enabled? {#enabled-QMARK-}
``` clojure
(enabled? client flag-name)
(enabled? client flag-name context-map)
```


Get flag status. you can optionally send a context map.
  All standard keys in context-map must be kebab-key. They'll be converte do camelCase internally

  Unleash docs:
  - [Feature Toggle](https://docs.getunleash.io/reference/feature-toggles)
<p><sub><a href="https://github.com/yuhrao/big-bang/blob/main//src/yuhrao/feature_flag/core.clj#L20-L29">Source</a></sub></p>

### variant {#variant}
``` clojure
(variant client flag-name)
(variant client flag-name context-map)
```


Get flag variant. you can optionally send a context map.
  All standard keys in context-map must be kebab-key. They'll be converte do camelCase internally

  Unleash docs:
  - [Feature Toggle Variants](https://docs.getunleash.io/reference/feature-toggle-variants)
  - [Strategy Variants](https://docs.getunleash.io/reference/strategy-variants)
<p><sub><a href="https://github.com/yuhrao/big-bang/blob/main//src/yuhrao/feature_flag/core.clj#L31-L41">Source</a></sub></p>
