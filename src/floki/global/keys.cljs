(ns floki.global.keys
  (:require [re-frame.core :as rf]))

(def bindings
  {["escape" "q" "C-c"] #(.exit js/process 0)
   ["g"]                #(rf/dispatch [:movement/first])
   ["S-g"]              #(rf/dispatch [:movement/last])
   ["h" "left" "C-a"]   #(rf/dispatch [:movement/left])
   ["j" "down" "C-n"]   #(rf/dispatch [:movement/down])
   ["k" "up" "C-p"]     #(rf/dispatch [:movement/up])
   ["l" "right" "C-e"]  #(rf/dispatch [:movement/right])})

(defn setup
  [screen]
  (doseq [[hotkeys f] bindings]
    (.key screen (clj->js hotkeys) f)))

