(ns helo.data.persons
  (:use [datomic.api :only [db q] :as d]
        [helo.data.core :only [conn] :as core]
        [helo.data.address :as addr]
        [helo.data.cchannels :as cchan]
        [helo.data.notes :as notes]
        [helo.utils.uri :as utils]
        [cheshire.core :as json]
        [clj-time.core :only [from-now minutes weeks] ]
        [clj-time.coerce :only [to-date] ]
        [clojure.tools.logging :only [info error]]))



;TODO does :who belong here?
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
   :note/note
   :who])

(def valid-keys (vec (flatten (conj person-record-keys addr/address-record-keys))))

(def required-record-keys
  [:person/last-name
   :person/first-name])

(defn add-defaults [handler]
  (fn [tran]
    (let [per (first tran)
          nme (str (:person/last-name per) ", " (:person/first-name per))
          who (:who per)
          per* (dissoc per :who)
          tstamp (java.util.Date.)
          tail (rest tran)]
      (handler (vec (flatten (conj [(merge per* {:name nme :created-by who :updated-by who :created tstamp :updated tstamp :type :type/person})] tail)))))))

(defn add-note [handler]
  (fn [tran]
    (let [per (first tran)]
      (if (contains? per :note/note)
        (if-not (clojure.string/blank? (:note/note per))
          (let [note-id (d/tempid :db.part/user)
                note (:note/note per)
                who (:who per)
                parent-id (:db/id per)
                per* (dissoc per :note/note)]
            (if-let [tail (rest tran)]
              (handler (vec (flatten [per* tail (notes/note-map parent-id note who)])))
              (handler [ per*(notes/note-map parent-id note who)])))
          {:status 400 :body (json/encode {:message (str "Bad note for :note/note " (:note/note per))})})
        (handler tran)))))

(defn add-email [handler]
  (fn [tran]
    (let [per (first tran)]
      (if (contains? per :person/email)
        (if-let [email (utils/to-email (:person/email per))]
          (let [email-id (d/tempid :db.part/user)
                who (:who per)
                chans (:person/cchannels per)
                per* (dissoc per :person/email)]
            (if-let [tail (rest tran)]
              (handler (vec (flatten [(assoc per* :person/cchannels (conj chans email-id)) tail (cchan/phone-map email-id email :cchannel.type/email who)])))
              (handler [(assoc per*  :person/cchannels (conj chans email-id)) (cchan/phone-map email-id email :cchannel.type/email who)])))
          {:status 400 :body (json/encode {:message (str "Bad email for :person/email " (:person/email per))})})
        (handler tran)))))

(defn add-sms [handler]
  (fn [tran]
    (let [per (first tran)]
      (if (contains? per :person/sms)
        (if-let [sms (utils/to-sms (:person/sms per))]
          (let [sms-id (d/tempid :db.part/user)
                who (:who per)
                chans (:person/cchannels per)
                per* (dissoc per :person/sms)]
            (if-let [tail (rest tran)]
              (handler (vec (flatten [(assoc per* :person/cchannels (conj chans sms-id)) tail (cchan/phone-map sms-id sms :cchannel.type/sms who)])))
              (handler [(assoc per*  :person/cchannels (conj chans sms-id)) (cchan/phone-map sms-id sms :cchannel.type/sms who)])))
          {:status 400 :body (json/encode {:message (str "Bad sms number for :person/sms " (:person/sms per))})})
        (handler tran)))))

(defn add-phone [handler k phone-type]
  (fn [tran]
    (let [per (first tran)]
      (if (contains? per k)
        (if-let [phone (utils/to-tel (k per))]
          (let [phone-id (d/tempid :db.part/user)
                who (:who per)
                chans (:person/cchannels per)
                per* (dissoc per k)]
            (if-let [tail (rest tran)]
              (handler (vec (flatten [(assoc per* :person/cchannels (conj chans phone-id)) tail (cchan/phone-map phone-id phone phone-type who)])))
              (handler [(assoc per*  :person/cchannels (conj chans phone-id)) (cchan/phone-map phone-id phone phone-type who)])))
          {:status 400 :body (json/encode {:message (str "Bad phone number for " k " " (k per))})})
        (handler tran)))))

(def create-person
  (-> (core/post)
      (add-defaults)
      (add-note)
      (add-sms)
      (add-email)
      (add-phone :person/fax :cchannel.type/fax)
      (add-phone :person/work :cchannel.type/work)
      (add-phone :person/home :cchannel.type/phone)
      (add-phone :person/cell :cchannel.type/cell)
      (core/add-key)
      (core/min-required-keys required-record-keys)
      (core/valid-keys valid-keys)
      (core/remove-empty-keys)
))
