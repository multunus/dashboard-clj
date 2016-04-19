(ns dashboard-clj.system
  (:require [dashboard-clj.routes :as routes]
            [dashboard-clj.components.webserver :as webserver]
            [dashboard-clj.components.scheduler :as scheduler]
            [dashboard-clj.components.websocket :as websocket]
            [taoensso.sente.server-adapters.http-kit      :refer [sente-web-server-adapter]]
            [com.stuartsierra.component :as component]))


(defn ->system [http-port datasources]
  (component/system-map
   :websocket (websocket/new-websocket-server (fn [_]) sente-web-server-adapter {})
   :server (component/using (webserver/new-webserver routes/->http-handler http-port) [:websocket])
   :scheduler (scheduler/new-scheduler datasources)))

(defn start [http-port datasources]
  (component/start (->system http-port datasources)))
