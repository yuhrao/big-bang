(ns yuhrao.serdes.content-negotiation.html
  (:require [hiccup2.core :as hiccup]
            [muuntaja.format.core :as mtj.core])
  (:import (java.io OutputStream OutputStreamWriter Writer)))

(defn clj->html-str [v]
  (str (hiccup/html v)))

(defn- encoder [_]
  (reify
    mtj.core/EncodeToBytes
    (encode-to-bytes [_ data charset]
      (.getBytes ^String (clj->html-str data) ^String charset))

    mtj.core/EncodeToOutputStream
    (encode-to-output-stream [_ data _charset]
      (fn [^OutputStream output-stream]
        (let [writer ^Writer (java.io.OutputStreamWriter. output-stream)]
          (.write writer (clj->html-str data))
          (.flush writer))))))

(defn decoder [{:keys [unsafe mark keywords] :or {keywords true}}]
  (reify
    mtj.core/Decode
    (decode [_ data _]
      (throw (ex-info "Not implemented" {}))
      nil)))

(def html-format
  (mtj.core/map->Format
   {:name    "application/x-yaml"
    :decoder [decoder]
    :encoder [encoder]}))
