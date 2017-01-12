(ns dashboard-clj.core
  (:require [dashboard-clj.system :as system]
            [dashboard-clj.data-source :as ds]))


(defn start [ds-maps {:keys [port] :as options}]
  (let [data-sources (map #(ds/create-datasource %) ds-maps)]
    (system/start port data-sources)))
