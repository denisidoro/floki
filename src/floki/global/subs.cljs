(ns floki.global.subs
  (:require [re-frame.core :as rf]
            [floki.tree.subs]
            [floki.preview.subs]
            [floki.movement.subs]))

(rf/reg-sub
  :db
  (fn [db _]
    db))
