(ns floki.core
  (:require [cljs.nodejs :as nodejs]
            [common.stdin :as stdin]
            [reagent.core :as r]
            [quark.conversion.data :as conversion]
            [re-frame.core :as rf]
            [blessed :as blessed]                           ; or use neo-blessed
            [fs :as fs]
            [tty :as tty]
            [common.stdin :as stdin]
            ["react-blessed" :as react-blessed]
            [floki.global.keys :as keys]
            [floki.global.subs]
            [floki.global.events]
            [floki.global.view :as view]
            [floki.debug.view :as v.debug]))

(def error-input
  {:error "Unable to parse JSON/EDN"})

(defn convert
  [x]
  (try
    (conversion/edn-str->edn x)
    (catch js/Error _
      (try
        (or (conversion/json->edn x) error-input)
        (catch js/Error _
          error-input)))))

(stdin/handler #(rf/dispatch [:input/set (convert %)]))

(defonce tty-fd
  (fs/openSync "/dev/tty" "r+"))

(defonce program
  (blessed/program #js {:input  (tty/ReadStream tty-fd)
                        :output (tty/WriteStream tty-fd)}))

(defonce screen
  (doto
    (blessed/screen #js {:program     program
                         :autoPadding true
                         :smartCSR    true
                         :title       "Hello react blessed"})
    keys/setup))

(defonce render
  (react-blessed/createBlessedRenderer blessed))

#_(defn get-input
    []
    (-> (.-argv js/process)
        last
        convert))

(defn load []
  (-> (r/reactify-component view/root)
      (r/create-element #js {})
      (render screen)))

(defn -main []
  (rf/dispatch-sync [:init])
  (load))

(defn log-fn [& args]
  (swap! v.debug/logger conj (clojure.string/join " " args)))

;; Hack to prevent figwheel, which prints to console.log, overwriting the "render"
(set! (.-log js/console) log-fn)

(re-frame.loggers/set-loggers! {:log  log-fn
                                :warn log-fn})

(set! *main-cli-fn* -main)
