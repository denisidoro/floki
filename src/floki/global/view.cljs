(ns floki.global.view
  (:require [floki.tree.view :as v.tree]
            [floki.preview.view :as v.preview]
            [floki.debug.view :as v.debug]))

(defn root [_]
  [:box#base {:left   0
              :right  0
              :width  "100%"
              :height "100%"}
   [:box {:bottom 11
          :left   0
          :width  "20%"
          :label  "Left box"
          :border {:type :line}}
    [v.tree/list-pane 0]]
   [:box {:bottom 11
          :left   "20%"
          :width  "20%"
          :label  "Middle box"
          :border {:type :line}}
    [v.tree/list-pane 1]]
   [:box {:bottom 11
          :right  0
          :width  "60%"
          :label  "Right box"
          :border {:type :line}}
    [v.preview/preview]]
   [v.debug/debug-box {:height 10}]])
