(ns br.dev.yuhri.webserver.serdes
  (:require [clojure.data.json :as json]
            [camel-snake-kebab.core :as csk]
            [camel-snake-kebab.extras :as cske]
            [muuntaja.core :as mtj]
            [muuntaja.format.core :as mtj.core])
  (:import (java.io InputStreamReader
                    InputStream
                    Writer
                    OutputStream)))

(defn encoder-key-fn [k]
  (if (number? k)
    (str k)
    (csk/->kebab-case-string k)))

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

(defn- extract-content-type [request]
  (let [headers (some->> request
                         :headers
                         (cske/transform-keys csk/->kebab-case-keyword))]
    (or
      (:content-type request)
      (:content-type headers)
      "application/json")))

(defn muuntaja []
  (-> mtj/default-options
      (assoc-in [:formats "application/json"]
                (mtj.core/map->Format
                  {:name    "application/json"
                   :decoder [decoder]
                   :encoder [encoder]}))
      (assoc-in [:http :extract-content-type] extract-content-type)
      mtj/create))
