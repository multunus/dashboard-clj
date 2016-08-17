(ns dashboard-clj.components.webserver
  (:require [com.stuartsierra.component :as component]
            [org.httpkit.server :refer [run-server]]))

(defrecord WebServer [server ->http-handler options websocket]
  component/Lifecycle
  (start [component]
    (let [ring-ajax-post                (get websocket :ring-ajax-post)
          ring-ajax-get-or-ws-handshake (get websocket :ring-ajax-get-or-ws-handshake)
          server                        (run-server (->http-handler ring-ajax-post ring-ajax-get-or-ws-handshake) options)]
      (assoc component :server server)))
  (stop [component]
    (when server
      (server)
      (assoc component :server nil))))

(defn new-webserver
  ([->http-handler port]
   (map->WebServer {:->http-handler ->http-handler
                    :options        {:port port :join? false}})))
