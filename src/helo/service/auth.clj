(ns helo.service.auth
  (:use [clojure.tools.logging :only [info error]])
  (:require [helo.service.core :as core]
            [helo.data.auth :as auth]
            [helo.utils.uri :as utils])
  (:import [javax.crypto Mac]
           [javax.crypto.spec SecretKeySpec]
           [org.apache.commons.codec.binary Base64]))

(defn twilio-signature [params url]
  (let [keySpec (SecretKeySpec. (.getBytes core/auth-tkn) "HmacSHA1")
        sorted (for [k (sort (keys params))]  [(name k)  (k params)])
        msg (str url (apply str (map #(str (first %)  (second %)) sorted)))
        mac (Mac/getInstance "HmacSHA1")
        mac1 (.init mac keySpec)
        raw (.doFinal mac (.getBytes msg))
        the-auth (String. (Base64/encodeBase64 raw)) ] 
          (info "KeySpec:" keySpec)
          (info "Params:" params)
          (info "Sorted:" sorted)
          (info "The Message:" msg)
          (info "mac:" mac)
          (info "mac1:" mac1)
          (info "The Raw:" raw)
          (info "The Auth:" the-auth)
          the-auth))

(defn twilio-auth-roles [{params :params uri :uri {signature "x-twilio-signature"} :headers}]
  (info "2Params" params)
  (info "2signature" signature)
  (if (= (twilio-signature params (str "https://api.team1.com"  uri)) signature)
    #{:role.machine/phone}))

(defn get-roles 
  ([tkey] (auth/get-roles (utils/uuid tkey)))
  ([username password] (auth/get-roles username password)))

;MAGIC anonymous?? could query on something else why better though?
(def anonymous (auth/get-who (utils/uuid "5159acae-b10a-4c60-bf16-b8957180afac")))

(defn get-who [tkey]
  (auth/get-who (utils/uuid tkey)))

(defn send-pin [cell]
  (info "Sending PIN to " cell)
  (core/send-pin-sms {} cell)
)

(defn post-pin [pin]
  (info "Received PIN " pin)
  (let [[tkey thru] (auth/get-tkey pin)]
    {"uuid"
      {:value (str tkey)
       :expires (str thru)
      }
  }))
