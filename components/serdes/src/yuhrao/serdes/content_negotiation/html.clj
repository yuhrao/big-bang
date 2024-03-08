(ns yuhrao.serdes.content-negotiation.html
  (:require [hiccup2.core :as hiccup]
            [hiccup.util]
            [muuntaja.format.core :as mtj.core])
  (:import (java.io OutputStream Writer)))

(def mime-type "text/html")

(defn clj->html-str [v]
  (str (hiccup/html v)))

(defn- encoder [_]
  (reify
    mtj.core/EncodeToBytes
    (encode-to-bytes [_ data charset]
      (let [data (cond
                   (string? data)                         data
                   (instance? hiccup.util.RawString data) (str data)
                   :else                                  (clj->html-str data))]
        (.getBytes ^String data ^String charset)))

    mtj.core/EncodeToOutputStream
    (encode-to-output-stream [_ data _charset]
      (fn [^OutputStream output-stream]
        (let [writer ^Writer (java.io.OutputStreamWriter. output-stream)
              data   (cond
                       (string? data)                         data
                       (instance? hiccup.util.RawString data) (str data)
                       :else                                  (clj->html-str data))]
          (.write writer data)
          (.flush writer))))))

(defn decoder [_]
  (reify
    mtj.core/Decode
    (decode [_ _ _]
      nil)))

(def html-format
  (mtj.core/map->Format
   {:name    mime-type
    :decoder [decoder]
    :encoder [encoder]}))
