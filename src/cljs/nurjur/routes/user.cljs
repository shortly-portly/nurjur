(ns nurjur.routes.user
  (:require [reitit.frontend.easy :as rfe]
            [nurjur.component :as c]
            [reagent.core :as r]
            [struct.core :as st]))

(def fields (r/atom {}))

(def user-schema
  {:first-name [st/required
                st/string
                {:message "must be longer than 5 characters"
                 :validate #(> (count %) 5)}]

   :last-name [st/required
               st/string
               {:message "wrong, wrong wrong"
                :validate #(> (count %) 5)}]})

(defn validate [field-name]
  (-> @fields
      (st/validate {field-name (field-name user-schema)})
      (first)
      (field-name)))

(defn user-form []
  (fn []
    [:div

     [c/text-input fields
      :label "First Name"
      :name :first-name
      :error-field :first-name-error
      :on-blur #((swap! fields assoc :first-name (-> % .-target .-value))
                 (swap! fields assoc :first-name-error (validate :first-name)))]

     [c/text-input fields
      :label "Last Name"
      :name :last-name
      :error-field :last-name-error
      :on-blur #((swap! fields assoc :last-name (-> % .-target .-value))
                 (swap! fields assoc :last-name-error (validate :last-name)))]]))

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
