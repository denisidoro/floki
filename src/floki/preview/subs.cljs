(ns floki.preview.subs
  (:require [re-frame.core :as rf]
            [quark.navigation.core :as nav]
            [floki.preview.logic :as l]))

(rf/reg-sub
  :preview/preview-viewmodel
  :<- [:tree/format]
  :<- [:tree/input]
  :<- [:tree/path]
  (fn [[format input path]]
    (l/preview-viewmodel format input path) ))

(rf/reg-sub
  :preview/path-viewmodel
  :<- [:tree/format]
  :<- [:tree/path]
  (fn [[format path]]
    (l/path-viewmodel format path)))
