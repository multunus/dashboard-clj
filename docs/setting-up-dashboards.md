# How To Setup A New Dashboard

`dashboard-clj` has been built as a library that can be easily integrated into any existing project. The procedure to setup a new dashboard is outlined below.

## Artifacts

With Leiningen, add
```clojure
[com.multunus/dashboard-clj "0.1.0-SNAPSHOT"]
```
or use the leiningen project template

- Add the following to your ```~/.lein/profiles.clj```

```clojure
{:user {:plugins [[dashboard-app/lein-template "0.0.1-SNAPSHOT"]]}}
```

- run ```lein new dashboard <your.project.name>```

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
     ]
```

This vector is the data source and contains hashmaps with the keys which represent the following:

* `:name` [Required]-
An alias that stands for how the Clojurescript front-end is going to access this particular dataset.

* `:read-fn` [Required]- 
The function (fully namespaced) that gets executed to compile the data that will get used in the interface.

* `:params` [Optional]- 
A vector of arguments that gets passed to the read function every time it is executed as per the schedule.

* `:schedule` [Optional]- 
The frequency with which the read function is invoked and the data gets refreshed. If you don't provide a schedule, it will be executed only once.
    Example:
    ```clojure
    {...
    :schedule {
      :in [5 :seconds]
      :every [1 :hour 30 :minutes :10seconds]
      :limit 100
    }
    ...}
    ```
    See [here](http://immutant.org/tutorials/scheduling/) for additional usage details of this section.
    
##### Starting the Dashboard
With the data source configuration set, we can now start the dashboard itself as follows:
```clojure
(dashboard/start datasources {:port <port-number>})
```

`datasources` here is the vector shown above that basically governs the supply of data to the dashboard itself. This will in turn fire up the various components required for the dashboard to function, namely the websocket server([sente](https://github.com/ptaoussanis/sente)), the web server([http-kit](http://www.http-kit.org/)) and the scheduler([immutant scheduler](http://immutant.org/tutorials/scheduling/)). These are managed as [components](https://github.com/stuartsierra/component) in the system.

Once this happens, the read function provided in the configuration will be executed as dictated to the scheduler and push data to the websocket server which in turn broadcasts the same to the clients.

### cljs

The first thing we need to do is load the libraries required for a particular dashboard. These include the core, layouts and individual widgets.
```clojure
(:require [dashboard-clj.core :as dashboard]
          [dashboard-clj.layouts.responsive-grid-layout :as grid]
          [dashboard-widgets.widgets.simple-text]
          [dashboard-widgets.widgets.github-repo-stats])
```

The dashboard currently supports only [grid layout](https://github.com/STRML/react-grid-layout), meaning that the initial configuration of the widgets can be configured according to a predefined grid system as specified in the readme of the grid layout project. 

The various configurations of the dashboard widgets are supplied through a hashmap containing the following sample structure:
```clojure
    {:layout :responsive-grid-layout
     :options {
              :layout-opts {:cols {:lg 6 :md 4 :sm 2 :xs 1 :xxs 1}}}
     :widgets [
                {
                 :type :github-repo-stats
                 :name :first-widget
                 :data-source :sample-dashboard-stats
                 :options {
                          :repo-name "Sample Dashboard"
                          :title "commits"
                          :text "commit history"}
                 :layout-opts {:position {:x 0 :y 0  :h 1 :w 2 }}}
                ...
                ]})
```

Here’s what the main keys stand are for:

* `:layout` - 
The type of layout to be used for the widget. Right now the only supported option are `:responsive-grid-layout` and `:grid-layout` (via [grid layout](https://github.com/STRML/react-grid-layout)). 

* `:layout-opts` - 
The height and width of the layout area in terms of generic grid units, for different screen sizes e.g. `:cols` 6 divides the entire space into 6 columns.

* `:widgets` -
A vector of individual widget configurations as explained below. There are no widgets available as part of the core library, but we might create another repo with most common widgets. Note: the library ```dashboard-widgets``` used in the example does not exist, yet!

So, you might want to define a widget or reuse widget. Here’s the hashmap structure for the widget configurations:

* `:type` -
What type of widget this is. For example, ```:simple-text```.

* `:name` -
An alias for the widget.

* `:data-source` -
The name of the data source. This is the same name that is assigned to the `:name` attribute of the `datasources` map that we encountered in the backend config earlier.

* `:options` -
Widget specific options. For example, title text for a simple text widget.

* `:layout-opts` -
This dictates the position of and area covered by the widget, e.g. `{:position {:x 0 :y 0  :h 1 :w 2 }}`. `x` and `y` for the top-left position of the `h` and `w` for the height and width. All are given in generic grid units(for :grid-layout or :responsive-grid-layout). This could change depending on the layout selected.


Once the configuration is set up, all you have to do is fire up the dashboard:
```clojure
(dashboard/start-dashboard dashboard "app")
```

`app` is the HTML id of the element into which the dashboard will be rendered. You will be able to access the at `http://localhost:5000`

###Creating a widget
Dashboard uses re-frame, and dashboard widget is just a reagent component. The component function must be a two arity function which will receive data(from the :datasource provided) and options(provided in the widget configuration). For example,

```clojure
(defn simple-text[data options]
   [:div
    [:div {:class "title-wrapper"}
     [:h3 {:class "title"} (get-in data [:data :title])]]
    [:div {:class "simple-text-widget"}
     [:div {:class "data"}
      [:p
       (get-in data [:data :text])]]]])
```

The widget has to be registered before you can use it,

```clojure
(dashboard-clj.widgets.core/register-widget :simple-text simple-text)
```

See [creating reagent components](https://github.com/Day8/re-frame/wiki/Creating-Reagent-Components) doc for additional details.

## Example
To get a better feel for the above documentation, here’s an [example project](https://github.com/kp2222/deliverit-dashboard) that has used `dashboard-clj` to implement a [simple Github repos summary dashboard](https://protected-wave-36452.herokuapp.com/).
