(ns dashboard-clj.system
  (:require [dashboard-clj.routes :as routes]
            [dashboard-clj.components.webserver :as webserver]
            [dashboard-clj.components.scheduler :as scheduler]
            [dashboard-clj.components.websocket :as websocket]
            [taoensso.sente.server-adapters.http-kit      :refer [sente-web-server-adapter]]
            [com.stuartsierra.component :as component]))


(defn ->system [http-port data-sources]
  (component/system-map
   :websocket (websocket/new-websocket-server data-sources sente-web-server-adapter {})
   :server (component/using (webserver/new-webserver routes/->http-handler http-port) [:websocket])
   :scheduler (scheduler/new-scheduler data-sources)))

(defn start [http-port data-sources]
  (component/start (->system http-port data-sources)))
