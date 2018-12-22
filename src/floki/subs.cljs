(ns floki.subs
  (:require [re-frame.core :as rf]))

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
