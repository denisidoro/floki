(ns floki.events
  (:require [re-frame.core :as rf] ))

(def input
  {:a {:b 1 :c {:d 42}}})

(rf/reg-event-db
  :initialize
  (fn [_ _]
    {:time (js/Date.)
     :input input}))

(rf/reg-event-db
  :count-up
  (fn [db _]
    (update db :count inc)))
