(ns floki.movement.logic
  (:require [common.logic :as l]
            [floki.tree.logic :as l.tree]))

(defn update-list
    [{:keys [tree/input tree/path pos/x pos/y] :as db}
     element-increase]
  (let [data (l.tree/descs input path)
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
    [{:keys [tree/input tree/path pos/x pos/y]}
     increase]
  (let [data (l.tree/descs input path)
        total-items (-> data (get x) count)
        res (+ y increase)]
    (and (not (neg? res))
         (<= res total-items))))

(defn horizontal-allowed?
  [{:keys [tree/input tree/path pos/x pos/y]}
   increase]
  (let [data (l.tree/descs input path)
        total-items (-> data (get y) count)
        res (+ x increase)]
    (and (not (neg? res))
         (<= res total-items))))
