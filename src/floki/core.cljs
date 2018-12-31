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
  (let [res (try
              (conversion/edn-str->edn x)
              (catch js/Error e1
                (try
                  (conversion/json->edn x)
                  (catch js/Error e2
                    (error-input e1 e2)))))]
    (if false
      (error-input "Empty document")
      res)))


(defn get-filename
  []
  (->> (.-argv js/process)
       (drop 2)
      last))

(defn read-file-handler
  [err buf]
  (rf/dispatch [:input/set (-> buf str convert)]))

(defn stdin-handler
  [buf]
  (rf/dispatch [:input/set (convert buf)]))

(defonce filename (get-filename))

(defonce callback
         (if filename
           (fs/readFile filename read-file-handler)
           (stdin/handler stdin-handler)))

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
                         :title       "Floki"})
    keys/setup))

(defonce render
  (react-blessed/createBlessedRenderer blessed))


(defn load []
  (-> (r/reactify-component view/root)
      (r/create-element #js {})
      (render screen)))

(defn -main []
  (rf/dispatch-sync [:init])
  (load))

(set! (.-log js/console) log-fn)

(re-frame.loggers/set-loggers! {:log  log-fn
                                :warn log-fn})
(set! *main-cli-fn* -main)
