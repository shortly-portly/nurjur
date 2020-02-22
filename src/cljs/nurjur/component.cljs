(ns nurjur.component
  (:require [reagent.core :as r]
            [reitit.frontend :as rf]
            [reitit.frontend.easy :as rfe]
            [reitit.coercion :as rc]
            [nurjur.db :as n]
            [struct.core :as st]
            [reitit.coercion.spec :as rss]))

(defn validate [field-name validations]
  (if-let [error (first (st/validate @n/db {field-name validations}))]
    error
    {field-name nil}))

(defn section [content]
  [:section.section>div.container>div.content
   content])

(defn text-input [& {:as args}]
  (fn []
    [:div.field
     [:label.label (:label args)]
     [:div.control
      [:input.input
       {:type :text
        :name (:name args)
        :defaultValue (:name @n/db)
        :on-blur #((swap! n/db assoc (:name args) (-> % .-target .-value))
                   (swap! n/db assoc :error (merge (:error @n/db) (validate (:name args) (:validations args)))))}]]

     (if-let [error-text (get-in @n/db [:error (:name args)])]
       [:p.help.is-danger error-text])]))
