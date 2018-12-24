(ns floki.tree.view
  (:require [re-frame.core :as rf]
            [reagent.core :as r]))

(defn color
  [pos descs index]
  (if (= -1 (:pos/x pos))
    (case index 0 "green" 1 nil)
    (case index 0 "blue" 1 "green")))

(defn with-item
  [pos descs index items]
  (conj items
        (->> descs (keep :index) count)
        (color pos descs index)))

(defn list-native-pane
  []
  (let [ref* (atom nil)
        index (-> (r/current-component) r/props :index)
        get-fn #(try
                    (let [x (->> % (keep :index) count dec)
                           coll (->> % (into [{:keys [:root] :index 0}]) (drop x))]
                         (case index
                           0 (first coll)
                           1 (second coll)))
                         (catch js/Error e
                           (do (print e)
                               {})))
        update (fn [com]
                 (let [selected-index (some-> com r/props :descs get-fn :index)]
                   ;(print ["sel" index selected-index])
                   (print (some-> com r/props :descs))
                   (when selected-index
                     (some-> @ref* (.select selected-index)))))]
    (r/create-class
      {:component-did-update
       update

       :reagent-render
       (fn []
         (let [{:keys [descs pos]} (-> (r/current-component) r/props)]
           ;(print ["pos" index pos])
           [:list
            {:ref   (fn [ref] (reset! ref* ref))
             :items (->> descs
                         get-fn
                         :keys
                         ;(with-item pos descs index)
                         (map str))
             :style {:selected {:bg (color pos descs index)}}
             }
            ]))})))

(defn list-pane
  [index]
  [list-native-pane {:descs @(rf/subscribe [:tree/descs])
                     :pos @(rf/subscribe [:movement/pos])
                :index        index}])
