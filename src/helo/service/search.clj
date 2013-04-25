(ns helo.service.search
  (:require [helo.service.core :as core]
            [helo.data.search :as search]
            [cheshire.core :as json]
  ))

(defn get-orgs [{params :params}]
  (search/search-orgs params))

