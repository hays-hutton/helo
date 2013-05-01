(ns helo.data.cchannels
  (:use [datomic.api :only [db q] :as d]
        [helo.data.core :only [conn] :as core]
        [helo.utils.uri :as utils]
        [clj-time.core :only [from-now minutes weeks] ]
        [clj-time.coerce :only [to-date] ]
        [clojure.tools.logging :only [info error]]))

(def cchannel-type-to-label
  {:cchannel.type/cell "cell"
   :cchannel.type/work "work"
   :cchannel.type/fax "fax"
   :cchannel.type/home "home"
   :cchannel.type/sms "sms"
   :cchannel.type/email "email"
   :cchannel.type/unknown "unknown"
})

(defn phone-map [id tel phone-type who]
  (let [tstamp (java.util.Date.)]
    {:db/id id
     :name  (str (cchannel-type-to-label phone-type) ":" (utils/raw tel)) 
     :cchannel/cchannel tel
     :cchannel/type phone-type
     :updated-by who
     :created-by who
     :updated tstamp
     :created tstamp
     :type :type/cchannel
    }))
