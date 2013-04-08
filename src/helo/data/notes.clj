(ns helo.data.notes
  (:use [datomic.api :only [db q] :as d]
        [helo.data.core :only [conn] :as core]
        [helo.utils.uri :as utils]
        [clojure.string :as string]
        [clj-time.core :only [from-now minutes weeks] ]
        [clj-time.coerce :only [to-date] ]
        [clojure.tools.logging :only [info error]]))

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
