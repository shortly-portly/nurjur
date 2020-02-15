(ns nurjur.routes.user
  (:require
   [nurjur.layout :as layout]
   [nurjur.db.core :as db]
   [clojure.java.io :as io]
   [nurjur.middleware :as middleware]
   [ring.util.response]
   [ring.util.http-response :as response]
   [struct.core :as st]))

(def user-schema
  [[:first_name st/required st/string]
   [:last_name st/required st/string]
   [:email st/required st/email]])

(defn validate-user [params]
  (first (st/validate params user-schema)))

(def params {:first_name "Dave" :last_name "Simmons" :email "dave@email.com"})
