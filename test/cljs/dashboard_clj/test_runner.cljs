(ns dashboard-clj.test-runner
  (:require
   [doo.runner :refer-macros [doo-tests]]
   [dashboard-clj.core-test]))

(enable-console-print!)

(doo-tests 'dashboard-clj.core-test)
