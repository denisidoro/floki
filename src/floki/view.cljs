(ns floki.view
  (:require [re-frame.core :as rf]
            [common.print.core :as print]
            [reagent.core :as r]
            [clojure.string :as str]
            [clojure.pprint :as pprint]))

(defonce logger
         (r/atom []))

(defn clock
  []
  [:text
   {:left    0
    :top     0
    :height  2
    :width   50
    :content (-> @(rf/subscribe [:extract])
                 ;print/pprint-str
                 pprint/pprint)
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
           :content (print/pprint-str @(rf/subscribe [:db]))}]
   [log-box (dec height)]])

(defn temp2-inner
  []
  (let [ref* (atom nil)
        update (fn [com]
                 (some-> @ref* (.select (-> com r/props :extract first :index))))]
    (r/create-class
      {:component-did-update
       update

       :reagent-render
       (fn []
         [:list
          {:ref (fn [ref] (reset! ref* ref))
           :items      (->>
                         @(rf/subscribe [:root-keys])
                         (mapcat :keys)
                         (map str))
           :selectedBg "green"
           }
          ])})))

(defn temp2-outer
  []
  [temp2-inner {:extract @(rf/subscribe [:extract]) }])

(defn temp3-inner
  []
  (let [ref* (atom nil)
        update (fn [com]
                 (some-> @ref* (.select (-> com r/props :extract second :index))))]
    (r/create-class
      {:component-did-update
       update

       :reagent-render
       (fn []
         [:list
          {:ref (fn [ref] (reset! ref* ref))
           :items      (->>
                         @(rf/subscribe [:secondary-keys])
                         (mapcat :keys)
                         (map str))
           :selectedBg "green"
           }
          ])})))

(defn temp3-outer
  []
  [temp3-inner {:extract @(rf/subscribe [:extract]) }])

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
    [temp2-outer]]
   [:box {:bottom 11
          :left   "20%"
          :width  "20%"
          :label  "Middle box"
          :border {:type :line}}
    [temp3-outer]]
   [:box {:bottom 11
          :right  0
          :width  "60%"
          :label  "Right box"
          :border {:type :line}}
    [clock]]
   [debug-box {:height 10}]])
