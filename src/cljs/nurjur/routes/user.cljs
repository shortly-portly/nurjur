(ns nurjur.routes.user
  (:require [reitit.frontend.easy :as rfe]
            [nurjur.component :as c]
            [reagent.core :as r]
            [nurjur.db :as n]
            [struct.core :as st]))

(defn validate-length [length]
  {:message (str "Length must be greater than " length)
   :validate #(> (count %) length)})

(defn user-form []
  (c/form
   {:form-name :user/form
    :fields
    [{:type :text
      :label "First Name3"
      :name :user/first-name
      :default "David"
      :validations [st/required st/string (validate-length 2)]}
     {:type :text
      :label "Middle Name"
      :optional true
      :name :user/middle-name
      :validations [st/required st/string (validate-length 2)]}
     {:type :text
      :label "Last Name"
      :name :user/last-name
      :validations [st/required st/string (validate-length 2)]}]}))

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
