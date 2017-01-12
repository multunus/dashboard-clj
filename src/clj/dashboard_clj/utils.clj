(ns dashboard-clj.utils)

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

(defn resolve-fn [fn-name]
  (kw->fn fn-name))
