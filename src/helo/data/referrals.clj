(ns helo.data.referrals
  (:use [datomic.api :only [db q] :as d])
  (:require [helo.data.core :as core]
            [helo.data.address :as addr]
            [helo.data.cchannels :as cchan]
            [helo.data.persons :as per]
            [helo.data.notes :as notes]
            [helo.utils.uri :as utils]
            [cheshire.core :as json]
            [clojure.tools.logging :only [info error]]))

(def ez-to-attr
  {:name :name
   :erTag :referral/er-tag
   :eeTag :referral/ee-tag
   :erQuiet :referral/er-quiet?
   :eeQuiet :referral/ee-quiet?
   :note :note
   :who :who})

(def attr-to-ez
  (zipmap (vals ez-to-attr) (keys ez-to-attr)))

(def valid-keys
  [:erTag
   :eeTag
   :erQuiet
   :eeQuiet
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
   :referral.status/other "other"
   :referral.status/cancelled "cancelled" })

(def label-to-rfr-status
  (zipmap (vals rfr-status-to-label) (keys rfr-status-to-label)))

(defn ent-to-rfr-map [entity]
  {:id (:db/id entity)
   :name (:name entity)
   :er  (:db/id (first (:person/_cchannels (:referral/er-cchannel entity))))  
   :erName (:name (first (:person/_cchannels (:referral/er-cchannel entity))) )
   :erCChan (:db/id (:referral/er-cchannel entity)) 
   :erCChanName (:name (:referral/er-cchannel entity)) 
   :erQuiet (:referral/er-quiet? entity)
   :ee  (:db/id (first (:person/_cchannels (:referral/ee-cchannel entity))))   
   :eeName (:name (first (:person/_cchannels (:referral/ee-cchannel entity))) )
   :eeCChan (:db/id (:referral/ee-cchannel entity)) 
   :eeCChanName (:name (:referral/ee-cchannel entity)) 
   :eeQuiet (:referral/ee-quiet? entity)
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
          rfr* (dissoc rfr :who :referral/er-tag :referral/ee-tag)
          status (get label-to-rfr-status (:referral/status rfr) :referral.status/new)
          ee-cchan-q (get rfr :referral/ee-quiet? false)
          er-cchan-q (get rfr :referral/er-quiet? false)
          tstamp (java.util.Date.)
          tail (rest tran)]
      (handler (vec (flatten (conj [(merge rfr* {:name (str (utils/searchable (:referral/er-tag rfr)) " refers " (utils/searchable (:referral/ee-tag rfr))) 
                                                 :referral/status status
                                                 :created-by who 
                                                 :updated-by who 
                                                 :created tstamp
                                                 :updated tstamp
                                                 :referral/ee-quiet? ee-cchan-q  
                                                 :referral/er-quiet? er-cchan-q 
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



(defn strip-outer-vec [handler]
  (fn [v]
    (let [m (first v)]
      (handler m))))

;(def read-referrals
;  (-> (core/query)
;      (build-query)
;      (core/limit-query)
;      (gen-response)
;      (strip-outer-vec)
;      (core/remove-empty-keys)))


(defn lookup [uri]
  (ffirst (d/q '[:find ?e :in $ ?uri :where [?e :cchannel/cchannel ?uri]]  (db core/conn) uri)))

(defn add-cchan [handler k target cchan-type]
  (fn [tran]
    (let [rfr (first tran)]
      (if (contains? rfr k)
        (if-let [cchan (utils/to-uri (k rfr))]
          (if-let [cchan-id (lookup cchan)]
            (if-let [tail (rest tran)]
              (handler (vec (flatten [(assoc rfr target cchan-id) tail])))
              (handler [(assoc rfr target cchan-id)]))
            (let [cchan-id (d/tempid :db.part/user)
                  who (:who rfr)]
              (if-let [tail (rest tran)]
                (handler (vec (flatten [(assoc rfr target cchan-id) tail (cchan/phone-map cchan-id cchan cchan-type who)])))
                (handler [(assoc rfr  target cchan-id) (cchan/phone-map cchan-id cchan cchan-type who)]))))
          {:status 400 :body (json/encode {:message (str "Bad phone number for " k " " (k rfr))})})
        (handler tran)))))

;(def create-referral
;  (-> (core/post)
;      (add-defaults)
;      (add-cchan :referral/er-tag :referral/er-cchannel :cchannel.type/unknown )
;      (add-cchan :referral/ee-tag :referral/ee-cchannel :cchannel.type/unknown)
;      (add-note)
;      (core/add-key)
;      (core/translate-keys ez-to-attr)
;      (core/min-required-keys required-record-keys)
;      (core/valid-keys valid-keys)
;      (core/remove-empty-keys)))

(defn ncode  [handler]
  (fn  [request]
    (let  [response  (handler request)]
      {:status 200
       :body  (json/encode response)
      })))

(defn referral-query []
  (fn [id]
    (let [dbval (db core/conn)
          rid (Long/parseLong id)
          rfr (d/entity dbval rid)]
     (if (= :type/referral (:type rfr))
       rfr))))

(defn- action-set-owner [entity]
  {:name "status" 
   :title "Set Owner"
   :method "POST"
   :href (core/ent->href entity)
   :fields [{:name :status :type "hidden" :value :owned }
            {:name :owner :type "hidden" :value (:who entity)}]})

(defn- action-set-scheduled [entity]
  {:name "set scheduled" 
   :title "Set Scheduled"
   :method "POST"
   :href (core/ent->href entity)
   :fields [{:name :status :type "hidden" :value :scheduled }]})

(defn- action-set-in [entity]
  {:name "set in" 
   :title "Set In"
   :method "POST"
   :href (core/ent->href entity)
   :fields [{:name :status :type "hidden" :value :in }]})

(defn- action-set-completed [entity]
  {:name "set completed " 
   :title "Set Completed"
   :method "POST"
   :href (core/ent->href entity)
   :fields [{:name :status :type "hidden" :value :completed }]})

(defn- action-set-cancelled [entity]
  {:name "set cancelled" 
   :title "Set Cancelled"
   :method "POST"
   :href (core/ent->href entity)
   :fields [{:name :status :type "hidden" :value :cancelled }]})

(defn- action-set-other [entity]
  {:name "set other" 
   :title "Set Other"
   :method "POST"
   :href (core/ent->href entity)
   :fields [{:name :status :type "hidden" :value :other }]})

(defn- action-set-ee-cchan [entity]
  {:name "set eeCChan" 
   :title "Set EE CChannel"
   :method "POST"
   :href (core/ent->href entity)
   :fields [{:name :eeCChan :type "text" :value "" }]})

(defn- action-set-er-cchan [entity]
  {:name "set erCChan" 
   :title "Set ER CChannel"
   :method "POST"
   :href (core/ent->href entity)
   :fields [{:name :erCChan :type "text" :value "" }]})

(defn- action-set-ee-quiet [entity]
  {:name "set EE quiet " 
   :title "Set EE Quiet"
   :method "POST"
   :href (core/ent->href entity)
   :fields [{:name :eeQuiet :type "hidden" :value "true" }]})

(defn- action-set-ee-loud [entity]
  {:name "set EE loud" 
   :title "Set EE Loud"
   :method "POST"
   :href (core/ent->href entity)
   :fields [{:name :eeQuiet :type "hidden" :value "false" }]})

(defn- action-set-er-quiet [entity]
  {:name "set ER quiet " 
   :title "Set ER Quiet"
   :method "POST"
   :href (core/ent->href entity)
   :fields [{:name :erQuiet :type "hidden" :value "true" }]})

(defn- action-set-er-loud [entity]
  {:name "set ER loud " 
   :title "Set ER Loud"
   :method "POST"
   :href (core/ent->href entity)
   :fields [{:name :erQuiet :type "hidden" :value "false" }]})

(defn- action-cancel [entity]
  {:name "cancel" 
   :title "Cancel Referral"
   :method "POST"
   :href (core/ent->href entity)
   :fields [{:name :status :type "hidden" :value :cancel}]})

(defn toggle-ee-loud [entity]
  (if (:referral/ee-quiet? entity)
    (action-set-ee-loud entity)
    (action-set-ee-quiet entity)
    ))

(defn toggle-er-loud [entity]
  (if (:referral/er-quiet? entity)
    (action-set-er-loud entity)
    (action-set-er-quiet entity)
    ))

(defn ref-actions [entity]
  (case (:referral/status entity)
    :referral.status/new [(action-set-owner entity) (toggle-er-loud entity) (toggle-ee-loud entity) (action-set-cancelled entity)]
    :referral.status/owned [(action-set-scheduled entity) (toggle-er-loud entity) (toggle-ee-loud entity) (action-set-cancelled entity)]
    :referral.status/scheduled [(action-set-in entity) (toggle-er-loud entity) (toggle-ee-loud entity)(action-set-cancelled entity)]
    :referral.status/in [(action-set-completed entity) (toggle-er-loud entity) (toggle-ee-loud entity)]
    :referral.status/completed [(toggle-er-loud entity) (toggle-ee-loud entity)]
    :referral.status/other [(toggle-er-loud entity) (toggle-ee-loud entity)(action-set-cancelled entity)]
    :referral.status/cancelled [(action-set-other entity)]))

(defn gen-response [handler]
  (fn [params]
    (let [entity (handler params)
          er (first (:person/_cchannels (:referral/er-cchannel entity)))
          er-cchan (:referral/er-cchannel entity)
          ee (first (:person/_cchannels (:referral/ee-cchannel entity)))
          ee-cchan (:referral/ee-cchannel entity)
          owner (:referral/owner entity) ]
      {:status 200
       :headers {"Content-Type" "application/json"}
       :body (json/encode {:class [ "referral" ]
                           :properties (assoc (core/base-prop entity :referral/status rfr-status-to-label) :erQuiet (:referral/er-quiet? entity) :eeQuiet (:referral/ee-quiet? entity))  
                           :actions [ (ref-actions entity)]
                           :entities [ {:class ["er"]
                                        :properties (core/base-prop er :person/type per/person-type-to-label)
                                        :href (core/ent->href er)}
                                       {:class ["ee"]
                                        :properties (core/base-prop ee :person/type per/person-type-to-label)
                                        :href (core/ent->href ee)}
                                       {:class ["erCChan"]
                                        :properties (core/base-prop er-cchan :cchannel/type cchan/cchannel-type-to-label)
                                        :href (core/ent->href er-cchan)}
                                       {:class ["eeCChan"]
                                        :properties (core/base-prop ee-cchan :cchannel/type cchan/cchannel-type-to-label)
                                        :href (core/ent->href ee-cchan)}
                                       {:class ["owner"]
                                        :properties (core/base-prop owner :person/type per/person-type-to-label)
                                        :href (core/ent->href owner)}]
                           :href (core/ent->href entity)})})))
(def read-referral
  (-> (referral-query)
      (gen-response)))


(defn rename [handler km]
  (fn [tran]
    (let [m (first tran)
          tail (rest tran)
          m* (clojure.set/rename-keys m km)]
      (if tail 
        (handler [m* tail]) 
        (handler [m*])))))

(defn transform [k v km]
  (println "transform k:" k " v:" v " km:" km)
  (if-let [target (get km k)]
    (case target
      "long" (Long/parseLong v)
      "int" (Integer/parseInt v)
      "status-label" (label-to-rfr-status v)
      "bool" (Boolean/parseBoolean v)
      v)
    v))


(def coerce-map 
  {:db/id "long"
   ;:who "long"
   :referral/owner "long"
   :referral/ee-quiet? "bool"
   :referral/er-quiet? "bool"
   :referral/status "status-label"
   :id "long"
  })

(defn coerce [handler km]
  (fn [tran]
    (let [m (first tran)
          tail (rest tran)
          m* (into {} (for [[k v] m] [k (transform k v km)])) ]
      (println "coerce m:" m*)
      (if tail 
        (handler [m* tail]) 
        (handler [m*])))))

(defn update [handler]
  (fn [tran]
    (let [m (first tran)
          owner (:who m)
          m* (dissoc m :who) ] 
      (handler [ (merge m* {:updated (java.util.Date.) :updated-by (:who m)}) ]))))
