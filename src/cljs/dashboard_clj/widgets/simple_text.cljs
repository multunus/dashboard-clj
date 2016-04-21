(ns dashboard-clj.widgets.simple-text
  (:require [reagent.core :as r :refer [atom]]
            [dashboard-clj.widgets.core :as widget-common]))


(defmethod widget-common/create-widget :simple-text [{:keys [text data] :as w}]
  [:div
   [:div {:class "simple-text-widget"} (get-in @data [:value])]])
  

  
