; This code is heavily inspired by planck's source code
; Planck is a software copyrighted © 2015–2017 Mike Fikes and Contributors
; and distributed under the Eclipse Public License either version 1.0 or (at your option) any later version.
; 
; For the original source, please refer to 
; https://github.com/mfikes/planck/blob/master/planck-cljs/src/planck/themes.cljs

(ns common.print.themes
  "Color theme management."
  (:require [common.print.ansi :as ansi]))

(def ^:private colorize-fn-dumb identity)
(def ^:private ^:const colorize-off-dumb "")

(def ^:private dumb
  {:results-font         colorize-off-dumb
   :results-string-font  colorize-off-dumb
   :results-keyword-font colorize-off-dumb
   :ex-msg-fn            colorize-fn-dumb
   :ex-stack-fn          colorize-fn-dumb
   :rdr-ann-err-fn       colorize-fn-dumb
   :err-font             colorize-off-dumb
   :verbose-font         colorize-off-dumb
   :reset-font           colorize-off-dumb})

(def ^:private theme-ansi-base
  {:reset-font ansi/reset-font})

(def ^:private light
  (merge theme-ansi-base
    {:results-font         ansi/blue-font
     :results-string-font  ansi/green-font
     :results-keyword-font ansi/magenta-font
     :ex-msg-fn            ansi/bold-red
     :ex-stack-fn          ansi/green
     :rdr-ann-err-fn       ansi/bold-magenta
     :err-font             ansi/red-font
     :verbose-font         ansi/white-font}))

(def ^:private dark
  (merge theme-ansi-base
    {:results-font         ansi/bold-blue-font
     :results-string-font  ansi/green-font
     :results-keyword-font ansi/magenta-font
     :ex-msg-fn            ansi/bold-red
     :ex-stack-fn          ansi/green
     :rdr-ann-err-fn       ansi/magenta
     :err-font             ansi/red-font
     :verbose-font         ansi/white-font}))

(def ^:private themes
  {:plain dumb
   :light light
   :dark  dark})

(defn get-theme
  [theme-id]
  (get themes theme-id dumb))
