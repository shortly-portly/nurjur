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

(defn validate-fields! [fields]
  "Validate all the fields passed in the collection.

  Note the use of doseq which is required as we are not using the returning values simply
  forcing the update of the db."
  (doseq [field fields] (validate-field! (:name field) (:validations field))))

(defn validate-form! [fields form-validations]
  "Pefrom cross field validations")

(defn form-valid? [fields]
  (validate-fields! fields)
  ;(validate-form! (:fields args) (:form-validations args))
  (empty? (:errors @n/db)))

(defn post-data! []
  (js/console.log "token " js/csrfToken)
  (POST "/thanks"
    {:params {:first-name (:fist-name @n/db)
              :middle-name (:middle-name @n/db)
              :last-name (:last-name @n/db)
              :__anti-forgery-token js/csrfToken}
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
    [:div.field
     [:label.label (:label args)]
     [:div.control
      [:input.input
       {:type :text
        :name (str (namespace (:name args)) "-" (name (:name args)))
        :defaultValue ((:name args) @n/db)
        :on-blur #((swap! n/db assoc (:name args) (-> % .-target .-value))
                   (validate-field! (:name args) (:validations args)))}]

      (if-let [error-text (get-in @n/db [:errors (:name args)])]
        [:p.help.is-danger error-text])]]))

(defn form [args]
  (fn []
    [:form
     (for [field (:fields args)]
       [text-input field])

     [:button.button.is-primary
      {:on-click
       #(do
          (.preventDefault %)
          (if (form-valid? (:fields args))
            (post-data!)
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
