(ns floki.movement.events
  (:require [re-frame.core :as rf]
            [floki.movement.logic :as l.movement]))

(rf/reg-event-db
  :movement/down
  (fn [db _]
    (if (l.movement/vertical-allowed? db 1)
      (-> db
          (update :pos/y inc)
          (l.movement/update-list 0))
      db)))

(rf/reg-event-db
  :movement/up
  (fn [db _]
    (if (l.movement/vertical-allowed? db -1)
      (-> db
          (update :pos/y dec)
          (l.movement/update-list 0))
      db)))

(rf/reg-event-db
  :movement/right
  (fn [db _]
    (if (l.movement/horizontal-allowed? db 1)
      (-> db
          (assoc :pos/y 0)
          (update :pos/x inc)
          (l.movement/update-list 1))
      db)))

(rf/reg-event-db
  :movement/first
  (fn [db _]
    (if (l.movement/jump-allowed? db)
      (-> db
          (assoc :pos/y 0)
          (l.movement/update-list 0))
      db)))

(rf/reg-event-db
  :movement/last
  (fn [db _]
    (if (l.movement/jump-allowed? db)
      (-> db
          (assoc :pos/y (l.movement/last-index db))
          (l.movement/update-list 0))
      db)))

(rf/reg-event-db
  :movement/left
  (fn [db _]
    (cond
      (<= (:pos/x db) 0)
      (assoc db
        :pos/x -1
        :tree/path [])

      (l.movement/horizontal-allowed? db -1)
      (-> db
          l.movement/with-previous-pos-y
          (update :pos/x dec)
          (l.movement/update-list -1))

      :else
      db)))
