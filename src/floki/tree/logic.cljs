(ns floki.tree.logic
  (:require [quark.lang.collection :as coll]
            [quark.navigation.core :as nav]
            [reagent.core :as r]))

(defn fill
  [acc m]
  (if (map? m)
    (into acc [{:keys (-> m keys vec)}])
    acc))

(defn descs
  [input path]
  (loop [m   (nav/as-map-recursive input)
         [item & items] path
         acc []]
    (cond
      (not (map? m))
      acc

      (not item)
      (fill acc m)

      :else
      (let [ks     (-> m keys vec)
            index  (coll/first-index #(= item %) ks)
            next-m (get m item)
            res    {:keys ks :index index}]
        (recur next-m items (conj acc res))))))

(defn color
  [pos index]
  (if (= -1 (:pos/x pos))
    (case index 0 "green" 1 nil)
    (case index 0 "blue" 1 "green")))

(defn get-fn
  [coll index]
  (try
    (let [x     (->> coll (keep :index) count dec)
          coll' (->> coll (into [{:keys [:root] :index 0}]) (drop x))]
      (case index
        0 (first coll')
        1 (second coll')))
    (catch js/Error e
      (do (print e)
          {}))))

(defn pane-update
  [{:keys [descs index]} ref*]
  (let [selected-index (some-> descs (get-fn index) :index)]
    (when selected-index
      (some-> @ref* (.select selected-index)))))

(defn pane-viewmodel
  [{:keys [descs pos index]}]
  {:items (->> (get-fn descs index)
               :keys
               (map str))
   :style {:selected {:bg (color pos index)}}})
