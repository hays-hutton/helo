(ns helo.data.persons
  (:use [datomic.api :only [db q] :as d])
  (:require [helo.data.core :as core]
            [helo.data.address :as addr]
            [helo.data.cchannels :as cchan]
            [helo.data.notes :as notes]
            [helo.utils.uri :as utils]
            [cheshire.core :as json]
            [clojure.tools.logging :only [info error]]))

(def ez-to-attr
  {:lastName :person/last-name
   :firstName :person/first-name
   :cell :cell
   :work :work
   :home :home
   :email :email
   :personType :person/type
   :parent :person/org
   :note :note/note
   :address/address :address/address
   :address/street :address/street
   :address/city :address/city
   :address/region :address/region
   :address/postal-code :address/postal-code
   :address/latitude :address/latitude
   :address/longitude :address/longitude
   :address/provider :address/provider
   :who :who})

(def valid-keys
 (concat [:firstName
          :lastName
          :home
          :work
          :cell
          :email
          :personType
          :parent
          :note
          :who] addr/address-record-keys))

(def required-record-keys
  [:lastName
   :firstName 
   :who])

(def label-to-person-type 
  {"team" :person.type/team-member
   "csr" :person.type/csr
   "agent" :person.type/agent
   "producer" :person.type/producer
   "adjuster" :person.type/adjuster
   "claim" :person.type/claim
   "client" :person.type/client
   "vendor" :person.type/vendor})

(def person-type-to-label
  (zipmap (vals label-to-person-type) (keys label-to-person-type)))

(defn add-defaults [handler]
  (fn [tran]
    (let [per (first tran)
          nme (str (:person/last-name per) ", " (:person/first-name per))
          who (:who per)
          ptype (get label-to-person-type (:person/type per) :person.type/client)
          per* (dissoc per :who)
          tstamp (java.util.Date.)
          tail (rest tran)]
      (handler (vec (flatten (conj [(merge per* {:person/type ptype :name nme :created-by who :updated-by who :created tstamp :updated tstamp :type :type/person})] tail)))))))

(defn add-note [handler]
  (fn [tran]
    (let [per (first tran)]
      (if (contains? per :note/note)
        (if-not (clojure.string/blank? (:note/note per))
          (let [note-id (d/tempid :db.part/user)
                note (:note/note per)
                who (:who per)
                parent-id (:db/id per)
                per* (dissoc per :note/note)]
            (if-let [tail (rest tran)]
              (handler (vec (flatten [per* tail (notes/note-map parent-id note who)])))
              (handler [ per*(notes/note-map parent-id note who)])))
          {:status 400 :body (json/encode {:message (str "Bad note for :note/note " (:note/note per))})})
        (handler tran)))))

(defn add-email [handler]
  (fn [tran]
    (let [per (first tran)]
      (if (contains? per :email)
        (if-let [email (utils/to-email (:email per))]
          (let [email-id (d/tempid :db.part/user)
                who (:who per)
                chans (:person/cchannels per)
                per* (dissoc per :email)]
            (if-let [tail (rest tran)]
              (handler (vec (flatten [(assoc per* :person/cchannels (conj chans email-id)) tail (cchan/phone-map email-id email :cchannel.type/email who)])))
              (handler [(assoc per*  :person/cchannels (conj chans email-id)) (cchan/phone-map email-id email :cchannel.type/email who)])))
          {:status 400 :body (json/encode {:message (str "Bad email for :person/email " (:person/email per))})})
        (handler tran)))))

(defn add-sms [handler]
  (fn [tran]
    (let [per (first tran)]
      (if (contains? per :person/sms)
        (if-let [sms (utils/to-sms (:person/sms per))]
          (let [sms-id (d/tempid :db.part/user)
                who (:who per)
                chans (:person/cchannels per)
                per* (dissoc per :person/sms)]
            (if-let [tail (rest tran)]
              (handler (vec (flatten [(assoc per* :person/cchannels (conj chans sms-id)) tail (cchan/phone-map sms-id sms :cchannel.type/sms who)])))
              (handler [(assoc per*  :person/cchannels (conj chans sms-id)) (cchan/phone-map sms-id sms :cchannel.type/sms who)])))
          {:status 400 :body (json/encode {:message (str "Bad sms number for :person/sms " (:person/sms per))})})
        (handler tran)))))

