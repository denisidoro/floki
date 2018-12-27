(ns floki.preview.logic
  (:require [common.print :as print]))

(def preview-content
  print/cstr)

(defn ^:private without-first-and-last-chars
  [s]
  (subs s 1 (-> s count dec)))

(defn path-content
  [x]
  (-> x
      str
      without-first-and-last-chars))
