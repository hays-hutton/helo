(ns helo.data.orgs
  (:use [datomic.api :only [db q] :as d])
  (:require [helo.data.core :as core]
            [helo.data.address :as addr]
            [helo.data.cchannels :as cchan]
            [helo.data.notes :as notes]
            [helo.utils.uri :as utils]
            [cheshire.core :as json]
            [clj-time.core :only [from-now minutes weeks] ]
            [clj-time.coerce :only [to-date] ]
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

(def attr-to-ez
  (zipmap (vals ez-to-attr) (keys ez-to-attr)))

(def valid-keys
 (concat [:name
          :phone
          :fax
          :email
          :orgType
          :parent
          :acctMgr
          :note
          :who ] addr/address-record-keys))

(def required-record-keys
  [:name 
   :who])


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

(defn add-defaults [handler]
  (fn [tran]
    (let [org (first tran)
          who (:who org)
          org* (dissoc org :who)
          tstamp (java.util.Date.)
          tail (rest tran)]
      (handler (vec (flatten (conj [(merge org* {:created-by who :updated-by who :created tstamp :updated tstamp :type :type/org})] tail)))))))

(defn add-note [handler]
  (fn [tran]
    (let [org (first tran)]
      (if (contains? org :note)
        (if-not (clojure.string/blank? (:note org))
          (let [note-id (d/tempid :db.part/user)
                note (:note org)
                who (:who org)
                parent-id (:db/id org)
                org* (dissoc org :note)]
            (if-let [tail (rest tran)]
              (handler (vec (flatten [org* tail (notes/note-map parent-id note who)])))
              (handler [org* (notes/note-map parent-id note who)])))
          {:status 400 :body (json/encode {:message (str "Bad note for :note " (:note org))})})
        (handler tran)))))

(defn add-email [handler]
  (fn [tran]
    (let [org (first tran)]
      (if (contains? org :email)
        (if-let [email (utils/to-email (:email org))]
          (let [email-id (d/tempid :db.part/user)
                who (:who org)
                chans (:org/cchannels org)
                org* (dissoc org :email)]
            (if-let [tail (rest tran)]
              (handler (vec (flatten [(assoc org* :org/cchannels (conj chans email-id)) tail (cchan/phone-map email-id email :cchannel.type/email who)])))
              (handler [(assoc org*  :org/cchannels (conj chans email-id)) (cchan/phone-map email-id email :cchannel.type/email who)])))
          {:status 400 :body (json/encode {:message (str "Bad email for :org/email " (:email org))})})
        (handler tran)))))


(defn add-phone [handler k phone-type]
  (fn [tran]
    (let [org (first tran)]
      (if (contains? org k)
        (if-let [phone (utils/to-tel (k org))]
          (let [phone-id (d/tempid :db.part/user)
                who (:who org)
                chans (:org/cchannels org)
                org* (dissoc org k)]
            (if-let [tail (rest tran)]
              (handler (vec (flatten [(assoc org* :org/cchannels (conj chans phone-id)) tail (cchan/phone-map phone-id phone phone-type who)])))
              (handler [(assoc org*  :org/cchannels (conj chans phone-id)) (cchan/phone-map phone-id phone phone-type who)])))
          {:status 400 :body (json/encode {:message (str "Bad phone number for " k " " (k org))})})
        (handler tran)))))

(defn build-defaults [handler]
  (fn [tran]
    (let [org (first tran)
          who (:who org)
          org* (dissoc org :who)
          tstamp (java.util.Date.)
          tail (rest tran)]
      (handler (vec (flatten (conj [(merge org* {:created-by who :updated-by who :created tstamp :updated tstamp :type :type/org})] tail)))))))



(def filter-clauses
  {"insuror" '[[?e :org/type :org.type/insuror]] 
   "agency" '[[?e :org/type :org.type/agency]] 
   "vendor" '[[?e :org/type :org.type/vendor]] 
   "partner" '[[?e :org/type :org.type/partner]] 
   "client" '[[?e :org/type :org.type/client]]})

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
                         :where [[?e :type :type/org]
                                 [?e :updated ?updated]]}
                 :dbs []}
          filt (:filter params)
          search (:search params)]
       (println "gonna" filt ":" search)
       (if (or filt search) 
         (handler (-> q-map 
                      (update-in [:query :where] concat (filter-clauses filt))
                      (update-in [:query :where] concat (search-clause search))
                      (update-in [:query :in] concat (in-clause search))
                      (update-in [:dbs] concat (search-term search)))) 
         (handler q-map)))))

(defn gen-response [handler]
  (fn [params]
    (let [response (handler params)
          ents (map #(d/entity (:dbval response) (first %)) (:results response))
          orgs (into [] (map #(ent-to-org-map %) ents))]
      {:status 200
       :headers {"Content-Type" "application/json"}
       :body (json/encode {:limit (:limit response)
                           :offset (:offset response)
                           :count (:count response)
                           :type :orgs
                           :results orgs})})))

(defn strip-outer-vec [handler]
  (fn [v]
    (let [m (first v)]
      (handler m))))

(def read-orgs
  (-> (core/query)
      (build-query)
      (core/limit-query)
      (gen-response)
      (strip-outer-vec)
      (core/remove-empty-keys)))

(def org-query-keys [:db/id 
                     :name
                     :updated
                     :created 
                     :address/street
                     :address/city
                     :address/region
                     :address/postal-code
                     :address/latitude
                     :address/longitude
                     ]
)

(defn build-cchannel [cchannel]
  {:name (:name cchannel)
   :cchannel (:cchannel/cchannel cchannel)
   :id (:db/id cchannel)
  }
)

(defn build-note [note]
  {:name (:name note)
   :by-name (:name (:note/by note)) 
   :by-id (:db/id (:note/by note)) 
   :note (:note/note note)
   :created (:created note)
  }
)

(defn org-cchannels [entity]
  (if-let [cchannels (:org/cchannels entity)]
    (map #(build-cchannel %) cchannels)))


(defn org-notes [entity]
  (if-let [notes (:note/_parent entity)]
    (map #(build-note %) notes)))

(defn org-jobs [entity]
  (if-let [jobs (:jobs/_client entity)]
    "Whoop"))

(defn org-referrals [entity]
  (if-let [jobs (:referral/_er entity)]
    "ER"))

(defn org-query []
  (fn [id]
    (println "hello" id)
    (let [dbval (db core/conn)
          oid (Long/parseLong id)
          org (d/entity dbval oid) 
          o-map (ent-to-org-map org)]
     (-> o-map
       (assoc :cchannels (org-cchannels org))
       (assoc :referrals (org-referrals org))
       (assoc :jobs (org-jobs org)))
      )))

(def create-org
  (-> (core/post)
      (add-defaults)
      (add-note)
      (add-email)
      (add-phone :fax :cchannel.type/fax)
      (add-phone :phone :cchannel.type/work)
      (core/add-key)
      (core/translate-keys ez-to-attr)
      (core/min-required-keys required-record-keys)
      (core/valid-keys valid-keys)
      (core/remove-empty-keys)
))

(defn ncode  [handler]
  (fn  [request]
    (let  [response  (handler request)]
      {:status 200
       :body  (json/encode response)
      })))

(def read-org
  (-> (org-query)
    (ncode)))
