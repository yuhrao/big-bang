(ns br.dev.yuhri.serdes.content-negotiation.json
  (:require [camel-snake-kebab.core :as csk]
            [clojure.data.json :as json]
            [muuntaja.format.core :as mtj.core])
  (:import (java.io InputStream
                    InputStreamReader
                    OutputStream
                    Writer)))

(defn encoder-key-fn [k]
  (cond
    (number? k) (str k)
    (keyword? k) (name k)
    :else k))

(defn clj->json [x]
  (json/write-str x :key-fn encoder-key-fn))

(defn json-stream->clj [reader]
  (json/read reader
             :key-fn csk/->kebab-case-keyword))

(defn json->clj [json-str]
  (json/read-str json-str :key-fn #(-> %
                                       keyword
                                       csk/->kebab-case-keyword)))

(defn write [x ^Writer w]
  (json/write x w :key-fn csk/->kebab-case-string))

(defn- decoder [_options]
  (reify
    mtj.core/Decode
    (decode [_ data charset]
      (json-stream->clj (InputStreamReader.
                          ^InputStream data
                          ^String charset)))))

(defn- encoder [_options]
  (reify
    mtj.core/EncodeToBytes
    (encode-to-bytes [_ data charset]
      (.getBytes ^String (clj->json data) ^String charset))

    mtj.core/EncodeToOutputStream
    (encode-to-output-stream [_ data _charset]
      (fn [^OutputStream output-stream]
        (write data output-stream)))))

(def json-format
  (mtj.core/map->Format
    {:name    "application/json"
     :decoder [decoder]
     :encoder [encoder]}))
