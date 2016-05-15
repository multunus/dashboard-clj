(ns dashboard-clj.components.websocket
  (:require [com.stuartsierra.component :as component]
            [taoensso.sente :as sente]
            [clojure.core.async :as async]
            [dashboard-clj.data-source :as ds]))

(defmulti -client-ev-handler (fn [_ y] (:id y)))

(defn client-ev-handler [server {:as ev-msg :keys [id ?data event]}]
  (-client-ev-handler server ev-msg))

(defmethod -client-ev-handler :default [server {:as ev-msg :keys [id ?data event]}]
  (println "un handled client event" id))


(defmethod  -client-ev-handler :dashboard-clj.core/sync
  [ {:as ctx :keys [data-sources chsk-send!]} {:as ev-msg :keys [id ?data event uid]}]
  (doseq [event (map #(ds/data->event (:name %) (deref (:data %)))
                     data-sources)]
          (chsk-send! uid event)))




(defrecord WebSocketServer [data-sources handler webserver-adapter options ring-ajax-post ring-ajax-get-or-ws-handshake ch-recv chsk-send! connected-uids router]
  component/Lifecycle
  (start [component]
    (let [{:keys [ch-recv send-fn ajax-post-fn ajax-get-or-ws-handshake-fn connected-uids]} (sente/make-channel-socket-server! webserver-adapter options)
          ch-out                                                                            (async/chan (async/sliding-buffer 1000))
          mix-out                                                                           (async/mix ch-out)]
      (doseq [in-chan (map #(.output-chan %) data-sources)]
        (async/admix mix-out in-chan))
      (async/go-loop []
        (let [event (async/<! ch-out)]
          (doseq [cid (:any @connected-uids)]
            (send-fn cid event))
          (recur)))
      (assoc component
             :ring-ajax-post ajax-post-fn
             :ring-ajax-get-or-ws-handshake ajax-get-or-ws-handshake-fn
             :ch-recv ch-recv
             :chsk-send! send-fn
             :connected-uids connected-uids
             :router (sente/start-chsk-router! ch-recv
                                               (partial handler {:data-sources data-sources
                                                                 :chsk-send! send-fn })))))
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

(defn new-websocket-server [data-sources webserver-adapter options]
  (map->WebSocketServer {:data-sources      data-sources
                         :handler           client-ev-handler
                         :webserver-adapter webserver-adapter
                         :options           options}))
