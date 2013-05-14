(ns helo.data.events
  (:require [datomic.api :only [db q] :as d]
            [helo.data.core :only [conn] :as core]
            [cheshire.core :as json]
            [clojure.tools.logging :only [info error]]))

(def ez-to-attr
  {:description :event/description
   :by   :event/by
   :at :event/at
   :parent :event/parent
   :type :event/type
  })

(def attr-to-ez 
  (zipmap (vals ez-to-attr) (keys ez-to-attr)))

(defn ent-to-event-map [entity]
  {:description (:event/description entity)
   :byId (:db/id (:event/by entity)) 
   :byName (:name (:event/by entity))
   :id (:db/id entity)
   :type (:event/type entity) 
   :at (:event/at entity) })

(defn build-query [handler]
  (fn [params]
    (if-let [parent (Long/parseLong (:parent params))]
      (handler {:query  '{:find [?e, ?at]
                          :in [$ ?parent]
                          :where [[?e :event/parent ?parent]
                                  [?e :event/at ?at]]}
                :dbs [parent] }) 
      [])))

(defn strip-outer-vec [handler]
  (fn [v]
    (let [m (first v)]
      (handler m))))

(defn gen-response [handler]
  (fn [params]
    (let [response (handler params)
          ents (map #(d/entity (:dbval response) (first %)) (:results response))
          events (into [] (map #(ent-to-event-map %) ents)) ]
      {:status 200
       :headers {"Content-Type" "application/json"}
       :body (json/encode {:limit (:limit response)
                           :offset (:offset response)
                           :count (:count response)
                           :type :event
                           :results events})})))

(def read-events
  (-> (core/query)
      (build-query)
      (core/limit-query)
      (gen-response)
      (strip-outer-vec)
      (core/remove-empty-keys)))

