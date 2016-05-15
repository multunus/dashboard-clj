(ns dashboard-clj.data-source
  (:require [clojure.core.async :as async]))

(declare resolve-fn)
(declare data->event)

(defprotocol FetchableDataSource
  (fetch [this]))

(defrecord DataSource [name read-fn params schedule data output-chan]
  FetchableDataSource
  (fetch [this]
    (let [new-data (apply (resolve-fn read-fn) params)]
      (reset! data new-data)
      (async/go
        (async/>! output-chan (data->event name new-data))))))

(defn new-data-source [{:keys [name read-fn params schedule]}]
  (map->DataSource {
                    :name        name
                    :read-fn     read-fn
                    :params      params
                    :schedule    schedule
                    :output-chan (async/chan (async/sliding-buffer 1))
                    :data        (atom nil)}))

(defn data->event [event-name data]
  [:data-source/event [event-name {:data data}]])

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
