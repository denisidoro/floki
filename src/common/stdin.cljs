(ns common.stdin
  (:require [process :as process]
            [clojure.string :as str]))

(def stdinput (atom ""))
(def stdin (.-stdin process))

(defn handler
  [callback]
  (.setEncoding stdin "utf8")
  (.on stdin "data"
       (fn [data]
         ; (print (str "DATA!!!!!!!" data))
         (if (= (str data) "q")
           (process/exit))
         (swap! stdinput #(str % data))))
  (.on stdin "end"
       (fn []
         ; (print "END EVENT!!!!")
         (swap! stdinput #(str/trim %))
         (callback @stdinput))))
