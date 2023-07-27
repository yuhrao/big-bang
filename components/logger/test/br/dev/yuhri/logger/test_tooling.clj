(ns br.dev.yuhri.logger.test-tooling
  (:require [com.brunobonacci.mulog.buffer :as rb]
            [com.brunobonacci.mulog.core]
            [clojure.core.async :as a]))

(defonce ^:private in-memory-messages (atom {}))

(defn- update-messages! [new-messages old-messages]
  (concat old-messages new-messages))

(deftype MemoryPublisher [config buffer]
  com.brunobonacci.mulog.publisher.PPublisher
  (agent-buffer [_]
    buffer)

  (publish-delay [_]
    10)

  (publish [_ *buffer]
    (let [messages     (map second (rb/items *buffer))
          transform-fn (or (:transform config) identity)
          messages     (transform-fn messages)]
      (when (not (empty? messages))
        (swap! in-memory-messages
               update
               (:test-id config)
               (partial update-messages! messages))))
    (rb/clear *buffer)))

(defn- create-memory-publisher
  [config]
  (MemoryPublisher. config (rb/agent-buffer 100)))

(defn memory-publisher
  [test-id]
  (when (empty? (-> in-memory-messages
                    deref
                    (get test-id)))
    (swap! in-memory-messages #(assoc % test-id [])))
  {:type         :custom
   :fqn-function create-memory-publisher
   :test-id      test-id})

(defn retrieve-messages [test-id]
  (-> in-memory-messages
      deref
      (get test-id)))

(defn clear-messages [test-id]
  (swap! in-memory-messages assoc test-id [])
  nil)

(defn remove-messages []
  (reset! in-memory-messages {}))

(defn wait-for-logs
  ([test-id log-count]
   (wait-for-logs test-id log-count 3000))
  ([test-id log-count ^long timeout]
   (let [result-chan  (a/chan 1)
         timeout-chan (a/timeout timeout)]
     (a/go-loop []
       (a/put! timeout-chan true)
       (if (<= log-count (-> test-id
                             retrieve-messages
                             count))
         (a/>! result-chan true)
         (if (a/poll! timeout-chan)
           (recur)
           (a/>! result-chan false))))
     (a/<!! result-chan))))
