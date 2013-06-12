(ns helo.service.referrals
  (:require [helo.service.core :as core]
            [helo.data.core :as dc]
            [helo.data.referrals :as rfr]
            [helo.service.mq :as mq]
            [cheshire.core :as json]
  ))

;(defn get-referral [id] (rfr/read-referral id))

(defn get-referral [{params :params}] (rfr/read-referral params))

(defn creation-response [handler]
  (fn [params]
    (let [response (handler params)
          entid (:created-id response) ]
      (println "Created Referral: " entid)
      ;TODO try catch? what happens on error???
      (mq/trigger  {:event-type :referral/create :event-entity entid })
      
      {:status 200 :body (json/encode {:id entid :message (str "Created Referral: " entid )} )})))

(defn update-response [handler]
  (fn [params]
    (let [response (handler params)
          entid (:updated-id response)
          payload (first (:tran response))]
      (println "Update Referral Response: " response)
      ;TODO try catch? what happens on error???
      (mq/trigger  {:event-type :referral/update :event-entity entid :payload payload })
      {:status 200 :body (json/encode {:id entid :message (str "Updated Referral: " entid )} )})))

(def create-referral
  (-> (dc/post)
      (creation-response)
      (rfr/add-defaults)
      (rfr/add-cchan :referral/er-tag :referral/er-cchannel :cchannel.type/unknown )
      (rfr/add-cchan :referral/ee-tag :referral/ee-cchannel :cchannel.type/unknown)
      (rfr/add-note)
      (dc/add-key)
      (dc/translate-keys rfr/ez-to-attr)
      (dc/min-required-keys rfr/required-record-keys)
      (dc/valid-keys rfr/valid-keys)
      (dc/remove-empty-keys)))

(def update-referral
  (-> (dc/update-post)
      (update-response)
      (rfr/update)
      (rfr/coerce rfr/coerce-map)
      (rfr/rename {:id :db/id :status :referral/status :owner :referral/owner
                   :eeQuiet :referral/ee-quiet? :erQuiet :referral/er-quiet?})
      (dc/remove-empty-keys)))

(defn post-referrals [{params :params}]
  (create-referral params))

(def web-referral
  (-> (dc/post)
      (creation-response)
      (rfr/add-defaults)
      (rfr/add-cchan :referral/er-tag :referral/er-cchannel :cchannel.type/unknown )
      (rfr/add-cchan :referral/ee-tag :referral/ee-cchannel :cchannel.type/unknown)
      (rfr/add-note)
      (dc/add-key)
      (dc/translate-keys rfr/ez-to-attr)
      (dc/min-required-keys rfr/required-record-keys)
      (dc/valid-keys rfr/valid-keys)
      (dc/remove-empty-keys)))

(defn post-we-come-to-you [{params :params}]
  (println "post-we:" params)
  (let [eePhone (if-not (empty? (params :phone)) (params :phone))
        eeEmail (if-not (empty? (params :email)) (params :email))
        eeTag (or eePhone eeEmail)
]
  
  (if eeTag 
    (web-referral (assoc params :erTag "+19015505058" :eeTag eeTag ))
    {:status 400 :body "bad request need at least a contact"}
)
  )
)

(defn post-referral [{params :params}]
  (println "update:" params)
  (update-referral params)
)
