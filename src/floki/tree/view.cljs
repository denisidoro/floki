(ns floki.tree.view
  (:require [re-frame.core :as rf]
            [reagent.core :as r]))

(defn list-native-pane
  []
  (let [ref* (atom nil)
        index (-> (r/current-component) r/props :index)
        get-fn #(try
                    (let [x (->> % (keep :index) count dec)
                           coll (drop x %)]
                         (case index
                           0 (first coll)
                           1 (second coll)))
                         (catch js/Error e
                           (do (print e)
                               {})))
        update (fn [com]
                 (when-let [selected-index (some-> com r/props :descs get-fn :index)]
                  (some-> @ref* (.select selected-index))))]
    (r/create-class
      {:component-did-update
       update

       :reagent-render
       (fn []
         [:list
          {:ref        (fn [ref] (reset! ref* ref))
           :items      (->> (r/current-component)
                             r/props
                             :descs
                             get-fn
                             :keys
                             (map str))
           :selectedBg "green"
           }
          ])})))

(defn list-pane
  [index]
  [list-native-pane {:descs @(rf/subscribe [:tree/descs])
                :index        index}])
