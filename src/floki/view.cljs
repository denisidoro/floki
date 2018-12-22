(ns floki.view
  (:require [re-frame.core :as rf]
            [core.print.core :as print]
            [reagent.core :as r]))

(defonce logger
  (r/atom []))

(defn clock
  []
  [:text
   {:left    0
    :top     0
    :height  2
    :width   50
    :content (-> @(rf/subscribe [:time])
                 .toTimeString
                 (clojure.string/split " ")
                 first)
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

(defn root [_]
  [:box#base {:left   0
              :right  0
              :width  "100%"
              :height "100%"}
   [:box {:bottom 11
          :label  "Box label"
          :border {:type :line}}
    [clock]]
   [debug-box {:height 10}]])
