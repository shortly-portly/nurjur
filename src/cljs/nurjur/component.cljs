(ns nurjur.component
  (:require [reagent.core :as r]
            [reitit.frontend :as rf]
            [reitit.frontend.easy :as rfe]
            [reitit.coercion :as rc]
            [nurjur.db :as n]
            [struct.core :as st]
            [reitit.coercion.spec :as rss]))

(defn validate? [field-name]
  "Determine if the given field should be validated. An optional field will be validated if it contains data."
  (or (seq (field-name @n/db))
      (not (get-in @n/db [:optional field-name]))))

(defn validate [field-name validations]
  (if (validate? field-name)
    (if-let [error (first (st/validate @n/db {field-name validations}))]
      error
      {field-name nil})
    {field-name nil}))

(defn validate-form [form-name])

(defn section [content]
  [:section.section>div.container>div.content
   content])

(defn text-input [args]
  (swap! n/db assoc :optional (merge (:optional @n/db) {(:name args) (:optional args)}))
  (fn []
    [:div.field
     [:label.label (:label args)]
     [:div.control
      [:input.input
       {:type :text
        :name (str (namespace (:name args)) "-" (name(:name args)))
        :defaultValue (:name @n/db)
        :on-blur #((swap! n/db assoc (:name args) (-> % .-target .-value))
                   (swap! n/db assoc :error (merge (:error @n/db) (validate (:name args) (:validations args)))))}]]

     (if-let [error-text (get-in @n/db [:error (:name args)])]
       [:p.help.is-danger error-text])]))

(defn form [ args]
  (fn []
    [:form
     (for [field (:fields args)]
       [text-input field])

     [:button.button.is-primary {:on-click
                                 #(do
                                   (.preventDefault %)
                                   (js/console.log "click"))}
      "wibble2"]

     ]))
