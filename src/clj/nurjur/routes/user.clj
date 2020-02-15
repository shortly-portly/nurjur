(ns nurjur.routes.user
  (:require
   [nurjur.layout :as layout]
   [nurjur.db.core :as db]
   [clojure.java.io :as io]
   [nurjur.middleware :as middleware]
   [ring.util.response]
   [ring.util.http-response :refer :all]
   [struct.core :as st])
  (:use hiccup.page hiccup.element hiccup.core))

(def user-schema
  "Define the schema rules for a user."
  [[:first_name st/required st/string]
   [:last_name st/required st/string]
   [:email st/required st/email]])

(defn validate-user [params]
  "Validate the given params map against a user schema."
  (first (st/validate params user-schema)))

(defn create-user! [{:keys [params]}]
  "Validate and save a user record."
  (if-let [errors (validate-user params)]
    (-> (found "/users/new")))
  (do
    (db/create-user! params)
    (found "/")))

(defn index-page [request]
  (content-type (ok (html
   [:h1 "Welcome Users"])) "text/html; charset=utf-8"))

(defn user-routes []
  [""
   {:middleware [middleware/wrap-csrf
                 middleware/wrap-formats]}
   ["/users" {:get index-page}]
   ])

(comment
  (def params {:first_name "Dave" :last_name "Simmons" :email "dave@email.com"}))
