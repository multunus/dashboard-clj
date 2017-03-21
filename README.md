# dashboard-clj

[![Join the chat at https://gitter.im/multunus/dashboard-clj](https://badges.gitter.im/multunus/dashboard-clj.svg)](https://gitter.im/multunus/dashboard-clj?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

A clojure mini-framework to build dashboards inspired by [dashing](http://dashing.io/).

A lot of plumbing work goes into building a dashboard. Setting up a dashboard involves the following challenges:

- Fetching data
- Scheduling data retrieval
- Transferring data to the client
- Visualizing
- Deployment

### Fetching

Data source, used for fetching data, is just a clojure function! The scheduler will run the function and broadcast the result to the client side as per the schedule. We take care of caching the data so that a client who joins later can immediately use the last results available.

### Scheduling

In order to periodically update the dashboard with the latest data, every data source can optionally have a schedule. Data gets fetched and sent to the client at the configured intervals.

Here is an example configuration for a data source with schedule,

```clojure
{
  :name :sample-dashboard-stats
  :read-fn :sample-dashboard.fetcher/fetch
  :params ["multunus" "dashboard-clj"]
  :schedule {
    :in [0 :seconds]
    :every [5 :minutes]}}
```

### Transferring data

[Sente](https://github.com/ptaoussanis/sente) is a great websocket library that we use to broadcast the data fetched by every data source to the clients. On the client side, we use [re-frame](https://github.com/Day8/re-frame), and the data will be cached on the client - this happens under the hood. The widgets will be re-rendered when data changes on the client side. So, no confguration is required here.

### Visualizing

We are still trying to improve this part. We are shipping a basic layout(react grid layout) but not any widgets yet. We found that widgets and their look and feel vary greatly, and we are still pondering over a good common set of widgets to ship. 

Since widgets are just reagent components, they are easy to create. Above all, tons of great libraries can be found [here](http://cljsjs.github.io/). In addition, the [reagent cookbook](https://github.com/reagent-project/reagent-cookbook) talks in depth on how to setup common libraries.

### Deployment

We can host this dashboard on Heroku in no time! Read about it [here](https://devcenter.heroku.com/articles/deploying-clojure-applications-with-the-heroku-leiningen-plugin).


![Screenshot] (https://db.tt/Z5WnyEpj)

## [Demo](https://protected-wave-36452.herokuapp.com/)
## [Usage](https://github.com/multunus/dashboard-clj/blob/master/docs/setting-up-dashboards.md)

## About

[![Multunus logo](https://camo.githubusercontent.com/c0701d8866d0962ddc36db56dbf1ce93d712800e/68747470733a2f2f73332e616d617a6f6e6177732e636f6d2f6d756c74756e75732d696d616765732f4d756c74756e75735f4c6f676f5f566563746f725f726573697a65642e706e67)](http://www.multunus.com/?utm_source=github)

dashboard-clj is maintained and funded by Multunus Software Pvt. Ltd.
The names and logos for Multunus are trademarks of Multunus Software Pvt. Ltd.

We built this as part of learning Clojure in our [20% time](http://www.multunus.com/blog/2016/01/20-investment-time-background-story/). We will be supporting this library during our investment time and using it to create internal dashboards at Multunus.
