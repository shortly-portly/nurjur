(ns nurjur.component
  (:require [reagent.core :as r]
            [reitit.frontend :as rf]
            [reitit.frontend.easy :as rfe]
            [reitit.coercion :as rc]
            [nurjur.db :as n]
            [struct.core :as st]
            [reitit.coercion.spec :as rss]
            [ajax.core :refer [GET POST]]))

(defn validate? [field-name]
  "Determine if the given field should be validated. An optional field will be validated if it contains data."
  (or (seq (field-name @n/db))
      (not (get-in @n/db [:optional field-name]))))

(defn validate-field! [field-name validations]
  "Determine if the given field is valid."
  (if (validate? field-name)
    (if-let [error (first (st/validate @n/db {field-name validations}))]
      (swap! n/db assoc :errors (merge (:errors @n/db) error))

      (swap! n/db assoc :errors (dissoc (:errors @n/db) field-name)))))

(defn form-valid? [form-validations]
  (swap! n/db dissoc :errors)

  (if-let [error (first (st/validate @n/db form-validations))]
    (swap! n/db assoc :errors (merge (:errors @n/db) error)))

  (empty? (:errors @n/db)))

(defn post-data! [fields]
  (POST "/thanks"
    {:params (into {} (for [field fields] {(:name field) ((:name field) @n/db)}))
     :format :json
     :headers {"Accept" "application/transit+json"
               "x-csrf-token" js/csrfToken}
     :handler #(js/console.log (str "response " %))
     :error-handler #(js/console.log (str "error " %))}))

(defn section [content]
  [:section.section>div.container>div.content
   content])

(defn text-input [args]
  (swap! n/db assoc :optional (merge (:optional @n/db) {(:name args) (:optional args)}))
  (swap! n/db assoc (:name args) (:default args))
  (fn []
    (let [field-label (:label args)
          field-name (:name args)]
      [:div.field
       [:label.label field-label]
       [:div.control
        [:input.input
         {:type :text
          :name (str (namespace field-name) "-" (name field-name))
          :defaultValue (field-name @n/db)
          :on-blur #((swap! n/db assoc field-name (-> % .-target .-value))
                     (validate-field! field-name (:validations args)))}]

        (if-let [error-text (get-in @n/db [:errors (:name args)])]
          [:p.help.is-danger error-text])]])))

(defn form [args]
  (fn []
    [:form
     (for [field (:fields args)]
       [text-input field])

     [:button.button.is-primary
      {:on-click
       #(do
          (.preventDefault %)
          (if (form-valid? (:validations args))
            (post-data! (:fields args))
            (js/console.log "form invalid")))}
      "wibble2"]]))

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

(def args-2
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
     :validations [st/required st/string (validate-length 2)]}]})

(def user-schema
  [[:user/first-name st/required st/string]
   [:user/last-name st/required st/string]
   [:user/password st/required st/string]
   [:user/repeat-password st/required st/string]])
