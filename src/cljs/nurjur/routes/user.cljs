(ns nurjur.routes.user)

(defn user-routes []
  [["/user" :user]
  ["/user/new" :user-new]])
