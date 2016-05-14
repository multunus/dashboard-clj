(ns dashboard-clj.widget)

(defmacro widget [name widget]
  '(regsiter-widget name widget))
