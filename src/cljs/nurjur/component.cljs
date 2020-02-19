(ns nurjur.component
  (:require [reagent.core :as r]
            [reitit.frontend :as rf]
            [reitit.frontend.easy :as rfe]
            [reitit.coercion :as rc]
            [reitit.coercion.spec :as rss]
            ))

(defn section [content]
  [:section.section>div.container>div.content
   [:h1 "from section"]
   [:h2 "another bit from section"]
   content
   ])

