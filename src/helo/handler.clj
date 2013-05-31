(ns helo.handler
  (:use compojure.core)
  (:use ring.adapter.jetty)
  (:use ring.middleware.reload)
  (:use [clojure.tools.logging :only [debug info error]])
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [ring.util.response :as resp]
            [helo.service.auth :as auth]
            [helo.service.orgs :as org]
            [helo.service.entities :as ent]
            [helo.service.search :as search]
            [helo.service.notes :as nte]
            [helo.service.events :as evt]
            [helo.service.persons :as per]
            [helo.service.referrals :as rfr]
            [helo.site.template :as site]
            [cheshire.core :as json]
            [clojure.pprint])
  (:import org.apache.commons.codec.binary.Base64)
  (:import org.eclipse.jetty.server.nio.SelectChannelConnector)

)

(derive :role.person/partner :role.person/client)
(derive :role.person/vendor :role.person/client)
(derive :role.person/team-member :role.person/partner)
(derive :role.person/team-member :role.person/vendor)
(derive :role.person/admin :role.person/team-member)
(derive :role.machine/api :role.machine/default)
(derive :role.machine/phone :role.machine/default)

(defn- keyify-params [target]
  (into {}
    (for [[k v] target]
         [(if (string? k) (keyword k) k) v])))
                                                  
(defn keyword-params-request
  "Converts string keys in :params map to keywords."
  [req]
  (update-in req  [:params] keyify-params))

(defn wrap-keyword-params
[handler]
  (fn [req]
    (-> req
        keyword-params-request
        handler)))

(defn format-request [name request]
  (with-out-str 
    (println "----------------")
    (println name)
    (println "----------------")
    (clojure.pprint/pprint request)
    (println "----------------"))) 

(defn spy [handler spy-name]
  (fn [request]
    (let [incoming (format-request (str spy-name ":\n Incoming Request:" ) request)]
      (println incoming)
      (let [response (handler request)]
        (let [outgoing (format-request (str spy-name ":\n Outgoing Response:" ) response)]
          (println outgoing)
          response)))))

