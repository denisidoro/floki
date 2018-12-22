(ns floki.events
  (:require [re-frame.core :as rf] ))

(rf/reg-event-db
  :initialize
  (fn [_ _]
    {:time (js/Date.)}))

(rf/reg-event-db
  :timer
  (fn [db [_ new-time]]
    (assoc db :time new-time)))
