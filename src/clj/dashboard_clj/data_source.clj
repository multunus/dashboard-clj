(ns dashboard-clj.data-source
  (:require [clojure.core.async :as async]))

(declare resolve-fn)

(defprotocol FetchableDataSource
  (fetch [this]))

(defrecord DataSource [name read-fn params schedule data output-chan]
  FetchableDataSource
  (fetch [this]
    (let [new-data (apply (resolve-fn read-fn) params)]
      (reset! data new-data)
      (async/go
        (async/>! output-chan [:data-source/event [name {:data new-data}]])))))

(defn new-data-source [{:keys [name read-fn params schedule]}]
  (map->DataSource {
                    :name        name
                    :read-fn     read-fn
                    :params      params
                    :schedule    schedule
                    :output-chan (async/chan (async/sliding-buffer 1))
                    :data        (atom nil)}))

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
