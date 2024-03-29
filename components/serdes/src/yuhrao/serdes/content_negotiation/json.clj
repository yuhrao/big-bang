(ns yuhrao.serdes.content-negotiation.json
  (:require [camel-snake-kebab.core :as csk]
            [clojure.data.json :as json]
            [clojure.string :as string]
            [muuntaja.format.core :as mtj.core]
            [tick.core :as tick])
  (:import (java.io InputStream
                    InputStreamReader
                    OutputStream
                    Writer)))

(def mime-type "application/json")

(defn encoder-key-fn [k]
  (cond
    (number? k) (str k)
    (keyword? k) (name k)
    :else k))

(def temporal?
  (some-fn tick/date? tick/date-time? tick/duration? tick/instant? tick/zoned-date-time?))

(defn- value-writer [_k v]
  (cond
    (temporal? v) (tick/format v)
    :else v))

(defn clj->json [x _opts]
  (json/write-str x :key-fn encoder-key-fn :value-fn value-writer))

(defn json-stream->clj [reader {:keys [key-fn]
                                :or   {key-fn csk/->kebab-case-keyword}}]
  (json/read reader
             :key-fn key-fn))

(defn json->clj [json-str {:keys [key-fn]
                           :or   {key-fn csk/->kebab-case-keyword}}]
  (json/read-str json-str :key-fn key-fn))

(defn write [x ^Writer w]
  (json/write x w :key-fn csk/->kebab-case-string))

(defn- decoder [opts]
  (reify
    mtj.core/Decode
    (decode [_ data charset]
      (if (string? data)
        (when (not (string/blank? data))
          (json->clj data opts))
        (let [data ^InputStream data]
          (when (> (.available data) 0)
            (json-stream->clj (InputStreamReader.
                                data
                                ^String charset)
                              opts)))))))

(defn- encoder [opts]
  (reify
    mtj.core/EncodeToBytes
    (encode-to-bytes [_ data charset]
      (.getBytes ^String (clj->json data opts) ^String charset))

    mtj.core/EncodeToOutputStream
    (encode-to-output-stream [_ data _charset]
      (fn [^OutputStream output-stream]
        (write data output-stream)))))

(def json-format
  (mtj.core/map->Format
    {:name    mime-type
     :decoder [decoder]
     :encoder [encoder]}))
