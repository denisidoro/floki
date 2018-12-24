(ns floki.preview.view
  (:require [re-frame.core :as rf]
            [common.print :as print]))

(defn preview
  []
  [:text
   {:left    0
    :top     0
    :width   50
    :content (-> @(rf/subscribe [:preview/data])
                 print/cstr)}])

(defn without-first-and-last-chars
  [s]
  (subs s 1 (-> s count dec)))

(defn path-box
  []
  [:text#debug {:bottom 0
                :left   0
                :width  "100%"
                :style  {:border {:fg :yellow}}
                :border {:type :line}
                :label  "Path"}
   [:text {:width   "40%"
           :content (-> @(rf/subscribe [:tree/path])
                         str
                        without-first-and-last-chars)}]])
