(ns floki.preview.subs
  (:require [re-frame.core :as rf]
            [common.logic :as l]))

(rf/reg-sub
  :preview/data
  :<- [:tree/input]
  :<- [:tree/path]
  (fn [[input path]]
    (if (seq path)
      (l/generic-get-in input path)
      input)))
