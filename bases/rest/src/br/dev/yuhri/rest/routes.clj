(ns br.dev.yuhri.rest.routes
  (:require [br.dev.yuhri.rest.midi-hub.routes :as mh.routes]))

(def root [["/api"
            mh.routes/root]])
