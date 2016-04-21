(ns dashboard-clj.widgets.core
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :as rf]))


(defmulti create-widget :type)

(defn setup-widget[w]
  (if (:data-source w)
    (let [data (rf/subscribe [:app-db (:data-source w)])
          w (merge w {:data data})]
      [create-widget w])
    (create-widget w)))
