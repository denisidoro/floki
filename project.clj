(defproject floki "0.1.0-SNAPSHOT"
  :description "FIXME: write this!"
  :url "http://example.com/FIXME"
  :min-lein-version "2.7.1"
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/clojurescript "1.10.439"]
                 [reagent "0.8.1" :exclusions [[cljsjs/react]
                                               [cljsjs/react-dom]
                                               [cljsjs/create-react-class]]]
                 [re-frame "0.10.6"]]
  :plugins [[lein-cljsbuild "1.1.7" :exclusions [[org.clojure/clojure]]]
            [lein-figwheel "0.5.17"]]
  :source-paths ["src"]
  :clean-targets ["target" "node_modules" "package.json" "package-lock.json"]
  :cljsbuild {:builds [{:id           "dev"
                        :source-paths ["src"]
                        :figwheel     {:on-jsload "floki.core/load"}
                        :compiler     {:main                 floki.core
                                       :asset-path           "target/js/compiled/dev"
                                       :output-to            "target/js/compiled/hello_react_blessed.js"
                                       :output-dir           "target/js/compiled/dev"
                                       :target               :nodejs
                                       :optimizations        :none
                                       :source-map-timestamp true
                                       :npm-deps             {:blessed            "0.1.81"
                                                              :neo-blessed        "0.2.0" ; Optional
                                                              :react-blessed      "0.5.0"
                                                              :react              "16.6.3"
                                                              :react-dom          "16.6.3"
                                                              :create-react-class "15.6.3"
                                                              :ws                 "6.1.2"}
                                       :install-deps         true}}
                       {:id           "prod"
                        :source-paths ["src"]
                        :compiler     {:output-to     "target/main.js"
                                       :output-dir    "target/js/compiled/prod"
                                       :target        :nodejs
                                       :optimizations :simple
                                       :npm-deps      {:blessed            "0.1.81"
                                                       :react-blessed      "0.5.0"
                                                       :react              "16.6.3"
                                                       :react-dom          "16.6.3"
                                                       :create-react-class "15.6.3"}
                                       :install-deps  true}}]}
  :profiles {:dev {:dependencies [[figwheel-sidecar "0.5.13"]
                                  [com.cemerick/piggieback "0.2.2"]]
                   :repl-options {:nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}}})