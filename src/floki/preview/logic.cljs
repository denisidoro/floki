(ns floki.preview.logic
  (:require [common.print :as print]
            [clojure.string :as str]
            [quark.navigation.core :as nav]
            [node-json-color-stringify]
            [quark.conversion.data :as conversion]))

(def preview-content
  print/cstr)

(defn ^:private without-first-and-last-chars
  [s]
  (subs s 1 (-> s count dec)))

(defn replace-numbers-by-array-pos
  [s]
  (if (re-find #"^[0-9]+$" s)
    (str "[" s "]")
    s))

(defn path-viewmodel
  [format x]
  (if (= format :json)
    (as-> x it
          (str it)
          (without-first-and-last-chars it)
          (str/replace it ":" "")
          (str/split it " ")
          (map replace-numbers-by-array-pos it)
          (str/join "." it)
          (str "." it)
          (str/replace it ".[" "["))
    (-> x
        str
        without-first-and-last-chars)))

(defn preview-viewmodel
  [format input path]
  (let [data (if (seq path)
               (nav/navigate input path)
               input)]
    (if (= format :json)
      (-> data
          conversion/edn->json
          (js/JSON.parse)
          (js/JSON.colorStringify nil \t)
          (str/replace "[35m" "[33m")
          (str/replace "[39m" "[36m")
          (str/replace "[90m" "[32m"))
      (preview-content data))))
