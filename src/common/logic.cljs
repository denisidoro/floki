(ns common.logic
  (:require [clojure.walk :as walk]))

(defn find-first
  [pred coll]
  (first (filter pred coll)))

(defn first-index
  [pred coll]
  (find-first number?
    (map-indexed (fn [item index]
                   (and (pred index) item))
                 coll)))

(defn as-map
  [x]
  (if (map? x)
    x
    (->> x
           vec
           (map-indexed (fn [index item] [index item]))
           (into {}))))

(defn as-map2
  [x]
  (let [started?* (atom false)]
    (walk/prewalk
      #(let [res (if (and @started?*
                          (or (vector? %)
                              (set? %)
                              (list? %))
                          (not (map-entry? %)))
                   (as-map %)
                   %)]
         (reset! started?* true)
         res)
      x)))

(defn generic-get-in
  [m path]
  (loop [m' m
         [x & xs] path]
    (if-not x
      m'
      (let [m'' (if-not (map? m')
                  (as-map m')
                  m')
            next-m (get m'' x)]
        (recur next-m xs)))))