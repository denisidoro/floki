(ns floki.global.logic)

(defn custom-compare
  [& args]
  (try
    (apply compare args)
    (catch js/Error _
      (apply compare (mapv str args)))))
