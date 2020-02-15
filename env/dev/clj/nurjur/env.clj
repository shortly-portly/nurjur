(ns nurjur.env
  (:require
    [selmer.parser :as parser]
    [clojure.tools.logging :as log]
    [nurjur.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[nurjur started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[nurjur has shut down successfully]=-"))
   :middleware wrap-dev})
