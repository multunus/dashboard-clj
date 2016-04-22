(ns dashboard-clj.layouts.grid-layout
  (:require [cljsjs.react-grid-layout]
            [dashboard-clj.layouts.core :as layout-common]
            [dashboard-clj.widgets.core :as widget-common]
            [reagent.core :refer [atom] :as r]))

(def grid-layout-adapter (r/adapt-react-class js/ReactGridLayout))

(defn widget-wrapper[w]
  [:div {:key (:name w) :_grid (get-in w [:layout-opts :pos])}
   (widget-common/setup-widget w)])

(defmethod layout-common/create-layout :grid-layout [{:keys[widgets]}]
  [grid-layout-adapter
   {:className "layout" :cols 4 :rowHeight 30 :width 1200 }
   (doall (for [widget  widgets]
            (widget-wrapper widget)))])






