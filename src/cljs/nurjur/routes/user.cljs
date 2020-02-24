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
  (fn []
    [:div

     [c/text-input
      :label "First Name"
      :name :first-name
      :validations [st/required st/string (validate-length 2)]
      ]

     [c/text-input
      :label "Age"
      :name :middle-name
      :optional true
      :validations [(validate-length 2) ]
      ]

     [c/text-input
      :label "Last Name"
      :name :last-name
      :validations [st/required st/string (validate-length 4)]
      ]

     [:div
      (if (get-in @n/db [:error :first-name])
                  [:h1 "error"]
                  [:h2 "no error"])]
      ]
     ))


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
