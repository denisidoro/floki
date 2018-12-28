(ns common.stdin
  (:require [process :as process]))

(def stdinput (atom ""))
(def stdin (.-stdin process))

(defn handler
  [callback]
  (.setEncoding stdin "utf8")
  (.on stdin "data"
       (fn [data]
         (if (= (str data) "q")
           (process/exit))
         (swap! stdinput #(str % data))))
  (.on stdin "end"
       (fn []
         (print "END EVENT!!!!")
         (swap! stdinput #(->> % count dec (subs % 0)))
         (callback @stdinput))))
