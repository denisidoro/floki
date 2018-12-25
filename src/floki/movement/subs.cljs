(ns floki.movement.subs
  (:require [re-frame.core :as rf]))

(rf/reg-sub
  :movement/pos
  (fn [db _]
    (select-keys db [:pos/x :pos/y])))

