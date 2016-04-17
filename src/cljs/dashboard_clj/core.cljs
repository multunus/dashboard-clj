(ns dashboard-clj.core
  (:require [reagent.core :as r :refer [atom]]
            [cljsjs.react-grid-layout]
            [dashboard-clj.layouts.grid-layout :as layout]))


(defn render-dashboard [layout, element_id]
  (r/render layout (.getElementById js/document  element_id)))
