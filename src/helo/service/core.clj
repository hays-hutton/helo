(ns helo.service.core
  (:require [helo.service.st :as st]
            [helo.data.auth :as auth]
            [helo.utils.uri :as utils]
            [postal.core :as pc]
            [geocoder.core :as geo]
            [clj-http.client :as http]
            [cheshire.core :as json])
)

(def smtp-user "AKIAIGVAFOARJIOXITGQ")
(def smtp-pwd "Ar1RaN3FW8P0BD641BOPnKth5f2SqIKrNIDNZAaN+KvB")
(def smtp-server "email-smtp.us-east-1.amazonaws.com" )

(def acct-sid "ACb34a5bb9ec10b33e26ced611f9210b6d")
(def auth-tkn "f8a883669ea3447ecc9ed2750ff888e8")
(def phone-from "+19012354363")

(def er-sms-msg1 (st/get-view-from-classpath "helo/service/st/er-we-got-it-1"))
(def ee-sms-msg1 (st/get-view-from-classpath "helo/service/st/ee-will-call-1"))
(def t1-sms-msg1 (st/get-view-from-classpath "helo/service/st/t1-new-ref-1"))
(def er-email-msg1 (st/get-view-from-classpath "helo/service/st/er-email-msg1"))
(def pin-sms-msg (st/get-view-from-classpath "helo/service/st/pin-sms-msg1"))

(def twAuth (str acct-sid ":" auth-tkn))
(def twVer "2010-04-01")
(def twilio-url (str "https://api.twilio.com/" twVer "/Accounts/" acct-sid))

(def map-twilio-to-api {"to" :comm/to "from" :comm/from "body" :comm/body "sid" :uuid "date_created" :created "date_updated" :updated "status" :comm/status})

(defn send-pin-sms [person cell-164]
  (let [response  (http/post (str twilio-url "/SMS/Messages.json")
                            {:basic-auth twAuth 
                             :form-params {:To cell-164 
                                           :From (utils/e164 (utils/to-tel phone-from))
                                           :Body (st/render pin-sms-msg {:pin (auth/generate-pin (utils/to-sms cell-164))})}}) 
       clean (into {:comm/type :comm.type/sms} (for [[k v] (json/decode (:body response)) ] [(map-twilio-to-api k) v ])) ]
;     (comm/create-comm clean)
))

(defn assoc-geocode [m]
  (if (:address m)
    (if-let [loc (first (geo/geocode-address (:address m)))] 
        (merge m 
          {:address/street (str (:street-number loc) " " (:street-name loc))
           :address/city (:city loc)
           :address/region (:region loc)
           :address/postal-code (:postal-code loc)
           :address/latitude (:latitude (:location loc))
           :address/longitude (:longitude (:location loc))
           :address/provider (:provider loc)
           :address/address (:address m)
          }) m) m))
