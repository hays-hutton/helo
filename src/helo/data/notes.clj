(ns helo.data.notes
  (:require [datomic.api :only [db q] :as d]
            [helo.data.core :only [conn] :as core]
            [clojure.tools.logging :only [info error]]))

;TODO does :who really belong here?
(def valid-keys
  [:note/note
   :note/by
   :note/parent
   :note/visibility
   :who])

(def required-record-keys 
  [:note/note
   :note/parent])

;TODO is this even used?
(defn note-map [parent-id note who]
  (let [tstamp (java.util.Date.)
        length (count note)]
    {:db/id (d/tempid :db.part/user)
     :name  (if (< length 33) note (subs note 0 33))
     :note/note note
     :note/visibility :visibility/team-member
     :note/by who
     :note/parent parent-id
     :updated-by who
     :created-by who
     :updated tstamp
     :created tstamp
     :type :type/note
    }))


(defn add-defaults [handler]
  (fn [tran]
    (let [note-m (first tran)
          note (:note/note note-m)
          parent (Long/parseLong (:note/parent note-m))
          length (count note)
          nme (if (< length 130) note (subs note 0 130))
          who (:who note-m)
          note-m* (dissoc note-m :who)
          tstamp (java.util.Date.)]
      (handler [(merge note-m* {:name nme :note/by who :note/parent parent :created-by who :updated-by who :created tstamp :updated tstamp :type :type/note})]))))

(def create-note
  (-> (core/post)
      (add-defaults)
      (core/add-key)
      (core/min-required-keys required-record-keys)
      (core/valid-keys valid-keys)
      (core/remove-empty-keys)))
