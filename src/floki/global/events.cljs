(ns floki.global.events
  (:require [re-frame.core :as rf]
            [floki.movement.events]))

(def input
  {:a0 1
   :a {:b {:c {:d {:e 54
                   :hi "hello"}
               :phillip 44}
           :mary 42}
       :john 44}
   :x [{:foo 0} {:bar 1}]
   :y #{{:foo 0} {:bar 1}}
   :z 45})

(rf/reg-event-db
  :init
  (fn [_ _]
    {:tree/input input
     :pos/x -1
     :pos/y 0
     :tree/path  []}))
