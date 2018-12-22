(ns floki.keys)

(defn setup
  [screen]
  (.key screen #js ["escape" "q" "C-c"] #(.exit js/process 0)))
