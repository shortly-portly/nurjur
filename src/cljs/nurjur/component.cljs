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
  "Determine if the given field is valid."
  (if (validate? field-name)
    (if-let [error (first (st/validate @n/db {field-name validations}))]
      (swap! n/db assoc :errors (merge (:errors @n/db) error))
      (swap! n/db assoc :errors (merge (:errors @n/db) {field-name nil})))))

(defn validate-fields [fields]
  "Validate all the fields passed in the collection"
  (for [field fields]
    (validate (:name field) (:validations field))))

(defn section [content]
  [:section.section>div.container>div.content
   content])

(defn text-input [args]
  (swap! n/db assoc :optional (merge (:optional @n/db) {(:name args) (:optional args)}))
  (js/console.log (:default args))
  (swap! n/db assoc (:name args) (:default args))
  (js/console.log @n/db)
  (fn []
    [:div.field
     [:label.label (:label args)]
     [:div.control
      [:input.input
       {:type :text
        :name (str (namespace (:name args)) "-" (name(:name args)))
        :defaultValue ((:name args) @n/db)
        :on-blur #((swap! n/db assoc (:name args) (-> % .-target .-value))
                   (validate (:name args) (:validations args)))}]

     (if-let [error-text (get-in @n/db [:errors (:name args)])]
       [:p.help.is-danger error-text])]]))

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

; Test data

(defn validate-length [length]
  {:message (str "Length must be greater than " length)
   :validate #(> (count %) length)})

(def test-fields
    {:fields
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
      :default "Simmons"
      :validations [st/required st/string (validate-length 2)]}]})
