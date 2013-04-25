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

(defn build-query
 ([handler ent-type] 
  (fn [params]
    (let [q-map {:query '{:find [?e ?updated ?name ?region]
                            :in [$ ?search ?type]
                         :where [[?e :type ?type]
                                 [(fulltext $ :name ?search) [[?e ?name]]]
                                 [?e :updated ?updated]
                                 [?e :address/region ?region]]}
                 :dbs []}
          search (:q params)]
       (println "gonna" search)
       (handler (update-in q-map [:dbs] concat [search ent-type]))))) 
  ([handler ent-type sub-type sub-type-val]
    (fn [params]
      (let [q-map {:query '{:find [?e ?updated ?name ?region]
                              :in [$ ?search ?type]
                           :where [[?e :type ?type]
                                   [(fulltext $ :name ?search) [[?e ?name]]]
                                   [?e :updated ?updated]
                                   [?e :address/region ?region]]}
                   :dbs []}
            search (:q params)]
         (println "gonna" search)
         (-> q-map
           (update-in [:dbs] concat [search ent-type])
           (update-in [:query :where] concat [['?e sub-type sub-type-val ]])
           (handler)))))
)

(defn gen-response [handler]
  (fn [params]
    (let [response (handler params)
          results (group-by #(nth % 3) (:results response)) ]
      (println "Response:" response)
      (println "Results:" results)
        {:status 200
         :headers {"Content-Type" "application/json"}
         :body (json/encode 
            (for  [[k v] results] {:text k :children  (into  []  (map  #(assoc {} :text  (nth % 2) :id  (nth % 0)) v))})) 
        }
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

(def search-people
  (-> (core/query)
      (build-query :type/person)
      (gen-response)
      (strip-outer-vec)
      (core/remove-empty-keys)))

(def search-acct-mgr
  (-> (core/query)
      (build-query :type/person :person/type :person.type/team-member)
      (gen-response)
      (strip-outer-vec)
      (core/remove-empty-keys)))
