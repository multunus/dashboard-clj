(ns dashboard-clj.widgets.github-repo-stats
  (:require [reagent.core :as r :refer [atom]]
            [dashboard-clj.widgets.core :as widget-common]
            [dashboard-clj.widgets.line-chart :as charts]))


(defmethod widget-common/widget :github-repo-stats [{:keys [text data] :as w}]
  [:div {:class "github-repo-stats"}
   [:img {:class "github-logo" :src "/images/github_mark.png"}]
   [:h2 {:class "title" } (:repo-name w)]
   [:h4 {:class "total-commits"} (str "commits: "(reduce + (map #(second %) (get-in @data [:data :commit-data]))))]
   [:table {:class "contributors"}
    [:tr
     [:th "contributors"]]
     (doall (for [contributor (get-in @data [:data :contributors])]
              [:tr {:key contributor}
               [:th (str contributor)]]))]
   [:div {:class "commit-chart" :style { :width "95%" :height "40%"}} 
    [charts/line-chart {:chart-options
                        {:title {:text text}
                         :xAxis {:categories (mapv first (get-in @data [:data :commit-data]))}
                         :series [{:name "commits"
                                   :data (mapv second (get-in @data [:data :commit-data]))}]}}]]])




