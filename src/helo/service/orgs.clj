(ns helo.service.orgs
  (:require [helo.service.core :as core]

)

  (:require [cheshire.core :as json]))

(defn get-org [request]
  (println "service/get-org: " request))


(defn get-orgs [request]
  (println "service/get-orgs" request)
{:status 200
 :headers {"Content-Type" "application/json"}
   :body (json/encode {:results [{ :text "AR" :children [ {:id 1 :text "State Farm"} {:id 2 :text "Allstate"}]}
                                 {:children [ {:id 4 :text "State Farm"} {:id 5 :text "Holdridge"}] :text "MO"}]})
  }
)

(defn post-orgs [request]
  (println "service/post-orgs: " request))

(defn update-org [request]
  (println "service/update-org: " request))
