(ns floki.movement.logic
  (:require [floki.tree.logic :as l.tree]))

(defn update-list
    [{:keys [tree/paths tree/path pos/x pos/y] :as db}
     element-increase]
  (let [data (l.tree/descs paths path)
        get-fn #(get % x)
        ks (-> data get-fn :keys vec)
        item (get ks y)
        crop #(case element-increase
                -1 (drop-last 2 %)
                0 (drop-last %)
                1 %)
        new-path (-> path crop vec (conj item))]
    (if item
      (assoc db :tree/path new-path)
      (update db :pos/x dec element-increase))))

(defn vertical-allowed?
    [{:keys [tree/paths tree/path pos/x pos/y]}
     increase]
  (let [data (l.tree/descs paths path)
        total-items (-> data (get x) :keys count)
        res (+ y increase)]
    (and (not (neg? res))
         (< res total-items))))

(defn horizontal-allowed?
  [{:keys [tree/paths tree/path pos/x]}
   increase]
  (let [data (l.tree/descs paths path)
        total-items (-> data (get (+ increase x)) :keys count)]
    (pos? total-items)))

(defn jump-allowed?
  [{:keys [pos/x]}]
  (not (neg? x)))

(defn last-index
  [{:keys [tree/paths tree/path]}]
  (let [descs (l.tree/descs paths path)]
    (->> descs
         (filter :index)
         last
         :keys
         count
         dec)))

(defn with-previous-pos-y
  [{:keys [tree/paths tree/path] :as db}]
  (let [descs (l.tree/descs paths path)
        previous-index (->> descs
                            (keep :index)
                            drop-last
                            last)]
    (assoc db :pos/y previous-index)))
