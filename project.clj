(defproject dashboard-clj "0.1.0-SNAPSHOT"
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
                 [reagent "0.6.0-alpha"]
                 [cljsjs/react-grid-layout "0.10.8-0"]]

  :plugins [[lein-cljsbuild "1.1.1"]
            [lein-environ "1.0.1"]]

  :min-lein-version "2.6.1"

  :source-paths ["src/clj" "src/cljs" "dev"]

  :test-paths ["test/clj"]

  :clean-targets ^{:protect false} [:target-path :compile-path "resources/public/js"]

  :uberjar-name "dashboard-clj.jar"

  ;; Use `lein run` if you just want to start a HTTP server, without figwheel
  :main dashboard-clj.server

  ;; nREPL by default starts in the :main namespace, we want to start in `user`
  ;; because that's where our development helper functions like (run) and
  ;; (browser-repl) live.
  :repl-options {:init-ns user}

  :env { :http-port 10555}

  :cljsbuild {:builds
              {:app
               {:source-paths ["src/cljs"]

                :figwheel true
                ;; Alternatively, you can configure a function to run every time figwheel reloads.
                ;; :figwheel {:on-jsload "dashboard-clj.core/on-figwheel-reload"}

                :compiler {:main dashboard-clj.core
                           :asset-path "js/compiled/out"
                           :output-to "resources/public/js/compiled/dashboard_clj.js"
                           :output-dir "resources/public/js/compiled/out"
                           :source-map-timestamp true}}}}

  ;; When running figwheel from nREPL, figwheel will read this configuration
  ;; stanza, but it will read it without passing through leiningen's profile
  ;; merging. So don't put a :figwheel section under the :dev profile, it will
  ;; not be picked up, instead configure figwheel here on the top level.

  :figwheel {;; :http-server-root "public"       ;; serve static assets from resources/public/
             ;; :server-port 3449                ;; default
             ;; :server-ip "127.0.0.1"           ;; default
             :css-dirs ["resources/public/css"]  ;; watch and update CSS

             ;; Instead of booting a separate server on its own port, we embed
             ;; the server ring handler inside figwheel's http-kit server, so
             ;; assets and API endpoints can all be accessed on the same host
             ;; and port. If you prefer a separate server process then take this
             ;; out and start the server with `lein run`.

             ;; Start an nREPL server into the running figwheel process. We
             ;; don't do this, instead we do the opposite, running figwheel from
             ;; an nREPL process, see
             ;; https://github.com/bhauman/lein-figwheel/wiki/Using-the-Figwheel-REPL-within-NRepl
             ;; :nrepl-port 7888

             ;; To be able to open files in your editor from the heads up display
             ;; you will need to put a script on your path.
             ;; that script will have to take a file path and a line number
             ;; ie. in  ~/bin/myfile-opener
             ;; #! /bin/sh
             ;; emacsclient -n +$2 $1
             ;;
             ;; :open-file-command "myfile-opener"

             :server-logfile "log/figwheel.log"}

  :doo {:build "test"}

  :profiles {:dev
             {:dependencies [[figwheel "0.5.2"]
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
                            :compiler
                            {:optimizations :advanced
                             :pretty-print false}}}}}})
