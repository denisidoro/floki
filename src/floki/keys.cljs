(ns floki.keys
  (:require [re-frame.core :as rf]))

(defn setup
  [screen]
  (.key screen #js ["escape" "q" "C-c"] #(.exit js/process 0))
  (.key screen #js ["j"] #(rf/dispatch [:movement/down]))
  (.key screen #js ["k"] #(rf/dispatch [:movement/up]))
  (.key screen #js ["l"] #(rf/dispatch [:movement/right]))
  (.key screen #js ["h"] #(rf/dispatch [:movement/left]))
  )

