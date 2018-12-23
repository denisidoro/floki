(ns floki.tree.view
  (:require [re-frame.core :as rf]
            [reagent.core :as r]))

(defn with-item
  [{:pos/keys [x]} index items]
  (conj items x index (= index x)))

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
            {:ref        (fn [ref] (reset! ref* ref))
             :items      (->> descs
                              get-fn
                              :keys
                              (map str))
             :selectedBg (if (= (:pos/x pos) index) "green" "blue")
             }
            ]))})))

(defn list-pane
  [index]
  [list-native-pane {:descs @(rf/subscribe [:tree/descs])
                     :pos @(rf/subscribe [:movement/pos])
                :index        index}])
