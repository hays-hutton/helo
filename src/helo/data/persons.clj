(ns helo.data.persons
  (:use [datomic.api :only [db q] :as d]
        [helo.data.core :only [conn] :as core]
        [helo.data.address :as addr]
        [helo.utils.uri :as utils]
        [clj-time.core :only [from-now minutes weeks] ]
        [clj-time.coerce :only [to-date] ]
        [clojure.tools.logging :only [info error]]))

(def person-record-keys
  [:person/first-name
   :person/last-name
   :person/org
   :person/acct-mgr
   :person/email
   :person/cell
   :person/home
   :person/work
   :person/fax
   :note/note])

(def valid-keys (vec (flatten (conj person-record-keys addr/address-record-keys))))

(def required-record-keys
  [:person/last-name
   :person/first-name])

(def create-person
  (-> (core/post)
      (core/add-key)
      (core/min-required-keys required-record-keys)
      (core/valid-keys valid-keys)
      (core/remove-empty-keys)
))
