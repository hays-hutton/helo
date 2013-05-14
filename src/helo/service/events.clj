(ns helo.service.events
  (:require [helo.service.core :as core]
            [helo.data.events :as events]
            [cheshire.core :as json]))

(defn get-events [{params :params}]
  (events/read-events params))

