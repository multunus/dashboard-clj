# Setting up a dashboard

## Create new project

create a new clojure(script) project using lein. We recommned using the chestnut lein template.

```

lein new chestnut <your-dashboard-name> -- --vanilla --http-kit

```

## Set up dashboard-clj as a dependecy

Add dashbaord-clj in your project.clj dependencies

```

:dependecies [[dashboard-clj "0.1.0-SNAPSHOT"]]

```

## Setup datasources

### Configure data source

Data source requires a unique name, a fully qualified function name and a schedule. Data source is the way to collect data periodically as per the schedule and publish it to the dashboard. You could use the following configuration to setup a datasource,

```

(def datasources [{
     :name :banglore-weather
     :fn-name :dashboard-clj.plugins.weather.core/fetch
     :params [{:city "Bangalore"}]
     :schedule {
                :in [0 :seconds]
                :every [1 :hour 20 :seconds]
               }
}])

```

### Start fetching from data source

```

(dashboard-clj.core/start datasources {:port 8000})

```

## setup dashboards

go to the main cljs namespace in your application. If your project name is `deliverit-dashboard`, this might be `deliverit-dashboard.core`

### A simple dashboard with two widgets, which uses a grid layout


```

(ns deliverit-dashboard.core
  (:require [dashboard-clj.core :as d]
            [dashboard-clj.layouts.grid-layout :as grid]
            [dashboard-clj.widgets.simple-text :as s]))


(def dashboard {:layout grid/grid-layout
                :widget [[s/simple-text-widget {:name :widget-one :text "Hello world" :pos {:x 0 :y 0 :h 1 :w 2 }}]
                         [s/simple-text-widget {:name :widget-two :text "World!!" :pos {:x 0 :y 1 :h 1 :w 2}}]]})


(d/render-dashboard dashboard "app")

```







