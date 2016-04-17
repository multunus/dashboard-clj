(ns dashboard-clj.system
  (:require [dashboard-clj.routes :as routes]
            [dashboard-clj.components.webserver :as webserver]
            [com.stuartsierra.component :as component]
            [environ.core :refer [env]]))


(defn ->system []
  (component/system-map
   :server (webserver/new-webserver routes/http-handler (Integer/parseInt (env :http-port)))))
