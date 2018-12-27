(ns floki.tree.view
  (:require [re-frame.core :as rf]
            [floki.tree.logic :as l]
            [reagent.core :as r]))

(defn color
  [pos index]
  (if (= -1 (:pos/x pos))
    (case index 0 "green" 1 nil)
    (case index 0 "blue" 1 "green")))

(defn list-native-pane
  []
  (let [ref*   (atom nil)
        update #(l/pane-update (some-> % r/props) ref*)]
    (r/create-class
      {:component-did-update
       update

       :reagent-render
       (fn []
         (let [{:keys [items style]} (-> (r/current-component) r/props l/pane-viewmodel)]
           [:list
            {:ref   (fn [ref] (reset! ref* ref))
             :items items
             :style style}]))})))

(defn list-pane
  [index]
  [list-native-pane {:descs @(rf/subscribe [:tree/descs])
                     :pos   @(rf/subscribe [:movement/pos])
                     :index index}])
