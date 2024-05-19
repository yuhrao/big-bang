(ns yuhrao.webserver.middlewares.obscurer
  (:require [yuhrao.data-cloak.core.map :as dc.map]
            [camel-snake-kebab.core :as csk]
            [camel-snake-kebab.extras :as cske]))

(defn- create-obscurer-for [obscurers k]
  (if-let [obscure-fns (obscurers k)]
    (dc.map/obscurer (merge obscure-fns
                            (cske/transform-keys csk/->HTTP-Header-Case-String obscure-fns)))
    identity))

(def obscurer-middleware
  {:name    ::obscurer
   :compile (fn [{:keys [obscurers]} _opts]
              (if (and (map? obscurers) (seq obscurers))
                (let [body-obscurer   (create-obscurer-for obscurers :body)
                      header-obscurer (create-obscurer-for obscurers :headers)]
                  {:wrap (fn [handler]
                           (fn [req]
                             (let [{:keys [body headers] :as res} (handler req)]
                               (cond-> res
                                 body (update :body body-obscurer)
                                 headers (update :headers header-obscurer)))))})
                {:wrap (fn [handler]
                         (fn [req]
                           (handler req)))}))})
