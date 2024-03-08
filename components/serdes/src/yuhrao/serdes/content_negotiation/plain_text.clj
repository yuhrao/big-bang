(ns yuhrao.serdes.content-negotiation.plain-text
  (:require
   [clojure.java.io :as io]
   [muuntaja.format.core :as mtj.core])
  (:import
   (java.io InputStream OutputStream)))

(def mime-type "plain/text")

(defn- decoder [_opts]
  (reify
    mtj.core/Decode
    (decode [_ data _charset]
      (if (string? data)
        data
        (let [data ^InputStream data
              ]
          (with-open [reader (io/reader data)]
            (apply str (line-seq reader))))))))

(defn- encoder [opts]
  (reify
    mtj.core/EncodeToBytes
    (encode-to-bytes [_ data charset]
      (if (string? data)
        (.getBytes ^String data ^String charset)
        (.getBytes ^String (str data opts) ^String charset)))

    mtj.core/EncodeToOutputStream
    (encode-to-output-stream [_ data charset]
      (fn [^OutputStream output-stream]
        (let [data  ^String (if (not (string? data))
                             (str data)
                             data)
              bytes (.getBytes data charset)]
          (.write output-stream bytes))))))

(def plain-text-format
  (mtj.core/map->Format
   {:name    mime-type
    :decoder [decoder]
    :encoder [encoder]}))
