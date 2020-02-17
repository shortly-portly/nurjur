(ns nurjur.routes.user
  (:require [reitit.frontend.easy :as rfe]))

(defn post-page []
  [:section.section>div.container>div.content
   [:h2 "Welcome to the User Page"]
  [:a
   {:href (rfe/href ::thanks) }
   "a link"]
   ])

(defn thanks-page []
  [:section.section>div.container>div.content
   [:h2 "Thanks"]]
  )

(def user-routes
  [""
   ["/post" {:name ::post
             :view post-page}]
   ["/thanks" {:name ::thanks
               :view thanks-page}]])
