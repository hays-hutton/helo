(ns helo.service.referrals
  (:require [helo.service.core :as core]
            [helo.data.referrals :as rfr]
            [cheshire.core :as json]
  ))

(defn get-referral [id]
  (rfr/read-referral id))

(defn get-referrals [{params :params}]
  (rfr/read-referrals params))

(defn post-referral [{params :params}]
  (rfr/create-referral params))

