(ns floki.global.view
  (:require [floki.tree.view :as v.tree]
            [floki.preview.view :as v.preview]
            [floki.debug.view :as v.debug]))

(defn root [_]
  [:box#base {:left   0
              :right  0
              :width  "100%"
              :height "100%"}
   [:box {:bottom 3
          :left   0
          :width  "20%"
          :border {:type :line
                   :right false
                   :fg "blue"}}
    [v.tree/list-pane 0]]
   [:box {:bottom 3
          :left   "15%"
          :width  "20%"
          :border {:type :line
                   :left false
                   :fg "blue"}}
    [v.tree/list-pane 1]]
   [:box {:bottom 3
          :right  0
          :width  "68%"
          :label  "Preview"
          :border {:type :line}}
   [v.preview/preview]]
   [v.preview/path-box]
   ;[v.debug/debug-box {:height 10}]
   ])
