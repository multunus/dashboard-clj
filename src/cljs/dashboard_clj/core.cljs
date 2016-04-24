(ns dashboard-clj.core
  (:require-macros [reagent.ratom :refer [reaction]]
                   [cljs.core.async.macros :as asyncm :refer (go go-loop)])
  (:require [reagent.core :as r :refer [atom]]
            [cljs.core.async :as async :refer (<! >! put! chan)]
            [taoensso.sente  :as sente :refer (cb-success?)]
            [cljsjs.react-grid-layout]
            [dashboard-clj.layouts.core :as layout]
            [re-frame.core :as rf]))

(defn initial-state [val]
  {:data-sources {}})


(rf/register-handler
 :initialize
 (fn
   [db _]
   (let [new-val (rand-int 100)]
     (merge db (initial-state new-val)))))


(rf/register-handler
 :update-data-source
 (fn [app-state [_ data-source new-val]]
   (assoc-in  app-state [:data-sources data-source] new-val)))


(defn register-global-app-state-subscription[]
  (rf/register-sub
   :app-db
   (fn [db [sid & db-path]]
     (reaction
      (get-in @db (flatten [:data-sources db-path]))))))

(defn connect-to-data-sources []
  (let [{:keys [chsk ch-recv send-fn state]} (sente/make-channel-socket! "/chsk" {:type :auto})]
    (asyncm/go-loop []
      (let [{:keys [event id ?data send-fn]} (async/<! ch-recv)]
        (when (= (get ?data 0) :data-source/event)
          (let [[_ [ds-name ds-data]] ?data]
            (rf/dispatch [:update-data-source ds-name ds-data])))
        (recur)))))

(defn start-dashboard[dashboard element_id]
  (rf/dispatch-sync [:initialize])
  (connect-to-data-sources)
  (register-global-app-state-subscription)
  (let [new-layout (layout/create-layout dashboard)]
    (r/render new-layout (.getElementById js/document element_id))))
