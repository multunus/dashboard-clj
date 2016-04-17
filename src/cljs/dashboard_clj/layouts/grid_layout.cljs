(ns dashboard-clj.layouts.grid-layout
  (:require [cljsjs.react-grid-layout]
            [reagent.core :refer [atom] :as r]))

(def grid-layout-adapter (r/adapt-react-class js/ReactGridLayout))

(defn create-widget-from-args [[widget opts]]
  [:div {:key (:name opts) :_grid (:pos opts)}
   [widget (:text opts)]])

(defn grid-layout[widgets]
    [grid-layout-adapter {:className "layout" :cols 4 :rowHeight 30 :width 1200 }
     (for [w widgets]
       (create-widget-from-args w))])


