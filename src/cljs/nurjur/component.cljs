(ns nurjur.component
  (:require [reagent.core :as r]
            [reitit.frontend :as rf]
            [reitit.frontend.easy :as rfe]
            [reitit.coercion :as rc]
            [reitit.coercion.spec :as rss]))

(defn section [content]
  [:section.section>div.container>div.content
   [:h1 "from section"]
   [:h2 "another bit from section"]
   content])

(defn text-input [ratom & {:as args}]
  (fn []
    (let [error? (and (:error-field args) ((:error-field args) @ratom) "is-danger") ]
    [:div.field
     [:label.label (:label args)]
     [:div.control
      [:input.input
       {:type :text
        :class error?
        :name (:name args)
        :defaultValue (:name @ratom)
        :on-blur (:on-blur args)}]]

     (if-let [error-text (and (:error-field args) ((:error-field args) @ratom))]
       [:p.help.is-danger error-text])])))
