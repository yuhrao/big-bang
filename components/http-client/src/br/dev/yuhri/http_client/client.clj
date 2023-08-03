(ns br.dev.yuhri.http-client.client
  (:require [br.dev.yuhri.serdes.core.content-negotiation :as content-negotiation]
            [clojure.string :as string]))

(defn- remove-trailing-slash [url]
  (if (string/ends-with? url "/")
    (->> url
         seq
         drop-last
         (apply str))
    url))

(defn default-encode-fn [{:keys [muuntaja]} {:keys [body]
                                             :as req}]
  (let [fmt (content-negotiation/extract-content-type req)]
    (when body
      (content-negotiation/encode muuntaja fmt body))))

(defn default-decode-fn [{:keys [muuntaja]} {:keys [body]
        :as req}]
  (let [fmt (content-negotiation/extract-content-type req)]
    (when body
      (content-negotiation/decode muuntaja fmt body))))

(defn- include-content-negotiation [{{:keys [encode-fn
                                             decode-fn]}
                                     :content-negotiation
                                     :as opts}]
  (let [content-negotiation {:encode-fn (partial (or encode-fn default-encode-fn) opts)
                             :decode-fn (partial (or decode-fn default-decode-fn) opts)}]
    (assoc opts :content-negotiation content-negotiation)))

(defn client [{:keys [muuntaja
                      base-url]
               :or   {muuntaja (content-negotiation/muuntaja)}
               :as   opts}]
  (cond-> opts
          true (assoc :muuntaja muuntaja)
          base-url (update :base-url remove-trailing-slash)
          true (include-content-negotiation)))
