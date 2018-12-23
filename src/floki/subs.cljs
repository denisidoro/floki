(ns floki.subs
  (:require [re-frame.core :as rf]
            [floki.logic :as l]))

(rf/reg-sub
  :time
  (fn [db _]
    (:time db)))

(rf/reg-sub
  :db
  (fn [db _]
    db))

(rf/reg-sub
  :pos
  (fn [{:keys [x y]} _]
    {:x x
     :y y}))

(rf/reg-sub
  :input
  (fn [db _]
    (:input db)))

(rf/reg-sub
  :list
  (fn [db _]
    (:list db)))

(rf/reg-sub
  :extract
  :<- [:input]
  :<- [:list]
  (fn [[input list]]
    (l/extract input list)))

(rf/reg-sub
  :root-keys
  :<- [:extract]
  (fn [extract]
    (take 1 extract)))

(rf/reg-sub
  :secondary-keys
  :<- [:extract]
  (fn [extract]
    (->> extract
         (drop 1)
         (take 1))))

(rf/reg-sub
  :preview
  :<- [:input]
  :<- [:list]
  (fn [[input list]]
    (get-in input list)))

(rf/reg-sub :y
            (fn [db]
              (:y db)))

(rf/reg-sub
  :preview2
  :<- [:input]
  :<- [:list]
  (fn [[input list]]
    :todo))
