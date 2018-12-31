(ns floki.core
  (:require [cljs.nodejs :as nodejs]
            [common.stdin :as stdin]
            [reagent.core :as r]
            [quark.conversion.data :as conversion]
            [re-frame.core :as rf]
            [blessed :as blessed]
            [fs :as fs]
            [tty :as tty]
            [common.stdin :as stdin]
            ["react-blessed" :as react-blessed]
            [floki.global.keys :as keys]
            [floki.global.subs]
            [floki.global.events]
            [floki.global.view :as view]
            [floki.debug.view :as v.debug]))

(defn log-fn [& args]
  (swap! v.debug/logger conj (clojure.string/join " " args)))

(defn error-input
  [& exceptions]
  {:error "Unable to parse JSON/EDN"
   :exceptions exceptions})

(defn convert
  [x]
  (try
    (conversion/edn-str->edn x)
    (catch js/Error e1
      (try
        (or (conversion/json->edn x) (error-input e1))
        (catch js/Error e2
          (error-input e1 e2))))))

(stdin/handler #(do (log-fn %)
                    (rf/dispatch [:input/set (convert %)])))

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

; (set! (.-log js/console) log-fn)

#_(re-frame.loggers/set-loggers! {:log  log-fn
                                :warn log-fn})

(set! *main-cli-fn* -main)
