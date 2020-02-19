(ns nurjur.core
  (:require [reagent.core :as r]
            [reitit.frontend :as rf]
            [reitit.frontend.easy :as rfe]
            [reitit.coercion :as rc]
            [reitit.coercion.spec :as rss]
            [nurjur.routes.user :as user]
            [nurjur.component :as c]))

(defonce match (r/atom nil))

(defn nav-link [title page]
  [:a.navbar-item
   {:href (rfe/href page)
    :class (when (= page (:name (:data @match)) "is-active"))}
   title])

(defn navbar [links]
  (r/with-let [expanded? (r/atom false)]
    [:nav.navbar.is-info>div.container
     [:div.navbar-brand
      [:a.navbar-item {:href "/" :style {:font-weight :bold}} "nurjur"]
      [:span.navbar-burger.burger
       {:data-target :nav-menu
        :on-click #(swap! expanded? not)
        :class (when @expanded? :is-active)}
       [:span] [:span] [:span]]]
     [:div#nav-menu.navbar-menu
      {:class (when @expanded? :is-active)}
      [:div.navbar-start
       (for [link links]
         ^{:key link} link)]]]))
(def links
  [[nav-link "Home" ::home]
   [nav-link "About" ::about]
   [nav-link "foo"  ::user/post]])

(defn about-page []
  [c/section
   [:div
   [:h1 "About"]
   [:h2 "Another bit"]]])

(defn home-page []
  [c/section
   [:h1 "Home Page"]])

;; -------------------------
;; Routes

(defn current-page []
  (navbar links)
  (if @match
    (let [view (:view (:data @match))]
      [view])))

(def routes
  [["/"
    {:name ::home
     :view home-page}]
   ["/about"
    {:name ::about
     :view about-page}]
   ["/user" user/user-routes]])

(defn init! []
  (rfe/start!
   (rf/router routes)
   (fn [route]
     (reset! match route))
   {:user-fragment true})
  (r/render [navbar links] (.getElementById js/document "navbar"))
  (r/render [current-page] (.getElementById js/document "app")))

(init!)

;; --------------------
;; Scratch
