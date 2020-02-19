(ns nurjur.routes.user
  (:require [reitit.frontend.easy :as rfe]
            [nurjur.component :as c]
            [reagent.core :as r]))

(def fields (r/atom {}))


(defn input [name label]
(fn []
  [:div
   [:div.field
    [:label.label label]
    [:div.control
     [:input.input
      {:type :text
       :name name
       :value (name @fields)
       :on-change #(swap! fields assoc name (-> % .-target .-value))
       }]]]]))

(defn user-form []
(fn []
      [:div
       [input :first-name "First Name" fields]
       [input :last-name "Last Name" fields]
       [input :email "Email" fields]

[:h5 "First Name is: " (:first-name @fields)]
       [:h5 "The number of characters in First Name is: " (count (:first-name @fields))]]))

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
