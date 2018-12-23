(ns floki.events
  (:require [re-frame.core :as rf]
            [floki.logic :as l]))

(def input
  {:a {:b 1 :c {:d 42} :john 44}
   :x 42
   :y 45})

(rf/reg-event-db
  :initialize
  (fn [_ _]
    {:time  (js/Date.)
     :input input
     :y 0
     :x 0
     :list  [:a]}))

(rf/reg-event-db
  :y-up
  (fn [db _]
    (update db :y inc)))

(rf/reg-event-db
  :movement/down
  (fn [db _]
    (-> db
        (update :y inc)
        (l/update-list 0))))

(rf/reg-event-db
  :movement/up
  (fn [db _]
    (-> db
        (update :y dec)
        (l/update-list 0))))

(rf/reg-event-db
  :movement/right
  (fn [db _]
    (-> db
        (assoc :y 0)
        (update :x inc)
        (l/update-list 1))))

(rf/reg-event-db
  :movement/left
  (fn [db _]
    (-> db
        (assoc :y 0)
        (update :x dec)
        (l/update-list -1))))

(rf/reg-event-db
  :timer
  (fn [db [_ time]]
    (assoc db :time time)))

(rf/reg-event-db
  :list-select
  (fn [db [_ item index]]
    (assoc db :list item)))
