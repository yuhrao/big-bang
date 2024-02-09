(ns br.dev.yuhri.rest.midi-hub-test
  (:require [br.dev.yuhri.rest.routes :as sut]
            [br.dev.yuhri.midi-hub.lib.db :as db]
            [br.dev.yuhri.webserver.core :as webserver]
            [br.dev.yuhri.serdes.core.json :as json]
            [clojure.test :as t]
            [matcher-combinators.test]
            [camel-snake-kebab.extras :as cske]
            [camel-snake-kebab.core :as csk]
            [matcher-combinators.matchers :as matcher]))
(defonce app (webserver/app {:routes sut/root
                             :disable-logs? true}))

(comment

  (->> (db/list-brands)
       (take 3)
       vec))

(defn- prepare-request [{:keys [body] :as req}]
  (cond-> req
    body (assoc :body (json/clj->json body))
    body (assoc-in [:headers "Content-Type"] "application/json")))

(def brands-sample
  [{:id "b767dd16-c782-498b-819a-58862fb2856a"
    :value "access"
    :brand_name "Access"
    :created_at "2024-01-02T21:27:20.190427"
    :updated_at "2024-01-02T21:27:20.190456"}
   {:id "8c7d2275-4f23-4be5-b883-77c4e6f75ff7"
    :value "aerospaceaudio"
    :brand_name "Aerospace Audio"
    :created_at "2024-01-02T21:27:20.206818"
    :updated_at "2024-01-02T21:27:20.206825"}
   {:id "adbcd303-bc4a-420c-8edd-1a640d57d862"
    :value "arturia"
    :brand_name "Arturia"
    :created_at "2024-01-02T21:27:20.208336"
    :updated_at "2024-01-02T21:27:20.208342"}])

(t/deftest list-brands
  (with-redefs [db/list-brands (constantly brands-sample)]
    (t/is (match? {:status 200
                   :body (cske/transform-keys csk/->kebab-case-keyword brands-sample)}
                  (update
                   (app {:request-method :get
                         :uri "/api/midi-hub/brands"})
                   :body
                   json/json->clj)))))

(t/deftest get-brand
  (let [sample (first brands-sample)]
    (with-redefs [db/list-brands (constantly [sample])]
      (t/is (match? {:status 200
                     :body (cske/transform-keys csk/->kebab-case-keyword sample)}
                    (update
                     (app {:request-method :get
                           :uri (str "/api/midi-hub/brands/" (:id sample))})
                     :body
                     json/json->clj)))))

  (with-redefs [db/list-brands (constantly [])]
    (t/is (match? {:status 404
                   :body matcher/absent}
                  (app {:request-method :get
                        :uri (str "/api/midi-hub/brands/" (random-uuid))}))))

  (with-redefs [db/list-brands (constantly [])]
    (t/is (match? {:status 400
                   :body (matcher/equals
                          {:value {:id "invaid-id"},
                           :type "request-coercion",
                           :coercion "malli",
                           :in ["request" "path-params"],
                           :humanized {:id ["should be a uuid"]}})}
                  (update (app {:request-method :get
                                :uri (str "/api/midi-hub/brands/invaid-id")})
                          :body
                          json/json->clj)))))
