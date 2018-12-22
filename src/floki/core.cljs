(ns floki.core
  (:require [cljs.nodejs :as nodejs]
            [reagent.core :as reagent]
            [re-frame.core :as rf]
            [blessed :as blessed]                           ; or use neo-blessed
            ["react-blessed" :as rb]
            [floki.subs]
            [floki.events]
            [floki.view :as view]))

(defonce logger
  (reagent/atom []))

(defonce screen
  (blessed/screen #js {:autoPadding true
                       :smartCSR    true
                       :title       "Hello react blessed"}))

(defonce render
  (rb/createBlessedRenderer blessed))

(.key screen #js ["escape" "q" "C-c"] #(.exit js/process 0))

(defn dispatch-timer-event
  []
  (let [now (js/Date.)]
    (rf/dispatch [:timer now])))

(defonce do-timer
  (js/setInterval dispatch-timer-event 1000))

(defn load []
  (-> (reagent/reactify-component view/root)
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
