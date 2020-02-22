(ns nurjur.routes.user
  (:require [reitit.frontend.easy :as rfe]
            [nurjur.component :as c]
            [reagent.core :as r]
            [nurjur.db :as n]
            [struct.core :as st]))



(def user-schema
  {:first-name [st/required
                st/string
                {:message "must be longer than 5 characters"
                 :validate #(> (count %) 5)}]

   :last-name [st/required
               st/string
               {:message "wrong, wrong wrong"
                :validate #(> (count %) 5)}]})
(def validate-length
  {:message "wrong wrong wrong"
   :validate #(> (count %) 5)})

(defn user-form []
  (fn []
    [:div

     [c/text-input
      :label "First Name"
      :name :first-name
      :validations [st/required st/string validate-length]
      :error-field :first-name-error
      ]

     [c/text-input
      :label "Last Name"
      :name :last-name
      :validations [st/required st/string validate-length]
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
