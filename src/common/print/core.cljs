; This code is heavily inspired by planck's source code
; Planck is a software copyrighted © 2015–2017 Mike Fikes and Contributors
; and distributed under the Eclipse Public License either version 1.0 or (at your option) any later version.
; 
; For the original source, please refer to 
; https://github.com/mfikes/planck/blob/master/planck-cljs/src/planck/pprint/data.cljs

(ns core.print.core
  "Pretty printing for data."
  (:refer-clojure :exclude [lift-ns])
  (:require [clojure.string :as string]
            [fipp.engine :refer [pprint-document]]
            [fipp.visit :refer [visit visit*]]
            [goog.object :as gobj]
            [core.print.themes :as themes]
            [cljs.pprint :as cljprint]))

(defn pretty-coll [{:keys [print-level print-length] :as printer}
                   open xs sep close f]
  (let [printer  (cond-> printer print-level (update :print-level dec))
        xform    (comp (if print-length (take print-length) identity)
                   (map #(f printer %))
                   (interpose sep))
        ys       (if (pos? (or print-level 1))
                   (sequence xform xs)
                   "#")
        ellipsis (when (and print-length (seq (drop print-length xs)))
                   [:span sep "..."])]
    [:group open [:align ys ellipsis] close]))

(defn wrap-theme
  [kw theme text]
  [:span [:pass (kw theme)] [:text text] [:pass (:reset-font theme)]])

(defn demunge-macros-symbol
  [sym]
  (let [sym-ns (namespace sym)]
    (if (and (some? sym-ns)
             (string/ends-with? sym-ns "$macros"))
      (symbol (subs sym-ns 0 (- (count sym-ns) 7)) (name sym))
      sym)))

(defn- lift-ns
  "Returns [lifted-ns lifted-map] or nil if m can't be lifted."
  [print-namespace-maps m]
  (when print-namespace-maps
    (loop [ns nil
           [[k v :as entry] & entries] (seq m)
           lm (empty m)]
      (if entry
        (when (or (keyword? k) (symbol? k))
          (if ns
            (when (= ns (namespace k))
              (recur ns entries (assoc lm (strip-ns k) v)))
            (when-let [new-ns (namespace k)]
              (recur new-ns entries (assoc lm (strip-ns k) v)))))
        [ns lm]))))

(defrecord PlanckPrinter [symbols print-meta print-length print-level print-namespace-maps theme keyword-ns demunge-macros-symbols?]

  fipp.visit/IVisitor

  (visit-unknown [this x]
    (cond
      (instance? Eduction x)
      (visit this (sequence x))
      (array? x)
      (pretty-coll this "#js [" x :line "]" visit)
      (object? x)
      (let [kvs (map (fn [k]
                       [(cond-> k (some? (re-matches #"[A-Za-z_\*\+\?!\-'][\w\*\+\?!\-']*" k)) keyword)
                        (gobj/get x k)])
                  (js-keys x))]
        (pretty-coll this "#js {" kvs [:span "," :line] "}"
          (fn [printer [k v]]
            [:span (visit printer k) " " (visit printer v)])))
      :else
      [:text (binding [*print-meta* false] (pr-str x))]))

  (visit-nil [this]
    (wrap-theme :results-font theme "nil"))

  (visit-boolean [this x]
    (wrap-theme :results-font theme (str x)))

  (visit-string [this x]
    (wrap-theme :results-string-font theme (pr-str x)))

  (visit-character [this x]
    (wrap-theme :results-string-font theme (pr-str x)))

  (visit-symbol [this x]
    [:text (str (cond-> x demunge-macros-symbols? demunge-macros-symbol))])

  (visit-keyword [this x]
    (wrap-theme :results-keyword-font theme
      (if (and keyword-ns
               (= (namespace x) (str keyword-ns)))
        (str "::" (name x))
        (pr-str x))))

  (visit-number [this x]
    (wrap-theme :results-font theme (pr-str x)))

  (visit-seq [this x]
    (if-let [pretty (symbols (first x))]
      (pretty this x)
      (pretty-coll this "(" x :line ")" visit)))

  (visit-vector [this x]
    (pretty-coll this "[" x :line "]" visit))

  (visit-map [this x]
    (let [[ns lift-map] (lift-ns print-namespace-maps x)
          prefix (when (some? ns)
                   (str "#:" ns))]
      (pretty-coll this (str prefix "{") (or lift-map x) [:span "," :line] "}"
        (fn [printer [k v]]
          [:span (visit printer k) " " (visit printer v)]))))

  (visit-set [this x]
    (pretty-coll this "#{" x :line "}" visit))

  (visit-tagged [this {:keys [tag form]}]
    [:group "#" (pr-str tag)
     (when (or (and print-meta (meta form))
               (not (coll? form)))
       " ")
     (visit this form)])

  (visit-meta [this m x]
    (if print-meta
      [:align [:span "^" (visit this m)] :line (visit* this x)]
      (visit* this x)))

  (visit-var [this x]
    [:text (pr-str x)])

  (visit-pattern [this x]
    [:text (pr-str x)])

  (visit-record [this x]
    (pretty-coll this (str "#" (string/replace (pr-str (type x)) #"/" ".") "{") x [:span "," :line] "}"
      (fn [printer [k v]]
        [:span (visit printer k) " " (visit printer v)]))))

(defn pprint
  ([x] (pprint x {}))
  ([x options]
   (let [defaults        {:symbols              {}
                          :print-length         *print-length*
                          :print-level          *print-level*
                          :print-meta           *print-meta*
                          :print-namespace-maps *print-namespace-maps*
                          :theme                (themes/get-theme :light)
                          :pprint-document      fipp.engine/pprint-document}
         full-opts       (merge defaults options)
         pprint-document (:pprint-document full-opts)
         printer         (map->PlanckPrinter full-opts)]
     (pprint-document (visit printer x) full-opts))))

(defn pprint-str
  [& args]
  (with-out-str
    (apply pprint args)))

(def fast-print cljprint/pprint)