(defn- json-request?
  [req]
  (if-let  [#^String type  (:content-type req)]
    (not  (empty?  (re-find #"^application/(vnd.+)?json" type)))))

(defn wrap-json-params [handler]
  (fn [req]
    (if-let [body (and (json-request? req) (:body req))]
      (let [bstr (slurp body)
            json-params (json/parse-string bstr true)
            req* (assoc req
                   :json-params json-params
                   :params (merge (:params req) json-params))]
            (handler req*))
            (handler req))))

(defn wrap-log [handler]
  (fn [req]
    (info (req :uri) (get-in req  [:headers "x-forwarded-for"]) (get-in req  [:headers "x-requested-with"] ) (get-in req  [:headers "accept"])) 
    (handler req)))

(defn request-pin [cell]
  (auth/send-pin cell)
  (resp/file-response "auth.html" {:root "resources/public"}))

(defn post-pin [pin]
  (if-let [auth (auth/post-pin pin)]
    (assoc (resp/redirect-after-post "/") :cookies auth)
    (resp/file-response "auth.html" {:root "resources/public"})))

(defn authorized? [required-roles granted-roles]
  (first (for [granted granted-roles required required-roles :when (isa? granted required)] granted)))

(defn basic-roles [username password]
  (auth/get-roles username password))

(defn cookie-roles [tkey]
  (debug "Cookie-roles called here:" tkey)
  (auth/get-roles tkey))

; thanks to cemerick/friend to say the least
(defn wrap-auth [handler required-roles]
  (fn [{{:strs [authorization]} :headers :as request}]
    (if authorization 
      (if-let [[[_ username password]] (try (-> (re-matches #"\s*Basic\s+(.+)" authorization)
                                                ^String second
                                                (.getBytes "UTF-8")
                                                Base64/decodeBase64
                                                (String. "UTF-8")
                                                (#(re-seq #"([^:]*):(.*)" %))) 
                                         (catch Exception e 
                                           (.printStackTrace e)))]
        (if-let [user-roles (basic-roles username password)]
          (if (authorized? required-roles user-roles )
            (handler request)))) 
      (if-let [tkey (get-in request [:cookies "uuid" :value])]
        (if-let [user-roles (cookie-roles tkey)]
          (if (authorized? required-roles user-roles)
            (handler request))) 
        (if (get-in request [:headers "x-twilio-signature"])
  	  (if-let [phone-roles (auth/twilio-auth-roles request)] 
	    (if (authorized? required-roles phone-roles)
	      (handler request))))))))

(defn wrap-split-https [safer-handler open-handler]
  (fn [req]
    (if (= "https" (get-in req [:headers "x-forwarded-proto"]))
      (safer-handler req)
      (open-handler req))))

(defn wrap-who[handler]
  (fn [req]
    ; Could be invalid uuid but it is still the same "who"
    ;  no race conditions I can see other than get the data 
    (if-let [uuid (get-in req [:cookies "uuid" :value]) ]
      (if-let [who (auth/get-who uuid)]
        (handler (assoc-in req [:params :who] who))
        (handler (assoc-in req [:params :who] auth/anonymous)))
      (handler (assoc-in req [:params :who] auth/anonymous)))))

;Team role with R privilege
(defroutes team-read-routes
  (route/resources "/" )
  (GET "/orgs" request (org/get-orgs request))
  (GET "/entities" request (ent/get-entities request))
  (GET "/search/orgs" request (search/search-orgs request))
  (GET "/search/orgs-list" request (search/search-orgs-list request))
  (GET "/search/people" request (search/search-people request))
  (GET "/search/acctMgr" request (search/search-acct-mgr request))
  (GET "/orgs/:id" [id] (org/get-org id))
  (GET "/persons" request (per/get-persons request))
  (GET "/persons/:id" [id] (per/get-person id))
  (GET "/referrals/:id" request (rfr/get-referral request))
  (GET "/notes" request (nte/get-notes request))
  (GET "/events" request (evt/get-events request))
;  (GET "/notes/:id" request  (nte/get-note request))
)

;Team role with CUD privileges
(defroutes team-write-routes
  (POST "/orgs/:id" request  (org/update-org request))
  (POST "/orgs" request (org/post-orgs request))
  (POST "/persons/:id" request  (per/update-person request))
  (POST "/referrals/:id" request  (rfr/post-referral request))
  (POST "/persons" request (per/post-persons request))
;  (POST "/notes/:id" request  (nte/update-note request))
  (POST "/notes" request (nte/post-notes request)) 
)


(defroutes both-routes
  (route/resources "/" )
)

(defroutes https-routes
  (GET "/" [] {:status 200 :body "Yeeeeellllllllooooo"})
  (GET "/app.html" [] (resp/file-response "app.html" {:root "resources/public"}))
  (GET "/auth.html" [] (resp/file-response "auth.html" {:root "resources/public"}))
  (POST "/request-pin" [cell]  (request-pin cell))
  (POST "/pin" [pin]  (post-pin pin))
  (wrap-auth team-read-routes #{:role.person/team-member})
  (wrap-auth team-write-routes #{:role.person/team-member})
  (route/resources "/" )
)

(defroutes http-routes 
  (GET "/refer.html" [] (resp/file-response "refer.html" {:root "resources/public"}))
  (GET "/images/t1-logo.jpg" [] (resp/file-response "images/t1-logo.jpg" {:root "resources/public"}))
  (GET "/css/foundation.css" [] (resp/file-response "css/foundation.css" {:root "resources/public"}))
  (GET "/css/helo.css" [] (resp/file-response "css/helo.css" {:root "resources/public"}))
  (GET "/favicon.ico" [] (resp/file-response "favicon.ico" {:root "resources/public"}))
  ;Below should be https????
  ;Below should be https maybe????
  (POST "/referrals" request  (rfr/post-referrals request))
  (GET "/hello" request (resp/response "hello world"))
  (GET "/" request (site/index request))
  (GET "/body-shops-st-louis" request (site/shops-stl request))
  (GET "/body-shops-little-rock" request (site/shops-lr request))

  (route/resources "/" )
  (route/not-found (resp/file-response "not-found.html" {:root "resources/public"}))
)

(defn add-connectors [server]
  (let [conn1  (SelectChannelConnector.) 
        conn2 (SelectChannelConnector. ) ]
  (.setPort conn1  8081)
  (.setPort conn2  8080)
  (.addConnector server conn1 )
  (.addConnector server conn2) 
  server))

(def app
  (run-jetty (handler/site (wrap-reload (wrap-who (wrap-keyword-params (wrap-log (wrap-json-params (spy (wrap-split-https https-routes http-routes) "all" )))))) 
                ) {:configurator add-connectors :port 8083}))

