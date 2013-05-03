(ns helo.service.referrals
  (:require [helo.service.core :as core]
            [helo.data.core :as dc]
            [helo.data.referrals :as rfr]
            [cheshire.core :as json]
  ))

(defn get-referral [id] (rfr/read-referral id))

(defn get-referrals [{params :params}] (println params))

(def create-referral
  (-> (dc/post)
      (rfr/add-defaults)
      (rfr/add-cchan :referral/er-tag :referral/er-cchannel :cchannel.type/unknown )
      (rfr/add-cchan :referral/ee-tag :referral/ee-cchannel :cchannel.type/unknown)
      (rfr/add-note)
      (dc/add-key)
      (dc/translate-keys rfr/ez-to-attr)
      (dc/min-required-keys rfr/required-record-keys)
      (dc/valid-keys rfr/valid-keys)
      (dc/remove-empty-keys)))

(defn post-referral [{params :params}]
  (create-referral params))
