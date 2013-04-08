(ns helo.service.persons
(:require [helo.service.core :as core]
          [helo.data.persons :as pers]
          [cheshire.core :as json]))

;;TODO fix :id versus :uuid
(defn get-person [request]
  (println "service/get-person: " request))

(defn get-persons []
  (println "service/get-persons"))

(defn post-persons [{params :params}]
  (println "service/post-persons: " params)
  (pers/p-persons params)
  )

(defn update-person [request]
  (println "service/update-persons: " request))
