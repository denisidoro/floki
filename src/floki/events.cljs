(ns floki.events
  (:require [re-frame.core :as rf] ))

(def input
  {:a {:b 1 :c {:d 42}}})

(rf/reg-event-db
  :initialize
  (fn [_ _]
    {:time  (js/Date.)
     :input input
     :list  [:a]}))

(rf/reg-event-db
  :count-up
  (fn [db _]
    (update db :count inc)))

(rf/reg-event-db
  :movement/down
  (fn [db _]
    (update db :count inc)))

(rf/reg-event-db
  :movement/up
  (fn [db _]
    (update db :count dec)))

(rf/reg-event-db
  :timer
  (fn [db [_ time]]
    (assoc db :time time)))

(rf/reg-event-db
  :list-select
  (fn [db [_ item index]]
    (assoc db :list item)))
