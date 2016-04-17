(ns dashboard-clj.widgets.simple-text
  (:require [reagent.core :as r :refer [atom]]))

(defn simple-text-widget[text]
  [:div
   [:div {:class "simple-text-widget"} text]])
  

  
