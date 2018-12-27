(ns floki.tree.logic
  (:require [quark.lang.collection :as coll]))

(defn ^:private calc-index
  [path path-seq]
  (let [n (-> path-seq first count)
        p (take n path)]
    (coll/first-index #(= % p) path-seq)))

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
  (coll/assoc-if {:keys (mapv last path-seq)} :index index))

(defn descs
  [paths path]
  (let [path-seqs (calc-path-seqs paths path)
        indexes   (map (partial calc-index path) path-seqs)]
    (mapv merge-path-with-index path-seqs indexes)))

(defn fg-color
  [pos index]
  (if (= -1 (:pos/x pos))
    (case index 0 "green" 1 nil)
    (case index 0 "blue" 1 "green")))

(defn get-fn
  [coll index]
  (try
    (let [x     (->> coll (keep :index) count dec)
          coll' (->> coll (into [{:keys [:root] :index 0}]) (drop x))]
      (case index
        0 (first coll')
        1 (second coll')))
    (catch js/Error e
      (do (print e)
          {}))))

(defn pane-update
  [{:keys [descs index]} ref*]
  (let [selected-index (some-> descs (get-fn index) :index)]
    (when selected-index
      (some-> @ref* (.select selected-index)))))

(defn pane-viewmodel
  [{:keys [descs pos index]}]
  {:items (->> (get-fn descs index)
               :keys
               (map str))
   :style {:selected {:bg (fg-color pos index)}}})
