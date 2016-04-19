(ns dashboard-clj.components.webserver
  (:require [com.stuartsierra.component :as component]
            [org.httpkit.server :refer [run-server]]))

(defrecord WebServer [server handler options]
  component/Lifecycle
  (start [component]
    (let [handler (get component :handler)
          server  (run-server handler options)]
      (assoc component :server server)))
  (stop [component]
    (when server
      (server)
      (assoc component :server nil))))

(defn new-webserver
  ([handler port]
   (map->WebServer {:handler handler
                    :options {:port port :join? false}})))
