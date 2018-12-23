(ns floki.preview.subs
  (:require [re-frame.core :as rf]
            [floki.tree.logic :as l.tree]))

(rf/reg-sub
  :preview/data
  :<- [:tree/input]
  :<- [:tree/path]
  (fn [[input path]]
    (get-in (l.tree/xablau input) path)))
