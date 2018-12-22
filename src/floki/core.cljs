(ns floki.core
  (:require [cljs.nodejs :as nodejs]
            [reagent.core :as r]
            [re-frame.core :as rf]
            [blessed :as blessed]                           ; or use neo-blessed
            ["react-blessed" :as react-blessed]
            [floki.keys :as keys]
            [floki.subs]
            [floki.events]
            [floki.view :as view]))

(defonce screen
  (blessed/screen #js {:autoPadding true
                       :smartCSR    true
                       :title       "Hello react blessed"}))

(defonce render
  (react-blessed/createBlessedRenderer blessed))

(keys/setup screen)

(defn dispatch-timer-event
  []
  (let [now (js/Date.)]
    (rf/dispatch [:timer now])))

(defonce do-timer
  (js/setInterval dispatch-timer-event 1000))

(defn load []
  (-> (r/reactify-component view/root)
      (r/create-element #js {})
      (render screen)))

(defn -main []
  (rf/dispatch-sync [:initialize])
  (load))

(defn log-fn [& args]
  (swap! view/logger conj (clojure.string/join " " args)))

;; Hack to prevent figwheel, which prints to console.log, overwriting the "render"
(set! (.-log js/console) log-fn)

(re-frame.loggers/set-loggers! {:log log-fn, :warn log-fn})

(set! *main-cli-fn* -main)
