(ns br.dev.yuhri.data-cloak.core.map
  (:require [br.dev.yuhri.data-cloak.map :as dc.impl.map]))

(defn obscure
  "Given an obscure-fns map and a map, obscure map fields
  `obscure-fns` is a field<->obscurer-function map.
  This function will match fields from `obscure-fn` and `m` and
  apply the respective function"
  [obscure-fns m]
  (dc.impl.map/obscure obscure-fns m))

(defn obscurer
  "Slightly different from `obscure` since it creates a function that
  receives a map and return it with obscured field"
  [obscure-fns]
  (dc.impl.map/obscurer obscure-fns))
