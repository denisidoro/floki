(ns floki.global.view
  (:require [floki.tree.view :as v.tree]
            [floki.preview.view :as v.preview]
            [floki.debug.view :as v.debug]))

(defn root [_]
  [:box#base {:left   0
              :right  0
              :width  "100%"
              :height "100%"}
   [v.tree/left-pane]
   [v.tree/right-pane]
   [v.preview/preview-box]
   [v.preview/path-box]
   ;[v.debug/debug-box {:height 10}]
   ])
