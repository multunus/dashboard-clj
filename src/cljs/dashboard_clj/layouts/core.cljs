(ns dashboard-clj.layouts.core)

(def layout-store (atom {}))

(defn register-layout [name l]
  (swap! layout-store assoc name l))

(defn setup-layout [name {:keys [widgets options]}]
  [(get @layout-store name) widgets options])
