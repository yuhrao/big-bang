(ns br.dev.yuhri.feature-flag.unleash.flags
  (:require [br.dev.yuhri.feature-flag.unleash.utils :as unleash.utils])
  (:import (io.getunleash Unleash UnleashContext)))

(defn enabled?
  ([{:keys [^Unleash instance]} flag-name]
   (.isEnabled instance flag-name))
  ([^Unleash {:keys [^Unleash instance]} flag-name context-map]
   (.isEnabled instance
               ^String flag-name
               ^UnleashContext (unleash.utils/build-context context-map))))
