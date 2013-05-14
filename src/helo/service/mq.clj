(ns helo.service.mq
  (:require [helo.service.core :as core]
            [cemerick.bandalore :as sqs]))

(def client (sqs/create-client core/aws-id core/aws-key))

(def immediate-q (sqs/create-queue client "immediate"))

(def delay-1-q (sqs/create-queue client "delay1"))
(sqs/queue-attrs client delay-1-q {"DelaySeconds" "60"})
(sqs/queue-attrs client delay-1-q {"MessageRetentionPeriod" "1209600"})

(def delay-2-q (sqs/create-queue client "delay2"))
(sqs/queue-attrs client delay-2-q {"DelaySeconds" "120"})

(def delay-3-q (sqs/create-queue client "delay3"))
(sqs/queue-attrs client delay-3-q {"DelaySeconds" "180"})

(def delay-10-q (sqs/create-queue client "delay10"))
(sqs/queue-attrs client delay-10-q {"DelaySeconds" "600"})
(sqs/queue-attrs client delay-10-q {"MessageRetentionPeriod" "1209600"})

(def delay-15-q (sqs/create-queue client "delay15"))
(sqs/queue-attrs client delay-15-q {"DelaySeconds" "900"})
(sqs/queue-attrs client delay-15-q {"MessageRetentionPeriod" "1209600"})

(defn trigger [msg]
  (let [msg* (assoc msg :at (java.util.Date.))]
    (sqs/send client immediate-q (pr-str msg*))))

(defn trigger-in-1 [msg]
  (let [msg* (assoc msg :at (java.util.Date.))]
    (sqs/send client delay-1-q (pr-str msg*))))

(defn trigger-in-2 [msg]
  (let [msg* (assoc msg :at (java.util.Date.))]
    (sqs/send client delay-2-q (pr-str msg*))))

(defn trigger-in-3 [msg]
  (let [msg* (assoc msg :at (java.util.Date.))]
    (sqs/send client delay-3-q (pr-str msg*))))

(defn trigger-in-10 [msg]
  (let [msg* (assoc msg :at (java.util.Date.))]
    (sqs/send client delay-10-q (pr-str msg*))))

(defn trigger-in-15 [msg]
  (let [msg* (assoc msg :at (java.util.Date.))]
    (sqs/send client delay-15-q (pr-str msg*))))
