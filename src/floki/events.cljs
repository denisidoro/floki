(ns floki.events
  (:require [re-frame.core :as rf]
            [floki.logic :as l]))

(def input
  {:a {:b 1 :c {:d {:e 53}} :john 44}
   :x 42
   :y 45})

(rf/reg-event-db
  :init
  (fn [_ _]
    {:tree/input input
     :pos/x 0
     :pos/y 0
     :tree/path  [:a]}))

(rf/reg-event-db
  :movement/down
  (fn [db _]
    (if (l/vertical-allowed? db 1)
      (-> db
                                  (update :pos/y inc)
                                  (l/update-list 0))
      db)))

(rf/reg-event-db
  :movement/up
  (fn [db _]
    (if (l/vertical-allowed? db -1)
      (-> db
                                  (update :pos/y dec)
                                  (l/update-list 0))
      db)))

(rf/reg-event-db
  :movement/right
  (fn [db _]
    (if (l/horizontal-allowed? db 1)
      (-> db
                                         (assoc :pos/y 0)
                                         (update :pos/x inc)
                                         (l/update-list 1))
      db)))

(rf/reg-event-db
  :movement/left
  (fn [db _]
    (if (l/horizontal-allowed? db -1)
      (-> db
                                          (assoc :pos/y 0)
                                          (update :pos/x dec)
                                          (l/update-list -1))
      db)))
