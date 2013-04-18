(ns helo.service.notes
  (:require [helo.service.core :as core]
            [helo.data.notes :as notes]
            [cheshire.core :as json]))

(defn get-note [request]
  (println "service/get-note: " request))

(defn get-notes [{params :params}]
  (notes/read-notes params)
)

(defn post-notes [{params :params}]
  (println "service/post-notes: " params)
  (notes/create-note params )
)
(defn update-note [request]
  (println "service/update-note: " request))
