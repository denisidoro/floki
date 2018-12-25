(ns floki.preview.subs
  (:require [re-frame.core :as rf]
            [quark.navigation.core :as nav]))

(rf/reg-sub
  :preview/data
  :<- [:tree/input]
  :<- [:tree/path]
  (fn [[input path]]
    (if (seq path)
      (nav/navigate input path)
      input)))
