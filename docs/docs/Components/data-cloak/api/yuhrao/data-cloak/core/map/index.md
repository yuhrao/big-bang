---
sidebar_label: map
title: yuhrao.data-cloak.core.map
toc_min_heading_level: 2
toc_max_heading_level: 4
custom_edit_url:
---





### obscure {#obscure}
``` clojure
(obscure obscure-fns m)
```


Obscure the fields of a map using the given functions.
  The `obsucre-fns` is a map in which the keys are the fields to be obscured
  and the value is the function that will be used to obscure the field.

  Example:
  ```clojure
  (require '[yuhrao.data-cloak.core.string :as dc.string]
           '[yuhrao.data-cloak.core.map :as dc.map])
  (dc.map/obscure {:password dc.string/all} {:password "1234") => {:password "****")
  ```
  
<p><sub><a href="https://github.com/yuhrao/big-bang/blob/main//src/yuhrao/data_cloak/core/map.clj#L4-L17">Source</a></sub></p>

### obscurer {#obscurer}
``` clojure
(obscurer obscure-fns)
```


Slightly different from [`obscure`](#obscure) since it creates a function that
  receives a map and return it with obscured field


  Example:
  ```clojure
  (require '[yuhrao.data-cloak.core.string :as dc.string]
           '[yuhrao.data-cloak.core.map :as dc.map])
  (def obscurer (dc.map/obscurer {:password dc.string/all}))
  (obscurer {:password dc.string/all}) => {:password "****")
  ```

  
<p><sub><a href="https://github.com/yuhrao/big-bang/blob/main//src/yuhrao/data_cloak/core/map.clj#L19-L34">Source</a></sub></p>
