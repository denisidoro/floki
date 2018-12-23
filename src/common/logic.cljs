(ns common.logic)

(defn find-first
  [pred coll]
  (first (filter pred coll)))

(defn first-index
  [pred coll]
  (find-first number?
    (map-indexed (fn [item index]
                   (and (pred index) item))
                 coll)))
