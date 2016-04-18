(ns dashboard-clj.system
  (:require [dashboard-clj.routes :as routes]
            [dashboard-clj.components.webserver :as webserver]
            [dashboard-clj.components.scheduler :as scheduler]
            [com.stuartsierra.component :as component]
            [environ.core :refer [env]]))


(defn ->system [datasources]
  (component/system-map
   :server (webserver/new-webserver routes/http-handler (Integer/parseInt (env :http-port)))
   :scheduler (scheduler/new-scheduler datasources)))
