(ns common.print
  (:require [zprint.core :as zprint]))

(defn cstr
  [x]
  (zprint/czprint-str
    x
    {:color-map {:brace              :magenta,
                 :bracket            :purple,
                 :comment            :green,
                 :deref              :red,
                 :fn                 :blue,
                 :hash-brace         :red,
                 :hash-paren         :green,
                 :keyword            :cyan,
                 :nil                :yellow,
                 :none               :black,
                 :number             :green,
                 :paren              :green,
                 :syntax-quote-paren :red
                 :quote              :red,
                 :string             :yellow,
                 :uneval             :magenta,
                 :user-fn            :black}
     :map       {:comma?    false
                 :force-nl? true}}))
