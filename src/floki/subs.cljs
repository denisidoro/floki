(ns floki.subs
  (:require [re-frame.core :as rf]
            [floki.logic :as l]))

(rf/reg-sub
  :db
  (fn [db _]
    db))

(rf/reg-sub
  :tree/input
  (fn [db _]
    (:tree/input db)))

(rf/reg-sub
  :tree/path
  (fn [db _]
    (:tree/path db)))

(rf/reg-sub
  :tree/descs
  :<- [:tree/input]
  :<- [:tree/path]
  (fn [[input path]]
    (l/descs input path)))

(rf/reg-sub
  :preview/data
  :<- [:tree/input]
  :<- [:tree/path]
  (fn [[input path]]
    (get-in input path)))
