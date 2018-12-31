(ns floki.tree.subs
  (:require [re-frame.core :as rf]
            [floki.tree.logic :as l.tree]
            [floki.tree.logic :as l]))

(rf/reg-sub
  :tree/input
  (fn [db _]
    (:tree/input db)))

(rf/reg-sub
  :tree/format
  (fn [db _]
    (:tree/format db)))

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

(rf/reg-sub
  :tree/viewmodel
  :<- [:tree/descs]
  :<- [:movement/pos]
  (fn [[descs pos] [_ index]]
    (l/pane-viewmodel descs pos index)))
