(ns yuhrao.data-cloak.core.map
  (:require [yuhrao.data-cloak.map :as dc.impl.map]))

(defn obscure
  "Obscure the fields of a map using the given functions.
  The `obsucre-fns` is a map in which the keys are the fields to be obscured
  and the value is the function that will be used to obscure the field.

  Example:
  ```clojure
  (require '[yuhrao.data-cloak.core.string :as dc.string]
           '[yuhrao.data-cloak.core.map :as dc.map])
  (dc.map/obscure {:password dc.string/all} {:password \"1234\") => {:password \"****\")
  ```
  "
  [obscure-fns m]
  (dc.impl.map/obscure obscure-fns m))

(defn obscurer
  "Slightly different from `obscure` since it creates a function that
  receives a map and return it with obscured field


  Example:
  ```clojure
  (require '[yuhrao.data-cloak.core.string :as dc.string]
           '[yuhrao.data-cloak.core.map :as dc.map])
  (def obscurer (dc.map/obscurer {:password dc.string/all}))
  (obscurer {:password dc.string/all}) => {:password \"****\")
  ```

  "
  [obscure-fns]
  (dc.impl.map/obscurer obscure-fns))
