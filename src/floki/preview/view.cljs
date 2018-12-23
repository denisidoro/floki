(ns floki.preview.view
  (:require [re-frame.core :as rf]))

(defn preview
  []
  [:text
   {:left    0
    :top     0
    :height  2
    :width   50
    :content (-> @(rf/subscribe [:preview/data])
                 ;print/pprint-str
                 str
                 )
    }])
