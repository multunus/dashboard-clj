(ns dashboard-clj.system
  (:require [dashboard-clj.routes :as routes]
            [dashboard-clj.components.webserver :as webserver]
            [dashboard-clj.components.scheduler :as scheduler]
            [com.stuartsierra.component :as component]))


(defn ->system [http-port datasources]
  (component/system-map
   :server (webserver/new-webserver routes/http-handler http-port)
   :scheduler (scheduler/new-scheduler datasources)))
