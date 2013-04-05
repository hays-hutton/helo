(ns helo.data.init
  (:use [datomic.api :only [db q] :as d]))

(def uri "datomic:free://localhost:4334/team1")

(defn delete-db!
  "Don't do this."
   [] (d/delete-database uri))

(defn create-db!
  "Create db and init schema if doesn't exist"
  [] (when (d/create-database uri)
      (let [conn (d/connect uri)]
        @(d/transact conn (read-string (slurp "src/schema/team1.clj")))
        @(d/transact conn (read-string (slurp "src/schema/team1-funs.clj")))
        @(d/transact conn (read-string (slurp "src/schema/team1-seed.clj"))))))
