# How To Setup A New Dashboard

`dashboard-clj` has been built as a library that can be easily integrated into any existing project. The procedure to setup a new dashboard is outlined below.

## Artifacts

With Leiningen, add
```clojure
[dashboard-clj "0.1.0-SNAPSHOT"]
```

## Usage

### clj

The main namespace for the dashboard is dashboard-clj.core.
```clojure
(:require [dashboard-clj.core :as dashboard])
```

##### Data Source Specification

The basic function of the dashboard is of course to display data. In order for this to happen, it needs to receive its data in a specific format as exemplified below:
```clojure
     [
       {
         :name :sample-dashboard-stats
         :read-fn :sample-dashboard.fetcher/fetch
         :params ["multunus" "dashboard-clj"]
         :schedule {
           :in [0 :seconds]
           :every [5 :minutes]}}
       ...
     ]
```

This vector is the data source and contains hashmaps with the keys which represent the following:

* `:name` -
An alias that stands for how the Clojurescript front-end is going to access this particular dataset.

* `:read-fn` - 
The function (fully namespaced) that gets executed to compile the data that will get used in the interface.

* `:params` - 
A vector of arguments that gets passed to the read function.

* `:schedule` - 
The frequency with which the read function is invoked and the data gets refreshed. See [here](http://immutant.org/tutorials/scheduling/) for additional usage details of this section.


##### Starting the Dashboard
With the data source configuration set, we can now start the dashboard itself as follows:
```clojure
(dashboard/start datasources {:port <port-number>})
```

`datasources` here is the vector shown above that basically governs the supply of data to the dashboard itself. This will in turn fire up the various components required for the dashboard to function, namely the websocket server, the web server and the scheduler. These are managed as [components](https://github.com/stuartsierra/component) in the system.

Once this happens, the read function will be executed as dictated to the scheduler and push data to the websocket server which in turn broadcasts the same to the clients.

### cljs

The first thing we need to do is load the libraries required for a particular dashboard. These include the core, layouts and individual widgets.
```clojure
(:require [dashboard-clj.core :as dashboard]
          [dashboard-clj.layouts.grid-layout :as grid]
          [dashboard-clj.widgets.simple-text]
          [dashboard-clj.widgets.github-repo-stats])
```

The dashboard currently supports only a [grid layout](https://github.com/STRML/react-grid-layout), meaning that the initial configuration of the widgets can be configured according to a predefined grid system as specified in the readme of the grid layout project.

The various configurations of the dashboard widgets are supplied through a hashmap containing the following sample structure:
```clojure
    {:layout :grid-layout
     :layout-opts {:rowHeight 500 :cols 6}
     :widgets [
                {
                 :type :github-repo-stats
                 :name :first-widget
                 :data-source :sample-dashboard-stats
                 :repo-name "Sample Dashboard"
                 :title "commits"
                 :text "commit history"
                 :layout-opts {:position {:x 0 :y 0  :h 1 :w 2 }}
                 :style {:background-color "#ffffff"
                         :font-family "Times New Roman, Times, serif"
                         :font-size "20"}}
                ...
                ]})
```

Here’s what the main keys stand are for:

* `:layout` - 
The type of layout to be used for the widget. Right now the only supported option is `:grid-layout`.

* `:layout-opts` - 
The height and width of the layout area in terms of generic grid units, e.g. `:cols` 6 divides the entire space into 6 columns.

* `:widgets` -
A vector of individual widget configurations as explained below.

There are two widgets built in the dashboard, namely a simple text widget and a Github repository summary widget. Here’s the hashmap structure for the widget configurations:

* `:type` -
What type of widget this is. The two options currently supported are :simple-text and :github-repo-stats.

* `:name` -
An alias for the widget.

* `:data-source` -
The name of the data source. This is the same name that is assigned to the `:name` attribute of the `datasources` map that we encountered before.

* `:repo-name` -
The title to be displayed at the top of the widget. This would typically be the name of the repository for which details are shown.

* `:text` -
The title of the commit breakdown graph shown at the bottom of the widget.

* `:layout-opts` -
This dictates the position of and area covered by the widget, e.g. `{:position {:x 0 :y 0  :h 1 :w 2 }}`. `x` and `y` for the top-left position of the `h` and `w` for the height and width. All are given in generic grid units.

* `:style` -
Additional styles to be applied to the widget. These can be given as are usually specified for Reagent components. Of course styles can also be specified elsewhere, e.g. in other CSS files included.

Once the configuration is set up, all you have to do is fire up the dashboard:
```clojure
(dashboard/start-dashboard dashboard "app")
```

`app` is the HTML id of the element into which the dashboard will be rendered.

## Example
To get a better feel for the above documentation, here’s an [example project](https://github.com/kp2222/deliverit-dashboard) that has used `dashboard-clj` to implement a [simple Github repos summary dashboard](https://protected-wave-36452.herokuapp.com/).
