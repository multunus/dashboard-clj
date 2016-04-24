(ns dashboard-clj.components.scheduler
  (:require [com.stuartsierra.component :as component]
            [immutant.scheduling :as s]))

(declare schedule)

(defrecord Scheduler [data-sources schedules]
  component/Lifecycle
  (start [component]
    (let [schedules (doall (map schedule data-sources))]
      (assoc component :schedules schedules)))
  (stop [component]
    (when schedules
      (s/stop)
      (assoc component :schedules nil))))

(defn new-scheduler [data-sources]
  (map->Scheduler {:data-sources data-sources}))


(defn- schedule [data-source]
  (s/schedule #(.fetch data-source) (.schedule data-source)))
