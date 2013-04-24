(ns helo.data.core
  (:use [clj-time.coerce :as tc]) 
  (:use [datomic.api :only [db q] :as d])
  (:require [helo.utils.uri :as utils]
            [cheshire.core :as json]))

(def url "https://api.team1.com/")
(def uri "datomic:free://localhost:4334/team1")
(def conn (d/connect uri))

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
    (println "limitq:" params)
    (if-let [limit (Integer/parseInt (:limit params)) ]
      (let [offset (Integer/parseInt (:offset params)) 
            response (handler params)
            results (:results response)
            cnt (count results)]
        (assoc response :results (take limit (drop offset (sort-by-second results))) :count cnt :limit limit :offset offset))
     {:status 400
      :body (json/encode {:message "Bad limit or offset issue" :message-type "alert"})}) ))

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
      @(d/transact conn tran)
      (println "The record: " tran)
      {:status 200 :body (json/encode {:message "tran"})}
      (catch Exception e {:status 500 :body (json/encode {:message (str e)})}))))
