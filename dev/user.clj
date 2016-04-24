(ns user
  (:require [dashboard-clj.core :as dashboard]
            [figwheel-sidecar.repl-api :as figwheel]
            [com.stuartsierra.component :as component]
            [clojure.tools.namespace.repl :as repl-tools]
            [environ.core :refer [env]]))

;; Let Clojure warn you when it needs to reflect on types, or when it does math
;; on unboxed numbers. In both cases you should add type annotations to prevent
;; degraded performance.
(set! *warn-on-reflection* true)
(set! *unchecked-math* :warn-on-boxed)


(def system)

(defn run [ds-maps]
  (alter-var-root #'system (fn [_]
                             (dashboard/start  ds-maps {:port (Integer/parseInt (env :http-port))})))
  (figwheel/start-figwheel!))

(defn stop []
  (alter-var-root #'system component/stop)
  (figwheel/stop-figwheel!))

(defn reload-namespaces []
  (stop)
  (repl-tools/refresh))

(def browser-repl figwheel/cljs-repl)
