(ns yuhrao.rest.routes
  (:require [yuhrao.rest.midi-hub.routes :as mh.routes]))

(def root [["/api"
            mh.routes/root]])
