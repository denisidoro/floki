(ns floki.view
  (:require [re-frame.core :as rf]
            [reagent.core :as r]
            [clojure.string :as str]
            [clojure.pprint :as pprint]
            [floki.logic :as l]))

(defonce logger
         (r/atom []))

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

(defn log-box [n]
  [:text#log
   {:bottom     0
    :right      0
    :width      "50%"
    :height     n
    :style      {:fg :yellow :bg :grey}
    :scrollable true
    :content    (->> (take-last n @logger)
                     (clojure.string/join "\n"))}])

(defn debug-box [{:keys [height]}]
  [:text#debug {:bottom 0
                :left   0
                :width  "100%"
                :style  {:border {:fg :yellow}}
                :border {:type :line}
                :label  "Debug info"}
   [:text {:width   "40%"
           :content (str @(rf/subscribe [:db]))}]
   [log-box (dec height)]])

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

(defn root [_]
  [:box#base {:left   0
              :right  0
              :width  "100%"
              :height "100%"}
   [:box {:bottom 11
          :left   0
          :width  "20%"
          :label  "Left box"
          :border {:type :line}}
    [list-pane 0]]
   [:box {:bottom 11
          :left   "20%"
          :width  "20%"
          :label  "Middle box"
          :border {:type :line}}
    [list-pane 1]]
   [:box {:bottom 11
          :right  0
          :width  "60%"
          :label  "Right box"
          :border {:type :line}}
    [preview]]
   [debug-box {:height 10}]])
