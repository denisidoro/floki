(ns floki.subs
  (:require [re-frame.core :as rf]))

(defn find-first
  [pred coll]
  (first (filter pred coll)))

(defn first-index
  [pred coll]
  (first
    (map-indexed (fn [item index]
                   (and (pred index) item))
                 coll)))

(defn extract
  [input list]
  (loop [m input
         [item & items] list
         acc []]
    (if-not item
      acc
      (let [ks (keys m)
            index (first-index #(= item %) ks)
            next-m (get m item)
            res {:keys ks :index index}]
        (recur next-m items (conj acc res))))))

(rf/reg-sub
  :time
  (fn [db _]
    (:time db)))

(rf/reg-sub
  :db
  (fn [db _]
    db))

(rf/reg-sub
  :input
  (fn [db _]
    (:input db)))

(rf/reg-sub
  :list
  (fn [db _]
    (:list db)))

(rf/reg-sub
  :preview
  :<- [:input]
  :<- [:list]
  (fn [[input list]]
    (get-in input [:a])))

(rf/reg-sub :count
            (fn [db]
              (:count db)))

(rf/reg-sub
  :preview2
  :<- [:input]
  :<- [:list]
  (fn [[input list]]
    (extract input list)))
