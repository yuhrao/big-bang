(ns br.dev.yuhri.data-cloak.string
  (:require [clojure.string :as string]))

(defn offset [s {:keys [start end]
                 :or   {start 0
                        end   0}}]
  (if (string/blank? s)
    s
    (let [asterisks (repeat (- (count s) start end) "*")
          begining  (take start s)
          ending    (take-last end s)]
      (apply str
             (concat
               begining
               asterisks
               ending)))))

(defn symmetric-offset [s n]
  (offset s {:start n
             :end   n}))

(defn all [s]
  (symmetric-offset s 0))

(defn email [s]
  (if (string/blank? s)
    s
    (let [[local domain] (string/split s #"@")
          obscured (condp < (count local)
                     5 (symmetric-offset local 2)
                     1 (offset local {:start 1}))]
      (str obscured
           "@"
           domain))))

(defn phone [s]
  (if (string/blank? s)
    s
    (let [s (string/replace s #"[\(\)\+\s\-]" "")]
      (condp < (count s)
        12 (offset s {:start 5 :end 4})
        10 (offset s {:start 3 :end 4})
        (offset s {:end 4})))))
