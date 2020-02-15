(ns nurjur.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[nurjur started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[nurjur has shut down successfully]=-"))
   :middleware identity})
