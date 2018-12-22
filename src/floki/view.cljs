(ns floki.view
  (:require [re-frame.core :as rf]
            [common.print.core :as print]
            [reagent.core :as r]
            [clojure.string :as str]))

(defonce logger
  (r/atom []))

(defn clock
  []
  [:text
   {:left    0
    :top     0
    :height  2
    :width   50
    :content (-> {:a {:b 1 :c {:d 42}}}
                 (get-in [:a :b])
                 str)
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

(defn temp
  []
  [:list
   {:keys       true
    :mouse      true
    :vi         true
    :items      (->> @(rf/subscribe [:input])
                     :a
                     keys
                     ;(mapv print/pprint-str)
                     )
    :selectedBg "green"
    :onAction   (fn [item index] (rf/dispatch [:list-select (-> (.getContent item) keyword) index]))}
   ])

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
    [temp]]
   [:box {:bottom 11
          :right  0
          :width  "30%"
          :label  "Right box"
          :border {:type :line}}
    [clock]]
   [debug-box {:height 10}]])
