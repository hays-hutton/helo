(ns helo.data.auth
  (:use [datomic.api :only [db q] :as d]
        [helo.data.core :only [conn] :as core]
        [helo.utils.uri :as utils]
        [clj-time.core :only [from-now minutes weeks] ]
        [clj-time.coerce :only [to-date] ]
        [clojure.tools.logging :only [info error]]))

(defn get-roles
 ([tkey] 
    (let [dbval (db conn)]
      (info "get-roles by tkey " tkey)
      (if-let [entity (d/entity dbval 
                        (ffirst (q '[:find ?e
                                       :in $ ?tkey ?now
                                 :where [?e :person/tkey ?tkey]
                                        [?e :person/tkey-thru ?thru]
                                        [(< ?now ?thru)]]
                                   dbval tkey (java.util.Date.))))]
      (:person/roles entity)
      [:none])))
  ([username password]
    (let [dbval (db conn)]
      (info "get-roles by username " username password)
      (if-let [entity (d/entity dbval 
                        (ffirst (q '[:find ?e
                                       :in $ ?username ?password
                                    :where [?e :user/user ?username]
                                           [?e :user/password ?password]]
                                     dbval username password)))]
        (:user/roles entity)
        [:none]))))

(defn get-who [tkey]
  (ffirst (q '[:find ?e 
                 :in $ ?tkey
              :where [?e :person/tkey ?tkey] ]
                 (db conn) tkey)))

(defn gen-pin []
  (str (rand-int 99999999)))

;; 
;; I can't imagine there would ever be a collision of pins but maybe
(defn generate-pin [cell]
  (let [ pid (ffirst (q '[:find ?e :in $ ?cell :where [?e :person/cchannels ?c] [?c :cchannel/type :cchannel.type/sms] [?c :cchannel/cchannel ?cell]] (db conn) cell))
        pin (gen-pin) 
        tkey (d/squuid)
        tran {:db/id pid :person/pin pin :person/pin-thru (to-date (-> 5 minutes from-now)) :person/tkey tkey :person/tkey-thru (to-date (-> 3 weeks from-now))}]
  (if @(d/transact conn [tran]) 
    pin 
    (let [pin (gen-pin)
          tran2 {:db/id pid :person/pin pin :person/pin-thru (to-date (-> 5 minutes from-now)) :person/tkey tkey :person/tkey-thru (to-date (-> 3 weeks from-now))}]
       (if @(d/transact conn [tran2])
         pin)))))

(defn get-tkey [pin]
  (first (q '[:find ?tkey ?thru
                 :in $ ?pin
              :where [?e :person/tkey ?tkey]
                     [?e :person/tkey-thru ?thru]
                     [?e :person/pin ?pin]]
                 (db conn) pin)))

(defn get-roles-by-tkey [tkey]
  (ffirst (q '[:find [?roles ...]
                 :in $ ?tkey
              :where [?e :person/tkey ?tkey]
                     [?e :person/roles ?roles]]
                 (db conn) tkey)))


(defn get-roles-by-pin [pin]
  (ffirst (q '[:find [?roles ...]
                 :in $ ?pin
              :where [?e :person/pin ?pin]
                     [?e :person/roles ?roles]]
                 (db conn) pin)))

(defn get-pin-key [cell]
  (ffirst (q '[:find ?pin, ?key
                 :in $ ?cell
              :where [?e :person/pin ?pin]
                     [?e :person/temp-key ?key]
                     [?e :person/cchannels ?chan]
                     [?chan :cchannel/type :cchannel.type/cell]
                     [?chan :cchannel/cchannel ?cell]]
                 (db conn) cell)))
