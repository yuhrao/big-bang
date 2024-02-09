(ns br.dev.yuhri.rest.midi-hub.routes
  (:require [br.dev.yuhri.rest.midi-hub.handlers :as mh.handlers]
            [br.dev.yuhri.rest.midi-hub.schema :as mh.schema]))

(def brands
  ["/brands"
   [""
    {:get mh.handlers/list-brands}]
   ["/:id"
    {:get {:parameters {:path mh.schema/get-by-id-params}
           :handler    mh.handlers/get-brand}}]])

(def devices
  ["/devices"
   [""
    {:get {:handler mh.handlers/list-devices}}]
   ["/:id"
    {:get {:parameters {:path mh.schema/get-by-id-params}
           :handler    mh.handlers/get-device}}]])

(def root ["/midi-hub"
           brands
           devices])
