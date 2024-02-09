(ns yuhrao.data-cloak.map)

(defn obscurer [obscure-fns]
  (fn [m]
    (if (seq obscure-fns)
      (->> m
           (map (fn [[k v]]
                  (if-let [obscure-fn (obscure-fns k)]
                    [k (obscure-fn v)]
                    [k v])))
           (into {}))
      m)))

(defn obscure [obscure-fns m]
  ((obscurer obscure-fns) m))
