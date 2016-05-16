(ns dashboard-clj.layouts.grid-layout-responsive
  (:require [cljsjs.react-grid-layout]
            [dashboard-clj.layouts.core :as layout-common]
            [dashboard-clj.widgets.core :as widget-common]
            [reagent.core :refer [atom] :as r]))



(def responsive-grid-layout (.-Responsive js/ReactGridLayout))
(def width-provider (.-WidthProvider js/ReactGridLayout))
(def responsive-grid-layout-adapter (r/adapt-react-class (width-provider. responsive-grid-layout)))


(defn calculate-layout [{:as dashboard :keys [widgets]}]
  (into {}
        (for [brp [:lg :md, :sm :xs :xxs]
              :let [brp-pos
                    (into [] (for [widget (:widgets dashboard)
                                   :let [pos (get-in widget [:layout-opts :position brp])]
                                   :when (not (nil? pos))]
                               (merge pos {:i (:name widget )})))]
              :when (not-empty brp-pos)]
              {brp brp-pos})))
              
(def default-layout-opts {:className "layout"})


(defn widget-wrapper[w]
  [:div {:key (:name w) :class "widget" :style (:style w)}
   (widget-common/setup-widget w)])


(defmethod layout-common/create-layout :responsive-grid-layout [{:keys[widgets] :as dashboard}]
  [responsive-grid-layout-adapter
   (merge default-layout-opts (:layout-opts dashboard) {:layouts(calculate-layout dashboard)})
   (doall (for [widget  widgets]
            (widget-wrapper widget)))])




