(ns helo.data.referrals
  (:use [datomic.api :only [db q] :as d])
  (:require [helo.data.core :as core]
            [helo.data.address :as addr]
            [helo.data.cchannels :as cchan]
            [helo.data.notes :as notes]
            [helo.utils.uri :as utils]
            [cheshire.core :as json]
            [clojure.tools.logging :only [info error]]))

(def ez-to-attr
  {:name :name
   :erTag :referral/er-tag
   :eeTag :referral/ee-tag
   :note :note
   :who :who})

(def attr-to-ez
  (zipmap (vals ez-to-attr) (keys ez-to-attr)))

(def valid-keys
  [:erTag
   :eeTag
   :note
   :who])

(def required-record-keys
  [:erTag
   :eeTag
   :who])

(def rfr-status-to-label
  {:referral.status/new "new" 
   :referral.status/owned "owned"
   :referral.status/scheduled "scheduled" 
   :referral.status/in "in" 
   :referral.status/completed "completed" 
   :referral.status/other "other"})

(def label-to-rfr-status
  (zipmap (vals rfr-status-to-label) (keys rfr-status-to-label)))

(defn ent-to-rfr-map [entity]
  {:id (:db/id entity)
   :name (:name entity)
   :er  (:db/id (first (:person/_cchannels (:referral/er-cchannel entity))))  
   :erName (:name (first (:person/_cchannels (:referral/er-cchannel entity))) )
   :erCChan (:db/id (:referral/er-cchannel entity)) 
   :erCChanName (:name (:referral/er-cchannel entity)) 
   :ee  (:db/id (first (:person/_cchannels (:referral/ee-cchannel entity))))   
   :eeName (:name (first (:person/_cchannels (:referral/ee-cchannel entity))) )
   :eeCChan (:db/id (:referral/ee-cchannel entity)) 
   :eeCChanName (:name (:referral/ee-cchannel entity)) 
   :owner (:db/id (:referral/owner entity ))
   :ownerName (:name (:referral/owner entity))
   :status (rfr-status-to-label (:referral/status entity)) 
   :updated (:updated entity)
   :created (:created entity)
   :type (:type entity)})

(defn add-defaults [handler]
  (fn [tran]
    (let [rfr (first tran)
          who (:who rfr)
          rfr* (dissoc rfr :who)
          status (get label-to-rfr-status (:referral/status rfr) :referral.status/new)
          tstamp (java.util.Date.)
          tail (rest tran)]
      (handler (vec (flatten (conj [(merge rfr* {:name (str "rfr: "  ) 
                                                 :referral/status status
                                                 :created-by who 
                                                 :updated-by who 
                                                 :created tstamp
                                                 :updated tstamp
                                                 :type :type/referral})] tail)))))))

(defn add-note [handler]
  (fn [tran]
    (let [rfr (first tran)]
      (if (contains? rfr :note)
        (if-not (clojure.string/blank? (:note rfr))
          (let [note-id (d/tempid :db.part/user)
                note (:note rfr)
                who (:who rfr)
                parent-id (:db/id rfr)
                rfr* (dissoc rfr :note)]
            (if-let [tail (rest tran)]
              (handler (vec (flatten [rfr* tail (notes/note-map parent-id note who)])))
              (handler [rfr* (notes/note-map parent-id note who)])))
          {:status 400 :body (json/encode {:message (str "Bad note for :note " (:note rfr))})})
        (handler tran)))))


(def filter-clauses
  {"new" '[[?e :referral/status :referral.status/new ]] 
   "owned" '[[?e :referral/status :referral.status/owned]] 
   "scheduled" '[[?e :referral/status :referral.status/scheduled]] 
   "in" '[[?e :referral/status :referral.status/in]] 
   "completed" '[[?e :referral/status :referral.status/completed]] 
   "other" '[[?e :referral/status :referral.status/other]]})

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
                         :where [[?e :type :type/referral]
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

(defn gen-response [handler]
  (fn [params]
    (let [response (handler params)
          ents (map #(d/entity (:dbval response) (first %)) (:results response))
          referrals (into [] (map #(ent-to-rfr-map %) ents))]
      {:status 200
       :headers {"Content-Type" "application/json"}
       :body (json/encode {:class [:referrals]
                           :properties {:limit (:limit response)
                                        :offset (:offset response)
                                        :count (:count response)}
                           :entities referrals})})))

(defn strip-outer-vec [handler]
  (fn [v]
    (let [m (first v)]
      (handler m))))

(def read-referrals
  (-> (core/query)
      (build-query)
      (core/limit-query)
      (gen-response)
      (strip-outer-vec)
      (core/remove-empty-keys)))

(defn referral-query []
  (fn [id]
    (let [dbval (db core/conn)
          rid (Long/parseLong id)
          rfr (d/entity dbval rid) 
          r-map (ent-to-rfr-map rfr)]
     (println "r-map:" r-map)
     r-map
     )))

(defn lookup [uri]
  (ffirst (d/q '[:find ?e :in $ ?uri :where [?e :cchannel/cchannel ?uri]]  (db core/conn) uri)))

(defn add-cchan [handler k target cchan-type]
  (fn [tran]
    (let [rfr (first tran)]
      (if (contains? rfr k)
        (if-let [cchan (utils/to-uri (k rfr))]
          (if-let [cchan-id (lookup cchan)]
            (if-let [tail (rest tran)]
              (handler (vec (flatten [(assoc (dissoc rfr k) target cchan-id) tail])))
              (handler [(assoc (dissoc rfr k) target cchan-id)]))
            (let [cchan-id (d/tempid :db.part/user)
                who (:who rfr)
                rfr* (dissoc rfr k)]
              (if-let [tail (rest tran)]
                (handler (vec (flatten [(assoc rfr* target cchan-id) tail (cchan/phone-map cchan-id cchan cchan-type who)])))
                (handler [(assoc rfr*  target cchan-id) (cchan/phone-map cchan-id cchan cchan-type who)]))))
          {:status 400 :body (json/encode {:message (str "Bad phone number for " k " " (k rfr))})})
        (handler tran)))))


(def create-referral
  (-> (core/post)
      (add-defaults)
      (add-cchan :referral/er-tag :referral/er-cchannel :cchannel.type/unknown )
      (add-cchan :referral/ee-tag :referral/ee-cchannel :cchannel.type/unknown)
      (add-note)
      (core/add-key)
      (core/translate-keys ez-to-attr)
      (core/min-required-keys required-record-keys)
      (core/valid-keys valid-keys)
      (core/remove-empty-keys)))

(defn ncode  [handler]
  (fn  [request]
    (let  [response  (handler request)]
      {:status 200
       :body  (json/encode response)
      })))

(def read-referral
  (-> (referral-query)
      (ncode)))
