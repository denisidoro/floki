(ns floki.tree.subs
  (:require [re-frame.core :as rf]
            [floki.tree.logic :as l.tree]))

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
    (l.tree/descs input path)))
