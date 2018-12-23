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
    (l/extract input list)))
