(ns dashboard-clj.widgets.core
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :as rf]))

(def widget (atom {}))

(defn register-widget [name w]
  (swap! widget assoc name w))

(defn setup-widget [{:keys [:data-source :type] :as w}]
  (if data-source
    (let [data (rf/subscribe [:app-db data-source])]
      [(get @widget type) (merge w {:data data})])
    ((get @widget type) w)))
