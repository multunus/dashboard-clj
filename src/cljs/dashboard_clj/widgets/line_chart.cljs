(ns dashboard-clj.widgets.line-chart
  (:require [reagent.core    :as    r]
            [reagent.ratom   :refer-macros [reaction]]
            [cljsjs.highcharts]
            [cljsjs.jquery]))


(defn line-chart-render
  []
  [:div { :style {:width "100%" :height "100%"}}])

(def line-chart-config
  {:chart {:type "column"
           :backgroundColor "transparent"
           
           :style {:labels {
                            :fontFamily "monospace"
                            :color "#FFFFFF"}}}
   :yAxis {:title {:text "Commits"
                   :style {:color "#000000"}}
           :labels { :color "#ffffff"}} 
   :xAxis {:labels {:style {:color "#fff"}}}})


(defn plot-line [this]
  (let [config (-> this r/props :chart-options)
        all-config (merge line-chart-config config)]
    (.highcharts (js/$ (r/dom-node this))
                 (clj->js all-config))))

(defn line-chart
  [chart-options]
  (r/create-class {:reagent-render line-chart-render
                   :component-did-mount plot-line
                   :component-did-update plot-line}))
