(ns floki.preview.view
  (:require [re-frame.core :as rf]
            [floki.preview.logic :as l]))

(defn preview
  []
  [:text
   {:left    0
    :top     0
    :width   50
    :content @(rf/subscribe [:preview/preview-viewmodel])}])

(defn preview-box
  []
  [:box {:bottom 3
         :right  0
         :width  "76%"
         :label  "Preview"
         :border {:type :line}}
   [preview]])

(defn path-box
  []
  [:text#debug {:bottom 0
                :left   0
                :width  "100%"
                :style  {:border {:fg :yellow}}
                :border {:type :line}
                :shrink true
                :label  "Path"}
   [:text {:width   "40%"
           :content @(rf/subscribe [:preview/path-viewmodel]) }]])
