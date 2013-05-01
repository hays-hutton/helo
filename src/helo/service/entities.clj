(ns helo.service.entities
  (:require [helo.service.core :as core]
            [helo.data.entities :as ents]
            [cheshire.core :as json]
  ))

(defn get-entities [{params :params}]
  (ents/get-entities params))

