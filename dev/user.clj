(ns user
  (:require [dashboard-clj.system :as s]
            [figwheel-sidecar.repl-api :as figwheel]
            [com.stuartsierra.component :as component]))

;; Let Clojure warn you when it needs to reflect on types, or when it does math
;; on unboxed numbers. In both cases you should add type annotations to prevent
;; degraded performance.
(set! *warn-on-reflection* true)
(set! *unchecked-math* :warn-on-boxed)

(def system)

(defn run []
  (alter-var-root #'system (fn [_]
                             (component/start (s/->system))))
  (figwheel/start-figwheel!))

(defn stop []
  (alter-var-root #'system component/stop)
  (figwheel/stop-figwheel!))

(def browser-repl figwheel/cljs-repl)
