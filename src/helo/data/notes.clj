(ns helo.data.notes
  (:require [datomic.api :only [db q] :as d]
            [helo.data.core :only [conn] :as core]
            [cheshire.core :as json]
            [clojure.tools.logging :only [info error]]))

(def ez-to-attr
  {:note :note/note
   :name :name
   :by   :note/by
   :parent :note/parent
   :visibility :note/visibility
   :who :who
  })

(def attr-to-ez 
  (zipmap (vals ez-to-attr) (keys ez-to-attr)))

(def valid-keys
  [:note
   :by
   :parent
   :visibility
   :who])

(def required-record-keys 
  [:note
   :parent 
   :who])

(defn ent-to-note-map [entity]
  {:note (:note/note entity)
   :byId (:db/id (:note/by entity)) 
   :byName (:name (:note/by entity))
   :updated (:updated entity)
   :name (:name entity)
   :id (:db/id entity)
   :type (:type entity)})

(defn add-defaults [handler]
  (fn [tran]
    (let [note-m (first tran)
          note (:note/note note-m)
          parent (Long/parseLong (:note/parent note-m))
          length (count note)
          nme (if (< length 33) note (subs note 0 33))
          who (:who note-m)
          note-m* (dissoc note-m :who)
          tstamp (java.util.Date.)]
      (handler [(merge note-m* {:name nme :note/by who :note/parent parent :created-by who :updated-by who :created tstamp :updated tstamp :type :type/note})]))))

(defn build-query [handler]
  (fn [params]
    (if-let [parent (Long/parseLong (:parent params))]
      (handler {:query  '{:find [?e, ?updated]
                          :in [$ ?parent]
                          :where [[?e :type :type/note]
                                  [?e :note/parent ?parent]
                                  [?e :updated ?updated]]}
                :dbs [parent] }) 
      [])))

(defn strip-outer-vec [handler]
  (fn [v]
    (let [m (first v)]
      (handler m))))

(defn gen-response [handler]
  (fn [params]
    (let [response (handler params)
          ents (map #(d/entity (:dbval response) (first %)) (:results response))
          notes (into [] (map #(ent-to-note-map %) ents)) ]
      {:status 200
       :headers {"Content-Type" "application/json"}
       :body (json/encode {:limit (:limit response)
                           :offset (:offset response)
                           :count (:count response)
                           :type :note
                           :results notes})})))

(def read-notes
  (-> (core/query)
      (build-query)
      (core/limit-query)
      (gen-response)
      (strip-outer-vec)
      (core/remove-empty-keys)))

(def create-note
  (-> (core/post)
      (add-defaults)
      (core/add-key)
      (core/translate-keys ez-to-attr)
      (core/min-required-keys required-record-keys)
      (core/valid-keys valid-keys)
      (core/remove-empty-keys)))

(defn note-map  [parent-id note who]
  (let  [tstamp  (java.util.Date.)
        length  (count note)]
    {:db/id  (d/tempid :db.part/user)
     :name  (if  (< length 33) note  (subs note 0 33))
     :note/note note
     :note/visibility :visibility/team-member
     :note/by who
     :note/parent parent-id
     :updated-by who
     :created-by who
     :updated tstamp
     :created tstamp
     :type :type/note}))
