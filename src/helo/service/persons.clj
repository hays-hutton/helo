(ns helo.service.persons
(:require [helo.service.core :as core]
          [cheshire.core :as json]))

;;TODO fix :id versus :uuid
(defn get-person [request]
  (println "service/get-person: " request))

(defn get-persons []
  (println "service/get-persons"))

(defn post-persons [request]
  (println "service/post-persons: " request))

(defn update-person [request]
  (println "service/update-persons: " request))
