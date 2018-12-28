(ns floki.tree.view
  (:require [re-frame.core :as rf]
            [floki.tree.logic :as l]
            [reagent.core :as r]))

(defn list-native-pane
  []
  (let [ref*   (atom nil)
        update #(l/pane-update (some-> % r/props) ref*)]
    (r/create-class
      {:component-did-update
       update

       :reagent-render
       (fn []
         (let [com (r/current-component)
               {:keys [items style]} (r/props com)]
           [:list
            {:ref   (fn [ref] (reset! ref* ref))
             :items items
             :style style}]))})))

(defn list-pane
  [index]
  [list-native-pane @(rf/subscribe [:tree/viewmodel index])])

(defn left-pane
  []
  [:box {:bottom 3
         :left   0
         :width  "10%"
         :border {:type  :line
                  :right false
                  :fg    "blue"}}
   [list-pane 0]])

(defn right-pane
  []
  [:box {:bottom 3
         :left   "7%"
         :width  "14%"
         :border {:type :line
                  :left false
                  :fg   "blue"}}
   [list-pane 1]])
