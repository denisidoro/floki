(ns floki.tree.logic
  (:require [quark.collection.seq :as coll.seq]
            [quark.collection.map :as coll.map]
            [clojure.string :as str]))

(defn ^:private calc-index
  [path path-seq]
  (let [n (-> path-seq first count)
        p (take n path)]
    (coll.seq/first-index #(= % p) path-seq)))

(defn ^:private calc-path-seqs
  [paths path]
  (loop [n   0
         acc []]
    (if (> n (count path))
      acc
      (let [p  (take n path)
            ps (filter #(and (-> % count dec (= n))
                             (->> % (take n) (= p))) paths)]
        (recur (inc n) (conj acc ps))))))

(defn merge-path-with-index
  [path-seq index]
  (coll.map/assoc-if {:keys (mapv last path-seq)} :index index))

(defn descs
  [paths path]
  (let [path-seqs (vec (calc-path-seqs paths path))
        indexes   (mapv (partial calc-index path) path-seqs)]
    (mapv merge-path-with-index path-seqs indexes)))

(defn bg-color
  [pos index]
  (if (= -1 (:pos/x pos))
    (case index 0 "green" nil)
    (case index 0 "blue" "green")))

(defn get-fn
  [coll index]
  (try
    (let [x     (->> coll (keep :index) count dec)
          coll' (->> coll (into [{:keys [:root] :index 0}]) (drop x) vec)]
      (get coll' index))
    (catch js/Error e
      (do (print e)
          {}))))

(defn pane-update
  [{:keys [selected-index]} ref*]
  (when selected-index
    (some-> @ref* (.select selected-index))))

(defn ^:private without-colon
  [x]
  (-> x
      str
      (str/replace ":" "")))

(defn as-item
  [x]
  (without-colon x))

(defn pane-viewmodel
  [descs pos index]
  (let [desc           (get-fn descs index)
        items          (->> desc :keys (mapv as-item))
        selected-index (:index desc)
        color          (bg-color pos index)
        style          {:selected {:bg color}}]
    {:items          items
     :selected-index selected-index
     :style          style}))
