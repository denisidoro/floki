(ns floki.tree.logic
  (:require [common.logic :as l]))

(defn fill
  [acc m]
  (if (map? m)
    (into acc [{:keys (-> m keys vec)}])
    acc))

(defn descs
  [input path]
  (loop [m (l/as-map2 input)
         [item & items] path
         acc []]
    (cond
      (not (map? m))
      acc

      (not item)
      (fill acc m)

      :else
      (let [ks (-> m keys vec)
            index (l/first-index #(= item %) ks)
            next-m (get m item)
            res {:keys ks :index index}]
        (recur next-m items (conj acc res))))))
