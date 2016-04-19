(ns dashboard-clj.components.websocket
  (:require [com.stuartsierra.component :as component]
            [taoensso.sente :as sente]))

(defrecord WebSocketServer [handler webserver-adapter options ring-ajax-post ring-ajax-get-or-ws-handshake ch-recv chsk-send! connected-uids router]
  component/Lifecycle
  (start [component]
    (let [{:keys [ch-recv send-fn ajax-post-fn ajax-get-or-ws-handshake-fn connected-uids]} (sente/make-channel-socket-server! webserver-adapter options)]
      (assoc component
             :ring-ajax-post ajax-post-fn
             :ring-ajax-get-or-ws-handshake ajax-get-or-ws-handshake-fn
             :ch-recv ch-recv
             :chsk-send! send-fn
             :connected-uids connected-uids
             :router (sente/start-chsk-router! ch-recv handler))))
  (stop [component]
    (when router
      (router)
      (assoc component
             :ring-ajax-post nil
             :ring-ajax-get-or-ws-handshake nil
             :ch-recv nil
             :chsk-send! nil
             :connected-uids nil
             :router nil))))

(defn new-websocket-server [handler webserver-adapter options]
  (map->WebSocketServer {:handler           handler
                         :webserver-adapter webserver-adapter
                         :options           options}))
