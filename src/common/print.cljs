(ns common.print
  (:require [zprint.core :as zprint]
            [clojure.string :as str]))

(def default-options
  {:color-map
   {:brace              :magenta
    :bracket            :purple
    :comment            :green
    :deref              :red
    :fn                 :blue
    :hash-brace         :red
    :hash-paren         :green
    :keyword            :cyan
    :nil                :yellow
    :none               :yellow
    :number             :green
    :paren              :red
    :syntax-quote-paren :red
    :quote              :red
    :string             :yellow
    :uneval             :magenta
    :user-fn            :blue}

   :map
   {:comma?    false
    :force-nl? true}

   :set
   {:wrap?             false
    :wrap-coll?        false
    :wrap-after-multi? false
    :indent            5}

   :vector
   {:wrap-coll?        false
    :wrap?             false
    :wrap-after-multi? false
    :indent            4}})

(defn replace-color
  [text from to]
  (str/replace text (str \u001b \[ from) (str \u001b \[ to)))

(defn cstr
  [x]
  (-> x
      (zprint/czprint-str default-options)
      (replace-color 30 97)))
