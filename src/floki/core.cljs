(ns floki.core
  (:require
   [cljs.nodejs :as nodejs]
   [reagent.core :as reagent]
   [re-frame.core :as rf]
   [blessed :as blessed]                                    ; or use neo-blessed
   ["react-blessed" :as rb]
    [core.print.core :as print]
   ))

(defonce logger (reagent/atom []))

(defonce screen (blessed/screen #js {:autoPadding true
                                     :smartCSR    true
                                     :title       "Hello react blessed"}))

(defonce render (rb/createBlessedRenderer blessed))

(.key screen #js ["escape" "q" "C-c"] #(.exit js/process 0))

(defn dispatch-timer-event
  []
  (let [now (js/Date.)]
    (rf/dispatch [:timer now])))

(defonce do-timer (js/setInterval dispatch-timer-event 1000))

;; events & subs below copied from one of the official re-frame examples

(rf/reg-event-db
 :initialize
 (fn [_ _]
   {:time (js/Date.)}))

(rf/reg-event-db
 :timer
 (fn [db [_ new-time]]
   (assoc db :time new-time)))

(rf/reg-sub
 :time
 (fn [db _]
   (:time db)))

(rf/reg-sub
 :db
 (fn [db _]
   db))

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

(defn example [_]
  [:box#base {:left   0
              :right  0
              :width  "100%"
              :height "100%"}
   [:box {:bottom 11
          :label  "Box label"
          :border {:type :line}}
    [clock]]
   [debug-box {:height 10}]])


(defn load []
  (-> (reagent/reactify-component example)
      (reagent/create-element #js {})
      (render screen)))

(defn -main []
  (rf/dispatch-sync [:initialize])
  (load))

(defn log-fn [& args]
  (swap! logger conj (clojure.string/join " " args)))

;; Hack to prevent figwheel, which prints to console.log, overwriting the "render"

(set! (.-log js/console) log-fn)

(re-frame.loggers/set-loggers! {:log log-fn, :warn log-fn})

(set! *main-cli-fn* -main)
