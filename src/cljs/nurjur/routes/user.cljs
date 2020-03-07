(ns nurjur.routes.user
  (:require [reitit.frontend.easy :as rfe]
            [nurjur.component :as c]
            [reagent.core :as r]
            [nurjur.db :as n]
            [struct.core :as st]))

(defn validate-length [length]
  {:message (str "Length must be greater than " length)
   :validate #(> (count %) length)})

(defn passwords-match [password repeat-password]
  {:message "Passwords do not match"
   :validate (fn []
               (= password repeat-password))})

(def user-schema
  [[:user/first-name st/required st/string]
   [:user/last-name st/required st/string]
   [:user/password st/required st/string]
   [:user/repeat-password st/required st/string]
   [:user/password st/identical-to :user/repeat-password]])

(defn user-form []
  (c/form
   {:form-name :user/form
    :fields
    [{:type :text
      :label "First Name"
      :name :user/first-name
      :validations [st/required st/string]}

     {:type :text
      :label "Last Name"
      :name :user/last-name
      :validations [st/required st/string]}

     {:type :text
      :label "Password"
      :name :user/password
      :validations [st/required st/string]}

     {:type :text
      :label "Repeat Password"
      :name :user/repeat-password
      :validations [st/required st/string]}]

    :validations [:user/password [st/identical-to :user/repeat-password]]}))

(defn post-page []
  [c/section
   [:div
    [:h2 "Welcome to the User Page"]
    [user-form]]])

(defn thanks-page []
  [:section.section>div.container>div.content
   [:h2 "Thanks"]])

(def user-routes
  ["/users"
   ["/post" {:name ::post
             :view post-page}]
   ["/thanks" {:name ::thanks
               :view thanks-page}]])
