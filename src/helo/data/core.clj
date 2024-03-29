(ns helo.data.core
  (:use [clj-time.coerce :as tc]) 
  (:use [datomic.api :only [db q] :as d])
  (:require [helo.utils.uri :as utils]
            [cheshire.core :as json]))

(def url "https://api.team1.com/")
(def uri "datomic:free://localhost:4334/team1")
(def conn (d/connect uri))

(def type->label
  {:type/org "org"
   :type/person "person"
   :type/cchannel "cchannel"
   :type/referral "referral"
   :type/job "job"
   :type/note "note"
   :type/comm "comm"})

(def type->coll
  {:type/org "orgs"
   :type/person "persons"
   :type/cchannel "cchannels"
   :type/referral "referrals"
   :type/job "jobs"
   :type/note "notes"
   :type/comm "comms"})

(defn ent->href [entity]
  (if entity 
    (str "/" ((:type entity) type->coll) "/" (:db/id entity))
    ""))

(defn sort-by-updated [maps]
  (sort-by :updated #(compare (tc/to-long %2) (tc/to-long %1 )) maps))

(defn sort-by-second [vecs]
  (sort-by second #(compare (tc/to-long %2) (tc/to-long %1)) vecs ))

;TODO str'ing the v to handle :who which is a number. typecast error on blank? number
(defn remove-empty-keys [handler]
  (fn [record]
    (let [record* (apply dissoc record                                                                                                  
                    (for  [[k v] record :when  (clojure.string/blank? (str v))] k))]
      (handler [record*]))))

(defn valid-keys [handler v]
  (fn [tran]
    (println "valid-keys tran:" tran " valid:" v)
    (let [valid (select-keys (first tran) v) ]
      (handler [valid]))))

(defn min-required-keys [handler v]
  (fn [tran]
    (println "min-required-keys tran:" tran " required:" v)
    (let [missing (clojure.set/difference (set v) (set (keys (first tran))))] 
      (if (empty? missing)
        (handler tran)
        {:status 400 :body (json/encode {:message  (str "Missing required params:" missing)})}))))

(defn translate-keys [handler translate-map]
  (fn [tran]
    (println "translate-keys tran:" tran " tran-map:" translate-map)
    (let [m (first tran)
          m* (zipmap (map #(translate-map %) (keys m)) (vals m))]
      (println "post-translate: " m*)
      (handler [m*]))))

(defn add-key [handler]
  (fn [tran]
    (let [tran* (assoc (first tran) :db/id (d/tempid :db.part/user))]
      (handler [tran*]))))


(defn limit-query [handler]
  (fn [params]
    (let [limit (Integer/parseInt (get params :limit "30")) 
          offset (Integer/parseInt (get params :offset "0")) 
          response (handler params)
          results (:results response)
          cnt (count results)]
      (assoc response :results (take limit (drop offset (sort-by-second results))) :count cnt :limit limit :offset offset))))

(defn query []
  (fn [query]
    (try 
      (println "The query:" query)
      (let [dbval (db conn)
            dbs (:dbs query)
            m (:query query)
            args (concat  [m dbval] dbs )
            v (println "args" args)
            results (apply q args)]
        (println "Args is " args)
        (println "The query returns: " results)
        {:query args
         :dbval dbval
         :results results})
      (catch Exception e {:status 500 :body (json/encode {:message (str e)})}))))

(defn post []
  (fn [tran]
    (println "post:" tran)
    (try 
      (let [tempid (:db/id (first tran)) 
            response @(d/transact conn tran)
            entid (d/resolve-tempid (:db-after response) (:tempids response) tempid)]
        {:tran tran
         :created-id entid
         :response response}
      )
 ;     {:status 200 :body (json/encode {:message (str "Created: " (:type (first tran)) " ->" (:name (first tran)) )} )}
      (catch Exception e {:status 500 :body (json/encode {:message (str e)})}))))

(defn update-post []
  (fn [tran]
    (println "update-post:" tran)
    (try 
      (let [entid (:db/id (first tran)) 
            response @(d/transact conn tran) ]
        {:tran tran
         :updated-id entid
         :response response}
      )
 ;     {:status 200 :body (json/encode {:message (str "Created: " (:type (first tran)) " ->" (:name (first tran)) )} )}
      (catch Exception e {:status 500 :body (json/encode {:message (str e)})}))))


(defn strip-outer-vec [handler]
  (fn [v]
    (let [m (first v)]
      (handler m))))

(defn base-prop [entity k sub-type->label]
  (if entity
    {:id (:db/id entity)
     :name (:name entity)
     :created (:created entity)
     :createdBy (:db/id (:created-by entity) ) 
     :createdByName (:name (:created-by entity))
     :updated (:updated entity)
     :updatedBy (:db/id (:updated-by entity)) 
     :updatedByName (:name (:updated-by entity))
     :type ((:type entity) type->label)
     :subType ((k entity) sub-type->label)}
    {}))
