(ns floki.preview.subs
  (:require [re-frame.core :as rf]
            [floki.tree.logic :as l.tree]
            [common.logic :as l]))

(rf/reg-sub
  :preview/data
  :<- [:tree/input]
  :<- [:tree/path]
  (fn [[input path]]
    (l/generic-get-in input path)))
