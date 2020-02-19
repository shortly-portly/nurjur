(ns nurjur.routes.user
  (:require [reitit.frontend.easy :as rfe]
            [nurjur.component :as c]))

(defn post-page []
  [c/section
   [:div
   [:h2 "Welcome to the User Page"]


   [c/input nil "First Name"]
   [c/input "Last Name" "Last Name"]
   [c/input "Email" "Email"]

  [:a
   {:href (rfe/href ::thanks) }
   "a link"]
   ]])

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
