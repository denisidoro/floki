(ns floki.logic)

(defn find-first
  [pred coll]
  (first (filter pred coll)))

(defn first-index
  [pred coll]
  (find-first number?
    (map-indexed (fn [item index]
                   (and (pred index) item))
                 coll)))

(defn fill
  [acc m]
  (if (map? m)
    (into acc [{:keys (-> m keys vec)}])
    acc))

(defn extract
  [input list]
  (loop [m input
         [item & items] list
         acc []]
    (cond
      (not (map? m))
      acc

      (not item)
      (fill acc m)

      :else
      (let [ks (-> m keys vec)
            index (first-index #(= item %) ks)
            next-m (get m item)
            res {:keys ks :index index}]
        (recur next-m items (conj acc res))))))

(def extract2 extract)

#_(defn extract2
    [input list]
  (into [{:keys (keys input)}] (extract input list)))

(defn update-list
    [{:keys [input x y list] :as db}
     element-increase]
  (let [data (extract2 input list)
        get-fn #(get % x)
        ks (-> data get-fn :keys vec)
        item (get ks y)
        crop #(case element-increase
                -1 (drop-last 2 %)
                0 (drop-last %)
                1 %)
        new-list (-> list crop vec (conj item))]
    (assoc db :list new-list)))

(defn vertical-allowed?
    [{:keys [input list x y]}
     increase]
  (let [data (extract2 input list)
        total-items (-> data (get x) count)
        res (+ y increase)]
    (and (pos? res)
         (< res total-items))))