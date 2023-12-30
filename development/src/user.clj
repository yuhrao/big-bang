(ns user
  (:require
    [portal.api :as portal]
    [dev.database]
    [dev.unleash]))

(defonce p-instance (atom nil))

(defn start-intellij []
  (when (nil? @p-instance)
    (reset! p-instance (portal/open {:launcher :intellij}))
    (add-tap #'portal/submit)))
