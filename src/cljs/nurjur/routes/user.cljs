(ns nurjur.routes.user
  (:require [reitit.frontend.easy :as rfe]
            [nurjur.component :as c]
            [reagent.core :as r]))

(defn validate [ratom]
  (if (< (count (:first-name @ratom)) 3)
    (swap! ratom assoc :error "name was too short")
    (swap! ratom assoc :error nil)))

(defn user-form []
  (let [fields (r/atom {:first-name "Dave"})]
    (fn []
      [:div
       [:div.field
        [:label.label "First Name"]
        [:div.control
         [:input.input
          {:type :text
           :name :first-name
           :defaultValue (:first-name @fields)
           :on-blur #((swap! fields assoc :first-name (-> % .-target .-value))
                      (validate fields))}]]]

       [:div
        [:h4 (:error @fields)]]

       [:div.field
        [:label.label "Second Name"]
        [:div.control
         [:input.input
          {:type :text
           :name :second-name
           :value (:second-name @fields)
           :on-change #(swap! fields assoc :second-name (-> % .-target .-value))}]]]
       [:h5 "First Name is: " (:first-name @fields)]
       [:h5 "The number of characters in First Name is: " (count (:first-name @fields))]])))

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
