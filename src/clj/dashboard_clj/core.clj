(ns dashboard-clj.core
  (:require [dashboard-clj.system :as system]))


(defn start [datasources {:keys [port] :as options}]
  (system/start port datasources))
