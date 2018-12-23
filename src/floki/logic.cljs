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

(defn descs
  [input path]
  (loop [m input
         [item & items] path
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

(defn update-list
    [{:keys [tree/input tree/path pos/x pos/y] :as db}
     element-increase]
  (let [data (descs input path)
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
  (let [data (descs input path)
        total-items (-> data (get x) count)
        res (+ y increase)]
    (and (not (neg? res))
         (<= res total-items))))

(defn horizontal-allowed?
  [{:keys [tree/input tree/path pos/x pos/y]}
   increase]
  (let [data (descs input path)
        total-items (-> data (get y) count)
        res (+ x increase)]
    (and (not (neg? res))
         (<= res total-items))))
