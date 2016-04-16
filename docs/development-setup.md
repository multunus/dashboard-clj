# Developing dashboard-clj

## Developing a library and a host application at the same time

The trickier part in developing dashboard-clj is that dashboard is a library project that is intended to be used inside a host application. There are potentially many ways to go about this

* During development, simulate the host application as a separate namespace within dashboard itself. The danger of this approach is that we might end up in a situation where these two apps are indistinguishable from each other because it is easy forget about the separation since both of these apps reside with in the same code base.

* Develop as two separate application. Add a dashboard clj as lein dependency and use lein install on dashboard to make the jar available locally. The disadvantage of this approach is that we will have to do a lein install each time we make a change to dashboard this makes it too difficult to get quick feedback.

* Use lein checkouts dependencies. This seems to be the recommended way of developing host and library projects at the same time. One disadvantage seems to be that lein cljsbuild does not watch the changes in the checkouts dependencies as of now. This seems to known issue as of now. The only workaround for this problem seems to be to save a cljs file in the host application after a change in dashboard, this may not be ideal but this should do it for now.