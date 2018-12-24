(ns common.stdin)

(js/require "process")
(def stdinput (atom ""))
(def stdin (.-stdin js/process))

(defn handler
  [callback]
  (.setEncoding stdin "utf8")
  (.on stdin "data"
       (fn [data]
         (swap! stdinput #(str % data))))
  (.on stdin "end"
       (fn []
         (swap! stdinput #(->> % count dec (subs % 0)))
         (callback @stdinput))))
