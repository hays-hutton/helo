(ns helo.service.orgs
  (:require [helo.service.core :as core]
            [helo.data.orgs :as orgs]
            [cheshire.core :as json]
  ))

(defn get-org [id]
  (orgs/read-org id))

(defn get-orgs [{params :params}]
  (orgs/read-orgs params))

(defn post-orgs [{params :params}]
  (println "service/post-orgs: " params)
  (let [params* (core/assoc-geocode params)]
    (println "service/post-orgs::assoc-geo" params*)
    (orgs/create-org params*)))

(defn update-org [request]
  (println "service/update-org: " request))
