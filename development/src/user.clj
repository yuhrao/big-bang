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

(defn start-emacs []
  (when (nil? @p-instance)
    (reset! p-instance (portal/open {:app false
                                     :port 3000}))
    (add-tap #'portal/submit))
  (str "http://localhost:3000/?" (:session-id @p-instance)))


(comment

  (dev.database/do-migrations)
  (start-emacs)

  )
