(ns dashboard-clj.widgets.simple-text
  (:require [reagent.core :as r :refer [atom]]
            [dashboard-clj.widgets.core :as widget-common]))


(widget-common/register-widget
 :simple-text
 (fn [{:keys [text data] :as w}]
   [:div {:class "simple-text-widget"}
    [:span {:class "title"} (:title w)]
    [:div {:class "data"}
     [:span
      (get-in @data [:data]) (:text w)]]
    ]))
