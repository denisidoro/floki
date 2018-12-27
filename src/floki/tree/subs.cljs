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
  :tree/paths
  (fn [db _]
    (:tree/paths db)))

(rf/reg-sub
  :tree/descs
  :<- [:tree/paths]
  :<- [:tree/path]
  (fn [[paths path]]
    (l.tree/descs paths path)))
