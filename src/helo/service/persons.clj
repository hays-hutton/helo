(ns helo.service.persons
(:require [helo.service.core :as core]
          [helo.data.persons :as pers]
          [cheshire.core :as json]))

;;TODO fix :id versus :uuid
(defn get-person [request]
  (println "service/get-person: " request))

(defn get-persons []
  (println "service/get-persons"))

;POST to /persons -> Create a Person
(defn post-persons [{params :params}]
  (let [params* (core/assoc-geocode params)]
    (pers/create-person params*)))

(defn update-person [request]
  (println "service/update-persons: " request))