(defn add-phone [handler k phone-type]
  (fn [tran]
    (let [per (first tran)]
      (if (contains? per k)
        (if-let [phone (utils/to-tel (k per))]
          (let [phone-id (d/tempid :db.part/user)
                who (:who per)
                chans (:person/cchannels per)
                per* (dissoc per k)]
            (if-let [tail (rest tran)]
              (handler (vec (flatten [(assoc per* :person/cchannels (conj chans phone-id)) tail (cchan/phone-map phone-id phone phone-type who)])))
              (handler [(assoc per*  :person/cchannels (conj chans phone-id)) (cchan/phone-map phone-id phone phone-type who)])))
          {:status 400 :body (json/encode {:message (str "Bad phone number for " k " " (k per))})})
        (handler tran)))))

(def create-person
  (-> (core/post)
      (add-defaults)
      (add-note)
      (add-sms)
      (add-email)
      (add-phone :work :cchannel.type/work)
      (add-phone :home :cchannel.type/home)
      (add-phone :cell :cchannel.type/cell)
      (core/add-key)
      (core/translate-keys ez-to-attr)
      (core/min-required-keys required-record-keys)
      (core/valid-keys valid-keys)
      (core/remove-empty-keys)))

(defn ncode [handler]
  (fn [request]
    (let [response (handler request)]
      {:status 200
       :body (json/encode response)})))

(defn ent-to-per-map [entity]
  {:id (:db/id entity)
   :name (:name entity)
   :parent (:db/id (:person/org entity))
   :parentName (:name (:person/org entity))
   :street (:address/street entity)
   :city (:address/city entity)
   :state (:address/region entity)
   :zip (:address/postal-code entity)
   :lat (:address/latitude entity)
   :lon (:address/longitude entity)
   :personType (person-type-to-label (:person/type entity)) 
   :updated (:updated entity)
   :created (:created entity)
   :type (:type entity) })

(defn gen-response [handler]
  (fn [params]
    (let [response (handler params)
          ents (map #(d/entity (:dbval response) (first %)) (:results response))
          pers (into [] (map #(ent-to-per-map %) ents))]
      {:status 200
       :headers {"Content-Type" "application/json"}
       :body (json/encode {:limit (:limit response)
                           :offset (:offset response)
                           :count (:count response)
                           :type :persons
                           :results pers})})))

(def filter-clauses
  {"team" '[[?e :person/type :person.type/team-member]] 
   "csr" '[[?e :person/type :person.type/csr]] 
   "agent" '[[?e :person/type :person.type/agent]] 
   "producer" '[[?e :person/type :person.type/producer]] 
   "adjuster" '[[?e :person/type :person.type/adjuster]] 
   "claims" '[[?e :person/type :person.type/claims]] 
   "client" '[[?e :person/type :person.type/client]] 
   "vendor" '[[?e :person/type :person.type/vendor]]})

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
                         :where [[?e :type :type/person]
                                 [?e :updated ?updated]]}
                 :dbs []}
          filt (:filter params)
          search (:search params)]
       (if (or filt search) 
         (handler (-> q-map 
                      (update-in [:query :where] concat (filter-clauses filt))
                      (update-in [:query :where] concat (search-clause search))
                      (update-in [:query :in] concat (in-clause search))
                      (update-in [:dbs] concat (search-term search)))) 
         (handler q-map)))))

(defn strip-outer-vec [handler]
  (fn [v]
    (let [m (first v)]
      (handler m))))

(def read-persons
  (-> (core/query)
      (build-query)
      (core/limit-query)
      (gen-response)
      (strip-outer-vec)
      (core/remove-empty-keys)))

(defn build-cchannel [cchannel]
  {:name (:name cchannel)
   :cchannel (:cchannel/cchannel cchannel)
   :id (:db/id cchannel)
  })

(defn person-cchannels [entity]
  (if-let [cchannels (:person/cchannels entity)]
    (map #(build-cchannel %) cchannels)))

(defn person-jobs [entity]
  (if-let [jobs (:jobs/_client entity)]
    "Whoop"))

(defn person-referrals [entity]
  (if-let [jobs (:referral/_er entity)]
    "ER"))

(defn person-query []
  (fn [id]
    (let [dbval (db core/conn)
          nid (Long/parseLong id)
          person (d/entity dbval nid) 
          p-map (ent-to-per-map person) ]
     (-> p-map
       (assoc :cchannels (person-cchannels person))
       (assoc :referrals (person-referrals person))
       (assoc :jobs (person-jobs person))))))

(def read-person
  (-> (person-query)
      (ncode)))
