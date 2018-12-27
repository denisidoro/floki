(ns floki.global.events
  (:require [re-frame.core :as rf]
            [quark.navigation.core :as nav]
            [floki.movement.events]))

(def default-input
  {:error "Unable to parse JSON/EDN"})

(rf/reg-event-db
  :init
  (fn [_ [_ input]]
    (let [input' (or input default-input)]
      {:tree/input input'
       :tree/paths (nav/paths input)
       :pos/x      -1
       :pos/y      0
       :tree/path  []})))
