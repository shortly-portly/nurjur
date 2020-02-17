(ns nurjur.core
 (:require [reagent.core :as r]
            [reitit.frontend :as rf]
            [reitit.frontend.easy :as rfe]
            [reitit.coercion :as rc]
            [reitit.coercion.spec :as rss]
            ))

(defonce match (r/atom nil))

(defn nav-link [title page]
  [:a.navbar-item
   {:href (rfe/href page)
    :class (when (= page (:name (:data @match)) "is-active"))}
   title])

(defn navbar []
  (r/with-let [expanded? (r/atom false)]
    [:nav.navbar.is-info>div.container
     [:div.navbar-brand
      [:a.navbar-item {:href "/" :style {:font-weight :bold}} "nurjur"]
      [:span.navbar-burger.burger
       {:data-target :nav-menu
        :on-click #(swap! expanded? not)
        :class (when @expanded? :is-active)}
       [:span][:span][:span]]]
     [:div#nav-menu.navbar-menu
      {:class (when @expanded? :is-active)}
      [:div.navbar-start
       [nav-link "Home" ::home]
       [nav-link "About" ::about]
       ]]]))

(defn about-page []
  [:section.section>div.container>div.content
   [:img {:src "/img/warning_clojure.png"}]])

(defn home-page []
  [:section.section>div.container>div.content
   [:h1 "Home Page"]])

;; -------------------------
;; Routes

(defn current-page []
  (navbar)
  (if @match
    (let [view (:view (:data @match))]
      [view])))

(def routes
  [["/"
    {:name ::home
     :view home-page}]
   ["/about"
    {:name ::about
     :view about-page}]])

(defn init! []
  (rfe/start!
   (rf/router routes)
   (fn [route]
     (reset! match route))
   {:user-fragment true})
  (r/render [navbar] (.getElementById js/document "navbar"))
  (r/render [current-page] (.getElementById js/document "app")))

(init!)
