(ns nurjur.component
  (:require [reagent.core :as r]
            [reitit.frontend :as rf]
            [reitit.frontend.easy :as rfe]
            [reitit.coercion :as rc]
            [reitit.coercion.spec :as rss]
            ))

(defn section [content]
  (println (str "content.." content))
  [:section.section>div.container>div.content
   [:h1 "from section"]
   [:h2 "another bit from section"]
   content
   ])

(defn input [label placeholder]

   [:div.field
    (if label [:label.label label])
    [:div.control
    [:input.input {:type "text"
                 :placeholder placeholder}]]]
  )
