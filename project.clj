(defproject com.multunus/dashboard-clj "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.8.40" :scope "provided"]
                 [ring "1.4.0"]
                 [ring/ring-defaults "0.2.0"]
                 [bk/ring-gzip "0.1.1"]
                 [ring.middleware.logger "0.5.0"]
                 [compojure "1.5.0"]
                 [environ "1.0.2"]
                 [http-kit "2.1.19"]
                 [com.stuartsierra/component "0.3.1"]
                 [org.immutant/scheduling "2.1.3"]
                 [reagent "0.6.0-alpha"]
                 [com.taoensso/sente "1.8.1"]
                 [re-frame "0.7.0"]
                 [cljsjs/react-grid-layout "0.12.4-0"]]

  :plugins [[lein-cljsbuild "1.1.1"]
            [lein-environ "1.0.1"]]
  :min-lein-version "2.6.1"
  :source-paths ["src/clj" "src/cljs"]
  :test-paths ["test/clj"]
  :clean-targets ^{:protect false} [:target-path :compile-path "resources/public/js"]
  :uberjar-name "dashboard-clj.jar"
  :repl-options {:init-ns user}
  :env { :http-port 10555}
  :cljsbuild {:builds
              {:app
               {:source-paths ["src/cljs"]

                :figwheel true
                :compiler {:main dashboard-clj.core
                           :asset-path "js/compiled/out"
                           :output-to "resources/public/js/compiled/dashboard_clj.js"
                           :output-dir "resources/public/js/compiled/out"
                           :source-map-timestamp true}}}}
  :figwheel {
             :css-dirs ["resources/public/css"]  ;; watch and update CSS
             :server-logfile "log/figwheel.log"}
  :doo {:build "test"}
  :profiles {:dev
             {:source-paths ["dev"]
              :dependencies [[figwheel "0.5.2"]
                             [figwheel-sidecar "0.5.2"]
                             [com.cemerick/piggieback "0.2.1"]
                             [org.clojure/tools.nrepl "0.2.12"]]

              :plugins [[lein-figwheel "0.5.2"]
                        [lein-doo "0.1.6"]]

              :cljsbuild {:builds
                          {:test
                           {:source-paths ["src/cljs" "test/cljs"]
                            :compiler
                            {:output-to "resources/public/js/compiled/testable.js"
                             :main dashboard-clj.test-runner
                             :optimizations :none}}}}}

             :uberjar
             {:source-paths ^:replace ["src/clj"]
              :hooks [leiningen.cljsbuild]
              :omit-source true
              :aot :all
              :cljsbuild {:builds
                          {:app
                           {:source-paths ^:replace ["src/cljs"]
                            :jar true
                            :compiler
                            {:optimizations :advanced
                             :pretty-print false}}}}}})
