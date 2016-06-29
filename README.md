# dashboard-clj

A clojure mini-framework to setup dashboards inspired from [dashing](http://dashing.io/).

Visualisation is a key part of analysing data and making decisions. But, setting up a dashboard involves the following challenges:

- Fetching data
- Scheduling data retrieval
- Transferring data to the client
- Visualizing

###Fetching

Your Clojure code to retrieve data goes here. Every such function(data source) should have a unique name.

###Scheduling

In order to re-fetch data at a certain interval, we use [immutant's scheduler](http://immutant.org/tutorials/scheduling/). Every data source can optionally have a schedule. Data source fetching will happen at configured intervals and data gets sent to the client.

###Transferring data

[Sente](https://github.com/ptaoussanis/sente) is a great websocket library that we use to broadcast the data fetched by every data source to the clients. On the client side we use [re-frame](https://github.com/Day8/re-frame), and the data will be added to the re-frame's app-db.

###Visualising

We are still figuring this part out. We are shipping a basic layout (react grid layout) but not shipping any widgets yet. Since widgets are just reagent components, they are easy to setup. Above all, tons of great libraries for visualisation can be found [here](http://cljsjs.github.io/). And [reagent cookbook](https://github.com/reagent-project/reagent-cookbook) talks in depth on how to setup common libraries.

![Screenshot] (https://db.tt/AtDsHTHP)

## [Demo](https://protected-wave-36452.herokuapp.com/)
## [Setup Doc](https://github.com/multunus/dashboard-clj/blob/master/docs/setting-up-dashboards.md)

##About
![Multunus log](https://camo.githubusercontent.com/c0701d8866d0962ddc36db56dbf1ce93d712800e/68747470733a2f2f73332e616d617a6f6e6177732e636f6d2f6d756c74756e75732d696d616765732f4d756c74756e75735f4c6f676f5f566563746f725f726573697a65642e706e67)
This is side effect of trying to learn Clojure during our [20% time](http://www.multunus.com/blog/2016/01/20-investment-time-background-story/). 