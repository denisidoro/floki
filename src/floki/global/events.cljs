(ns floki.global.events
  (:require [re-frame.core :as rf]
            [floki.movement.events]))

(def input
  {:a {:b {:c {:d {:e 53
                   :hi "hello"}
               :phillip 44}
           :mary 42}
       :john 44}
   :x 42
   :y 45})

(rf/reg-event-db
  :init
  (fn [_ _]
    {:tree/input input
     :pos/x 0
     :pos/y 0
     :tree/path  [:a]}))
