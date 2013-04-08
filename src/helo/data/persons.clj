(ns helo.data.persons
  (:use [datomic.api :only [db q] :as d]
        [helo.data.core :only [conn] :as core]
        [helo.utils.uri :as utils]
        [clj-time.core :only [from-now minutes weeks] ]
        [clj-time.coerce :only [to-date] ]
        [clojure.tools.logging :only [info error]]))

(def valid-person-request-keys
  [:person/first-name
   :person/last-name
   :person/org
   :person/acct-mgr
   :person/cell
   :person/home
   :person/work
   :person/fax
   :note/note
   :address/address])

(def post-persons 
  (core/valid-keys (core/min-required-keys (core/post) [:person/last-name :person/cell])  valid-person-request-keys) )

(def p-persons
  (-> (core/post)
      (core/min-required-keys [:person/last-name :person/cell :person/first-name])
      (core/valid-keys valid-person-request-keys)
      (core/remove-empty-keys)))
