(ns helo.data.search
  (:use [datomic.api :only [db q] :as d])
  (:require [helo.data.core :as core]
            [helo.utils.uri :as utils]
            [cheshire.core :as json]
            [clojure.tools.logging :only [info error]]))

(def ez-to-attr
  {:name :name
   :phone :phone
   :email :email
   :fax :fax
   :orgType :org/type
   :parent :org/parent
   :acctMgr :org/acct-mgr
   :note :note/note
   :address/address :address/address
   :address/street :address/street
   :address/city :address/city
   :address/region :address/region
   :address/postal-code :address/postal-code
   :address/latitude :address/latitude
   :address/longitude :address/longitude
   :address/provider :address/provider
   :who :who
  })


(defn ent-to-org-map [entity]
  {:id (:db/id entity)
   :name (:name entity)
   :parent (:db/id (:parent entity))
   :parentName (:name (:parent entity))
   :acctMgr (:db/id (:acct-mgr entity ))
   :acctMgrName (:name (:acct-mgr entity))
   :street (:address/street entity)
   :city (:address/city entity)
   :state (:address/region entity)
   :zip (:address/postal-code entity)
   :lat (:address/latitude entity)
   :lon (:address/longitude entity)
   :orgType (:org/type entity)
   :updated (:updated entity)
   :created (:created entity)
   :type (:type entity)
  })

(defn build-query [handler ent-type]
  (fn [params]
    (let [q-map {:query '{:find [?e ?updated ?name ?region]
                            :in [$ ?search ?type]
                         :where [[?e :type ?type]
                                 [(fulltext $ :name ?search) [[?e ?name]]]
                                 [?e :updated ?updated]
                                 [?e :address/region ?region]]}
                 :dbs []}
          search (:search params)]
       (println "gonna" search)
       (handler (update-in q-map [:dbs] concat [search ent-type])))))

(defn gen-response [handler]
  (fn [params]
    (let [response (handler params)
          results (group-by #(nth % 3) (:results response)) ]
      (println "Response:" response)
      (println "Results:" results)
       response
      )))

(defn strip-outer-vec [handler]
  (fn [v]
    (let [m (first v)]
      (handler m))))

(def search-orgs
  (-> (core/query)
      (build-query :type/org)
      (gen-response)
      (strip-outer-vec)
      (core/remove-empty-keys)))

