(ns helo.service.search
  (:require [helo.service.core :as core]
            [helo.data.search :as search]
            [cheshire.core :as json]
  ))

(defn search-orgs [{params :params}]
  (search/search-orgs params))

(defn search-orgs-list [{params :params}]
  (search/search-orgs-list params))

(defn search-people [{params :params}]
  (search/search-people params))


(defn search-acct-mgr [{params :params}]
  (search/search-acct-mgr params))
