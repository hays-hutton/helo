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
    (let [valid (select-keys (first tran) v) ]
      (handler [valid]))))

(defn min-required-keys [handler v]
  (fn [tran]
    (let [missing (clojure.set/difference (set v) (set (keys (first tran))))] 
      (if (empty? missing)
        (handler tran)
        {:status 400 :body (json/encode {:message  (str "Missing required params:" missing)})}))))

(defn translate-keys [handler translate-map]
  (fn [tran]
    (let [m (first tran)
          m* (zipmap (map #(translate-map %) (keys m)) (vals m))]
      (handler [m*]))))

(defn add-key [handler]
  (fn [tran]
    (let [tran* (assoc (first tran) :db/id (d/tempid :db.part/user))]
      (handler [tran*]))))

(defn query []
  (fn [query]
    (try 
      (let [dbval (db conn)
            dbs (:dbs query)
            args (concat '(query dbval) dbs )
            return (ffirst (apply q args))]
        (println "The query returns: " return)
        return)
      (catch Exception e {:status 500 :body (json/encode {:message (str e)})}))))

(defn post []
  (fn [tran]
    (try 
      @(d/transact conn tran)
      (println "The record: " tran)
      {:status 200 :body (json/encode {:message "tran"})}
      (catch Exception e {:status 500 :body (json/encode {:message (str e)})}))))
