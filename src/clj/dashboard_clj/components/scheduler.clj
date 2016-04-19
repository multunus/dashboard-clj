(ns dashboard-clj.components.scheduler
  (:require [com.stuartsierra.component :as component]
            [immutant.scheduling :as s]))

(declare schedule)
(declare resolve-fn)

(defrecord Scheduler [datasources schedules]
  component/Lifecycle
  (start [component]
    (let [schedules (doall (map schedule datasources))]
      (assoc component :schedules schedules)))
  (stop [component]
    (when schedules
      (s/stop)
      (assoc component :schedules nil))))

(defn new-scheduler [datasources]
  (map->Scheduler {:datasources datasources}))


(defn- schedule [{:keys [name fn-name params schedule]}]
  (s/schedule #(apply (resolve-fn fn-name) params) schedule))


(defn- kw->fn [kw]
  (try
    (let [user-ns (symbol (namespace kw))
          user-fn (symbol (name kw))]
      (or (ns-resolve user-ns user-fn)
          (throw (Exception.))))
    (catch Throwable e
      (throw (ex-info (str "Could not resolve symbol on the classpath, did you require the file that contains the symbol " kw "?") {:kw kw})))))

(defn- resolve-fn [fn-name]
  (kw->fn fn-name))
