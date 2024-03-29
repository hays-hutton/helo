(ns helo.service.persons
(:require [helo.service.core :as core]
          [helo.data.persons :as pers]
          [cheshire.core :as json]))

(defn get-person [id]
  (println "service/get-person: " id)
  (pers/read-person id))

(defn get-persons [{params :params}]
  (println "service/get-persons")
  (pers/read-persons params))

;POST to /persons -> Create a Person
(defn post-persons [{params :params}]
  (let [params* (core/assoc-geocode params)]
    (pers/create-person params*)))

(defn update-person [request]
  (println "service/update-persons: " request))
