(ns floki.preview.events
  (:require [re-frame.core :as rf]
            [clipboardy :as clip]
            [floki.preview.logic :as l]))

(rf/reg-event-db
  :preview/copy-path
  (fn [{:tree/keys [format path] :as db}]
    (-> (l/path-viewmodel format path)
        (clip/writeSync))
    db))

