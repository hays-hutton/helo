(ns helo.utils.uri
  (:require [clojure.string :as string])
)




(defn to-email [value]
  (try 
    (let [cleaner (clojure.string/trim value)
          matches (re-matches #"^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$" cleaner)]
      (if matches
        (str "email:" cleaner)))
    (catch Exception e nil))
)

(defn to-tel [value]
  (if-not (re-matches #"tel:\+1\d{10}" value)
    (try 
      (let [matches (re-matches #"1{0,1}([2-9]\d{9})(.*)" (clojure.string/replace value #"[\+\-\s\(\)]" ""))]
        (str "tel:+1" (matches 1)))
      (catch Exception e nil))
    value))

(defn to-sms [value]
  (if-not (re-matches #"sms:\+1\d{10}" value)
    (try 
      (let [matches (re-matches #"1{0,1}([2-9]\d{9})(.*)" (clojure.string/replace value #"[\+\-\s\(\)]" ""))]
        (str "sms:+1" (matches 1)))
      (catch Exception e nil))
    value))

(defn to-unk [value]
  (str "unk:" value))

(defn to-uri [value]
  (or (to-tel value) (to-email value) (to-unk value)))

(defn p-tel [value]
  (if-not (nil? value)
    (when-let [matches (re-matches #"tel:\+1(\d{3})(\d{3})(\d{4})" value)]  
      (str "tel:+1(" (matches 1) ")" (matches 2) "-" (matches 3)))))

(defn e164 [value]
  (string/replace value #"tel:|sms:" ""))

(defn raw [value]
  (string/replace value #"tel:\+1|email:|sms:|unk:" ""))

(defn searchable [value]
  (println "value:" value)
  (string/replace (to-uri value) #"\+1" ""))

(defn to-map [uri]
  (if-let [v (clojure.string/split uri #":" 2)]
    {:scheme (first v)
     :resource (second v)
     :uri uri
    }))

;TODO
(defn uri-sms [value]
  (if (re-matches #"^sms:.*" value) value (str "sms:"  value)))

;???

(defn uuid [uid]
  (if (string? uid) (java.util.UUID/fromString uid) uid))
