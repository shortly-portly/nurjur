(ns nurjur.component
  (:require [reagent.core :as r]
            [reitit.frontend :as rf]
            [reitit.frontend.easy :as rfe]
            [reitit.coercion :as rc]
            [nurjur.db :as n]
            [struct.core :as st]
            [reitit.coercion.spec :as rss]))

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
  (-> @n/db
      (st/validate {field-name (field-name user-schema)})
      (first)
      (field-name)))

(defn section [content]
  [:section.section>div.container>div.content
   content])

(defn text-input [& {:as args}]
  (fn []
    (let [error? (and (:error-field args) ((:error-field args) @n/db) "is-danger")]
      [:div.field
       [:label.label (:label args)]
       [:div.control
        [:input.input
         {:type :text
          :class error?
          :name (:name args)
          :defaultValue (:name @n/db)
          :on-blur #((swap! n/db assoc (:name args) (-> % .-target .-value))
                     (swap! n/db assoc (:error-field args) (validate (:name args))))}]]

       (if-let [error-text (and (:error-field args) ((:error-field args) @n/db))]
         [:p.help.is-danger error-text])])))
