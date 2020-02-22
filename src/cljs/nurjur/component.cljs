(ns nurjur.component
  (:require [reagent.core :as r]
            [reitit.frontend :as rf]
            [reitit.frontend.easy :as rfe]
            [reitit.coercion :as rc]
            [nurjur.db :as n]
            [struct.core :as st]
            [reitit.coercion.spec :as rss]))


(defn validate [field-name validations]
  (js/console.log "Entered validate")
  (js/console.log field-name)
  (if-let [error-msg (field-name (first (st/validate @n/db {field-name validations})))]
     (assoc-in @n/db [:errors field-name] error-msg)
    (do
      ((js/console.log "All good")
       (update-in @n/db [:errors field-name] dissoc field-name)))))

(defn section [content]
  [:section.section>div.container>div.content
   content])

(defn text-input [& {:as args}]
  (fn []
    (let [error? (if (get-in @n/db [:errors (:name args)]) "is-danger")]
      [:div.field
       [:label.label (:label args)]
       [:div.control
        [:input.input
         {:type :text
          :class error?
          :name (:name args)
          :defaultValue (:name @n/db)
          :on-blur #((swap! n/db assoc (:name args) (-> % .-target .-value))
                     (swap! n/db assoc :errors (validate (:name args) (:validations args))))}]]

       (if-let [error-text (and (:error-field args) ((:error-field args) @n/db))]
         [:p.help.is-danger error-text])])))
