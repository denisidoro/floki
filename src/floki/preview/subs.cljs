(ns floki.preview.subs
  (:require [re-frame.core :as rf]))

(rf/reg-sub
  :preview/data
  :<- [:tree/input]
  :<- [:tree/path]
  (fn [[input path]]
    (get-in input path)))
