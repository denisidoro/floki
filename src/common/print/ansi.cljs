; This code is heavily inspired by planck's source code
; Planck is a software copyrighted © 2015–2017 Mike Fikes and Contributors
; and distributed under the Eclipse Public License either version 1.0 or (at your option) any later version.
; 
; For the original source, please refer to 
; https://github.com/mfikes/planck/blob/master/planck-cljs/src/planck/from/io/aviso/ansi.clj

(ns core.print.ansi
  (:require [clojure.string :as str]))

(def ^:private ^:const csi
  "The control sequence initiator: `ESC [`"
  "\u001b[")

(def ^:private ^:const sgr
  "The Select Graphic Rendition suffix: m"
  "m")

(def ^:const reset-font
  "Resets the font, clearing bold, italic, color, and background color."
  (str csi sgr))

(defn ^:private def-sgr-const
  "Utility for defining a font-modifying constant."
  [symbol-name color-name & codes]
  `(def ^:const ~(symbol symbol-name)
     ~(str "Constant for ANSI code to enable " color-name " text.")
     (str csi ~(str/join ";" codes) sgr)))

(defn ^:private def-sgr-fn
  "Utility for creating a function that enables some combination of SGR codes around some text, but resets
  the font after the text."
  [fn-name color-name & codes]
  `(defn ~(symbol fn-name)
     ~(str "Wraps the provided text with ANSI codes to render as " color-name " text.")
     [~'text]
     (str (str csi ~(str/join ";" codes) sgr) ~'text (str csi sgr))))

;; Lumo is bootstrapped Clojurescript so it doesn't support portable macros
#_(defmacro generate-color-functions []
  (let [generate-functions-for-index-color (fn [index color-name]
                                             [(def-sgr-fn color-name color-name (+ 30 index))
                                              (def-sgr-fn (str color-name "-bg") (str color-name " background") (+ 40 index))
                                              (def-sgr-fn (str "bold-" color-name) (str "bold " color-name) 1 (+ 30 index))
                                              (def-sgr-fn (str "bold-" color-name "-bg") (str "bold " color-name " background") 1 (+ 40 index))
                                              (def-sgr-const (str color-name "-font") color-name (+ 30 index))
                                              (def-sgr-const (str color-name "-bg-font") (str color-name " background") (+ 40 index))
                                              (def-sgr-const (str "bold-" color-name "-font") (str "bold " color-name) 1 (+ 30 index))
                                              (def-sgr-const (str "bold-" color-name "-bg-font") (str "bold " color-name " background") 1 (+ 40 index))])]
    `(do
       ~@(generate-functions-for-index-color 0 "black")
       ~@(generate-functions-for-index-color 1 "red")
       ~@(generate-functions-for-index-color 2 "green")
       ~@(generate-functions-for-index-color 3 "yellow")
       ~@(generate-functions-for-index-color 4 "blue")
       ~@(generate-functions-for-index-color 5 "magenta")
       ~@(generate-functions-for-index-color 6 "cyan")
       ~@(generate-functions-for-index-color 7 "white"))))

;; So we explicitly write the expanded macro expressions here
(defn black "Wraps the provided text with ANSI codes to render as black text." [text] (str (str csi "30" sgr) text (str csi sgr))) (defn black-bg "Wraps the provided text with ANSI codes to render as black background text." [text] (str (str csi "40" sgr) text (str csi sgr))) (defn bold-black "Wraps the provided text with ANSI codes to render as bold black text." [text] (str (str csi "1;30" sgr) text (str csi sgr))) (defn bold-black-bg "Wraps the provided text with ANSI codes to render as bold black background text." [text] (str (str csi "1;40" sgr) text (str csi sgr))) (def black-font "Constant for ANSI code to enable black text." (str csi "30" sgr)) (def black-bg-font "Constant for ANSI code to enable black background text." (str csi "40" sgr)) (def bold-black-font "Constant for ANSI code to enable bold black text." (str csi "1;30" sgr)) (def bold-black-bg-font "Constant for ANSI code to enable bold black background text." (str csi "1;40" sgr)) (defn red "Wraps the provided text with ANSI codes to render as red text." [text] (str (str csi "31" sgr) text (str csi sgr))) (defn red-bg "Wraps the provided text with ANSI codes to render as red background text." [text] (str (str csi "41" sgr) text (str csi sgr))) (defn bold-red "Wraps the provided text with ANSI codes to render as bold red text." [text] (str (str csi "1;31" sgr) text (str csi sgr))) (defn bold-red-bg "Wraps the provided text with ANSI codes to render as bold red background text." [text] (str (str csi "1;41" sgr) text (str csi sgr))) (def red-font "Constant for ANSI code to enable red text." (str csi "31" sgr)) (def red-bg-font "Constant for ANSI code to enable red background text." (str csi "41" sgr)) (def bold-red-font "Constant for ANSI code to enable bold red text." (str csi "1;31" sgr)) (def bold-red-bg-font "Constant for ANSI code to enable bold red background text." (str csi "1;41" sgr)) (defn green "Wraps the provided text with ANSI codes to render as green text." [text] (str (str csi "32" sgr) text (str csi sgr))) (defn green-bg "Wraps the provided text with ANSI codes to render as green background text." [text] (str (str csi "42" sgr) text (str csi sgr))) (defn bold-green "Wraps the provided text with ANSI codes to render as bold green text." [text] (str (str csi "1;32" sgr) text (str csi sgr))) (defn bold-green-bg "Wraps the provided text with ANSI codes to render as bold green background text." [text] (str (str csi "1;42" sgr) text (str csi sgr))) (def green-font "Constant for ANSI code to enable green text." (str csi "32" sgr)) (def green-bg-font "Constant for ANSI code to enable green background text." (str csi "42" sgr)) (def bold-green-font "Constant for ANSI code to enable bold green text." (str csi "1;32" sgr)) (def bold-green-bg-font "Constant for ANSI code to enable bold green background text." (str csi "1;42" sgr)) (defn yellow "Wraps the provided text with ANSI codes to render as yellow text." [text] (str (str csi "33" sgr) text (str csi sgr))) (defn yellow-bg "Wraps the provided text with ANSI codes to render as yellow background text." [text] (str (str csi "43" sgr) text (str csi sgr))) (defn bold-yellow "Wraps the provided text with ANSI codes to render as bold yellow text." [text] (str (str csi "1;33" sgr) text (str csi sgr))) (defn bold-yellow-bg "Wraps the provided text with ANSI codes to render as bold yellow background text." [text] (str (str csi "1;43" sgr) text (str csi sgr))) (def yellow-font "Constant for ANSI code to enable yellow text." (str csi "33" sgr)) (def yellow-bg-font "Constant for ANSI code to enable yellow background text." (str csi "43" sgr)) (def bold-yellow-font "Constant for ANSI code to enable bold yellow text." (str csi "1;33" sgr)) (def bold-yellow-bg-font "Constant for ANSI code to enable bold yellow background text." (str csi "1;43" sgr)) (defn blue "Wraps the provided text with ANSI codes to render as blue text." [text] (str (str csi "34" sgr) text (str csi sgr))) (defn blue-bg "Wraps the provided text with ANSI codes to render as blue background text." [text] (str (str csi "44" sgr) text (str csi sgr))) (defn bold-blue "Wraps the provided text with ANSI codes to render as bold blue text." [text] (str (str csi "1;34" sgr) text (str csi sgr))) (defn bold-blue-bg "Wraps the provided text with ANSI codes to render as bold blue background text." [text] (str (str csi "1;44" sgr) text (str csi sgr))) (def blue-font "Constant for ANSI code to enable blue text." (str csi "34" sgr)) (def blue-bg-font "Constant for ANSI code to enable blue background text." (str csi "44" sgr)) (def bold-blue-font "Constant for ANSI code to enable bold blue text." (str csi "1;34" sgr)) (def bold-blue-bg-font "Constant for ANSI code to enable bold blue background text." (str csi "1;44" sgr)) (defn magenta "Wraps the provided text with ANSI codes to render as magenta text." [text] (str (str csi "35" sgr) text (str csi sgr))) (defn magenta-bg "Wraps the provided text with ANSI codes to render as magenta background text." [text] (str (str csi "45" sgr) text (str csi sgr))) (defn bold-magenta "Wraps the provided text with ANSI codes to render as bold magenta text." [text] (str (str csi "1;35" sgr) text (str csi sgr))) (defn bold-magenta-bg "Wraps the provided text with ANSI codes to render as bold magenta background text." [text] (str (str csi "1;45" sgr) text (str csi sgr))) (def magenta-font "Constant for ANSI code to enable magenta text." (str csi "35" sgr)) (def magenta-bg-font "Constant for ANSI code to enable magenta background text." (str csi "45" sgr)) (def bold-magenta-font "Constant for ANSI code to enable bold magenta text." (str csi "1;35" sgr)) (def bold-magenta-bg-font "Constant for ANSI code to enable bold magenta background text." (str csi "1;45" sgr)) (defn cyan "Wraps the provided text with ANSI codes to render as cyan text." [text] (str (str csi "36" sgr) text (str csi sgr))) (defn cyan-bg "Wraps the provided text with ANSI codes to render as cyan background text." [text] (str (str csi "46" sgr) text (str csi sgr))) (defn bold-cyan "Wraps the provided text with ANSI codes to render as bold cyan text." [text] (str (str csi "1;36" sgr) text (str csi sgr))) (defn bold-cyan-bg "Wraps the provided text with ANSI codes to render as bold cyan background text." [text] (str (str csi "1;46" sgr) text (str csi sgr))) (def cyan-font "Constant for ANSI code to enable cyan text." (str csi "36" sgr)) (def cyan-bg-font "Constant for ANSI code to enable cyan background text." (str csi "46" sgr)) (def bold-cyan-font "Constant for ANSI code to enable bold cyan text." (str csi "1;36" sgr)) (def bold-cyan-bg-font "Constant for ANSI code to enable bold cyan background text." (str csi "1;46" sgr)) (defn white "Wraps the provided text with ANSI codes to render as white text." [text] (str (str csi "37" sgr) text (str csi sgr))) (defn white-bg "Wraps the provided text with ANSI codes to render as white background text." [text] (str (str csi "47" sgr) text (str csi sgr))) (defn bold-white "Wraps the provided text with ANSI codes to render as bold white text." [text] (str (str csi "1;37" sgr) text (str csi sgr))) (defn bold-white-bg "Wraps the provided text with ANSI codes to render as bold white background text." [text] (str (str csi "1;47" sgr) text (str csi sgr))) (def white-font "Constant for ANSI code to enable white text." (str csi "37" sgr)) (def white-bg-font "Constant for ANSI code to enable white background text." (str csi "47" sgr)) (def bold-white-font "Constant for ANSI code to enable bold white text." (str csi "1;37" sgr)) (def bold-white-bg-font "Constant for ANSI code to enable bold white background text." (str csi "1;47" sgr))
