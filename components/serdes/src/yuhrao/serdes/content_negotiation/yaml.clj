(ns yuhrao.serdes.content-negotiation.yaml
  (:require [camel-snake-kebab.core :as csk]
            [clj-yaml.core :as yaml]
            [clojure.string :as string]
            [muuntaja.format.core :as core]
            [medley.core :as medley])
  (:import (java.io OutputStream OutputStreamWriter InputStream)
           (org.yaml.snakeyaml Yaml)
           (flatland.ordered.map OrderedMap)))

(defn- ordered-map->map [m]
  (->> m
       (map (fn [[k v]]
              [(csk/->kebab-case-keyword k)

               (cond
                 (instance? OrderedMap v)
                 (ordered-map->map v)

                 (and (coll? v) (not (map? v)))
                 (map ordered-map->map v)

                 :else
                 v)]))
       (into {})
       (medley/remove-vals #(and (string? %) (string/blank? %)))))

(defn decoder [{:keys [unsafe mark keywords] :or {keywords true}}]
  (reify
    core/Decode
    (decode [_ data _]
      (-> (.load (yaml/make-yaml
                   :dumper-options {:flow-style :block})
                 ^InputStream data)
          (yaml/decode keywords)
          ordered-map->map))))

(defn encoder [options]
  (let [options-args (mapcat identity options)]
    (reify
      core/EncodeToBytes
      (encode-to-bytes [_ data _]
        (.getBytes
          ^String (apply yaml/generate-string data :dumper-options {:flow-style :block} options-args)))
      core/EncodeToOutputStream
      (encode-to-output-stream [_ data _]
        (fn [^OutputStream output-stream]
          (.dump ^Yaml (apply yaml/make-yaml :dumper-options {:flow-style :block} options-args) (yaml/encode data) (OutputStreamWriter. output-stream))
          (.flush output-stream))))))

(def yaml-format
  (core/map->Format
    {:name    "application/x-yaml"
     :decoder [decoder {:keywords true}]
     :encoder [encoder]}))
