(ns floki.preview.view
  (:require [re-frame.core :as rf]
            [common.print :as print]
            [floki.preview.logic :as l]))

(defn preview
  []
  [:text
   {:left    0
    :top     0
    :width   50
    :content (-> @(rf/subscribe [:preview/data])
                 l/preview-content)}])

(defn path-box
  []
  [:text#debug {:bottom 0
                :left   0
                :width  "100%"
                :style  {:border  {:fg :yellow}}
                :border {:type :line}
                :shrink true
                :label  "Path"}
   [:text {:width   "40%"
           :content (-> @(rf/subscribe [:tree/path])
                        l/path-content)}]])
