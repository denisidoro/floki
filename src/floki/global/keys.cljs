(ns floki.global.keys
  (:require [re-frame.core :as rf]))

(def bindings
  {["escape" "q" "C-c"] #(.exit js/process 0)
   ["h" "left"]         #(rf/dispatch [:movement/left])
   ["j" "down"]         #(rf/dispatch [:movement/down])
   ["k" "up"]           #(rf/dispatch [:movement/up])
   ["l" "right"]        #(rf/dispatch [:movement/right])})

(defn setup
  [screen]
  (doseq [[hotkeys f] bindings]
    (.key screen (clj->js hotkeys) f)))

