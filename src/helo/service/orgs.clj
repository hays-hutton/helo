(ns helo.service.orgs
  (:require [helo.service.core :as core])
  (:require [cheshire.core :as json]))

(defn get-org [request]
  (println "service/get-org: " request))

(defn get-orgs []
  (println "service/get-orgs"))

(defn post-orgs [request]
  (println "service/post-orgs: " request))

(defn update-org [request]
  (println "service/update-org: " request))
