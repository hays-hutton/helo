(ns helo.data.core
  (:use [clj-time.coerce :as tc]) 
  (:use [datomic.api :only [db q] :as d])
  (:require [helo.utils.uri :as utils])
)

(def url "https://api.team1.com/")
(def uri "datomic:free://localhost:4334/team1")
(def conn (d/connect uri))

(defn sort-by-updated [maps]
  (sort-by :updated #(compare (tc/to-long %2) (tc/to-long %1 )) maps))

(defn sort-by-second [vecs]
  (sort-by second #(compare (tc/to-long %2) (tc/to-long %1)) vecs ))

