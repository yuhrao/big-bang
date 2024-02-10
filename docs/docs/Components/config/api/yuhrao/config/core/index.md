---
sidebar_label: core
title: yuhrao.config.core
toc_min_heading_level: 2
toc_max_heading_level: 4
custom_edit_url:
---





### create {#create}
``` clojure
(create opts)
```


Creates a configuration map from options.
  Example:
  ```clojure
  (config/create {:my-env  {:env "PWD"}
                  :my-prop {:prop "java.vm.version"}
                  :both    {:env  "PWD"
                            :prop "java.version"}})
  ;; => {:my-env  "/my/path"
  ;;     :my-prop "12.1"
  ;;     :both    "12.1"}
  ```

  Config parameters
  - `:env` - Get value from a
  - `:prop` - Get value from a java system property
  - `:default` - Default value if no value is provided
  - `:parse-fn` - Funtion to parse the raw value.

  Notes:
  - `:prop` takes priority over `:env` if both are provided.
  - `:parse-fn` is applied to both `:env` and `:prop` values.
  
<p><sub><a href="https://github.com/yuhrao/big-bang/blob/main//src/yuhrao/config/core.clj#L16-L43">Source</a></sub></p>
