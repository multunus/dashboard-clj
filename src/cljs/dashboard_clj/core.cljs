(ns dashboard-clj.core
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [reagent.core :as r :refer [atom]]
            [cljsjs.react-grid-layout]
            [dashboard-clj.layouts.core :as layout]
            [re-frame.core :as rf]))

(defn initial-state [val]
  {:data-sources {:bangalore-weather { :value val }}})


(rf/register-handler 
 :initialize                
  (fn
    [db _]
    (let [new-val (rand-int 100)]
      (merge db (initial-state new-val )))))


(rf/register-handler
 :update-data-source
 (fn [app-state [_ data-source new-val]]
    data-source new-val))
   (assoc-in  app-state [:data-sources data-source] new-val)))


(defn register-global-app-state-subscription[]
  (rf/register-sub
   :app-db
   (fn [db [sid & db-path]]
     
     (reaction
      (get-in @db (flatten [:data-sources db-path]))))))
   

(defn start-dashboard[dashboard element_id]
  (rf/dispatch-sync [:initialize])
  (register-global-app-state-subscription)
  (let [new-layout (layout/create-layout dashboard)]
    (r/render new-layout (.getElementById js/document element_id))))
