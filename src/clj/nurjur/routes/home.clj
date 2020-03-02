(ns nurjur.routes.home
  (:require
   [nurjur.layout :as layout]
   [nurjur.db.core :as db]
   [clojure.java.io :as io]
   [nurjur.middleware :as middleware]
   [ring.util.response]
   [ring.util.http-response :as response]))

(defn home-page [request]
  (layout/render request "home.html"))

(defn thanks-page []
  [:section.section>div.container>div.content
   [:h2 "Thanks"]])

(defn home-routes []
  [""
   {:middleware [middleware/wrap-csrf
                 middleware/wrap-formats]}
   ["/" {:get home-page}]

   ["/docs" {:get (fn [_]
                    (-> (response/ok (-> "docs/docs.md" io/resource slurp))
                        (response/header "Content-Type" "text/plain; charset=utf-8")))}]])

