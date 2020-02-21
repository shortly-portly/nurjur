(ns nurjur.routes.user
  (:require [reitit.frontend.easy :as rfe]
            [nurjur.component :as c]
            [reagent.core :as r]
            [nurjur.db :as n]
            [struct.core :as st]))



(defn user-form []
  (fn []
    [:div

     [c/text-input
      :label "First Name"
      :name :first-name
      :error-field :first-name-error
      ]

     [c/text-input
      :label "Last Name"
      :name :last-name
      :error-field :last-name-error]]))

(defn post-page []
  [c/section
   [:div
    [:h2 "Welcome to the User Page"]
    [user-form]]])

(defn thanks-page []
  [:section.section>div.container>div.content
   [:h2 "Thanks"]])

(def user-routes
  [""
   ["/post" {:name ::post
             :view post-page}]
   ["/thanks" {:name ::thanks
               :view thanks-page}]])
