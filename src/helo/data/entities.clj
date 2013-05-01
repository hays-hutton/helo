(ns helo.data.entities
  (:use [datomic.api :only [db q] :as d])
  (:require [helo.data.core :as core]
            [helo.utils.uri :as utils]
            [helo.data.orgs :as orgs]
            [helo.data.persons :as pers]
            [helo.data.cchannels :as cchans]
            [cheshire.core :as json]
            [clojure.tools.logging :only [info error]]))


(def filter-clauses
  {"insuror" '[[?e :org/type :org.type/insuror]] 
   "agency" '[[?e :org/type :org.type/agency]] 
   "vendorOrg" '[[?e :org/type :org.type/vendor]] 
   "partner" '[[?e :org/type :org.type/partner]] 
   "clientOrg" '[[?e :org/type :org.type/client]] 
   "clientPer" '[[?e :person/type :person.type/client]] 
   "team" '[[?e :person/type :person.type/team-member]] 
   "csr" '[[?e :person/type :person.type/csr]] 
   "agent" '[[?e :person/type :person.type/agent]] 
   "producer" '[[?e :person/type :person.type/producer]] 
   "adjuster" '[[?e :person/type :person.type/adjuster]] 
   "claim" '[[?e :person/type :person.type/claim]] 
   "vendorPer" '[[?e :person/type :person.type/vendor]] 
   "org" '[[?e :type :type/org]]
   "person" '[[?e :type :type/person]]
   "cchan" '[[?e :type :type/cchannel]]
   "refer" '[[?e :type :type/referral]] })

; should use rule for "client" but then need to do logic to add to :in
;'[[(client? ?e) [?e :org/type :org.type/client]] [(client? ?e) [?e :person/type :person.type/client]]] 

(defn search-clause [search]
  (if search
    '[[(fulltext $ :name ?search) [[?e ?name]]]]  ))

(defn in-clause [search]
  (if search
    '[?search]))

(defn search-term [search]
  (if search
    [search]))

(defn build-query [handler]
  (fn [params]
    (let [q-map {:query '{:find [?e, ?updated]
                            :in [$]
                         :where [[?e :updated ?updated]]}
                 :dbs []}
          filt (:type params)
          search (:q params)]
       (if (or filt search) 
         ;(if-not (contains? filter-clauses filt)
         ;   {:query 0 :results [] }
            (handler (-> q-map 
                      (update-in [:query :where] concat (get filter-clauses filt))
                      (update-in [:query :where] concat (search-clause search))
                      (update-in [:query :in] concat (in-clause search))
                      (update-in [:dbs] concat (search-term search)))) 
         ;)
         (handler q-map)))))

(defn sub-type [entity]
  (cond
    (:org/type entity) ( (:org/type entity)  orgs/org-type-to-label)
    (:person/type entity) ( (:person/type entity)  pers/person-type-to-label)
    (:cchannel/type entity) (get cchans/cchannel-type-to-label (:cchannel/type entity) "None" )
    :else "None"))

(defn ent-to-ent-map [entity]
  {:class []
   :properties {:id (:db/id entity)
                :name (:name entity)
                :type (get core/type->label (:type entity) "None")
                :subType (sub-type entity)
                :region (get entity :address/region "None")}
   :href (core/ent->href entity)})

(defn gen-response [handler]
  (fn [params]
    (let [response (handler params)
          ents (map #(d/entity (:dbval response) (first %)) (:results response))
          entities (into [] (map #(ent-to-ent-map %) ents))]
      {:status 200
       :headers {"Content-Type" "application/json"}
       :body  ( json/encode {:class [:things :medium-collection]
                             :properties { :offset (:offset response)
                                           :limit (:limit response)
                                           :count (:count response)}
                             :entities entities})})))

(def get-entities
  (-> (core/query)
      (build-query)
      (core/limit-query)
      (gen-response)
      (core/strip-outer-vec)
      (core/remove-empty-keys)))
