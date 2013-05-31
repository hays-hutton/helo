(defproject helo "0.0.1"
  :description "TEAM1's REST Service"
  :url "https://api.team1.com/"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/tools.logging "0.2.6"]
                 [org.slf4j/slf4j-log4j12 "1.6.4"]
                 [log4j "1.2.17" :exclusions  [javax.mail/mail
                                               javax.jms/jms
                                               com.sun.jdmk/jmxtools
                                               com.sun.jmx/jmxr]]
                 [compojure "1.1.5"]
                 [com.datomic/datomic-free "0.8.3826" :exclusions [org.slf4j/slf4j-nop
                                                                   org.slf4j/log4j-over-slf4j ]]

                 [ring "1.1.8"]
                 [cheshire "5.1.0"]
                 [geocoder-clj "0.1.1"]
                 [org.apache.httpcomponents/httpclient "4.2.2"]
                 [com.draines/postal "1.9.0"]
                 [com.cemerick/bandalore "0.0.3"]
                 [enlive "1.1.1"]
                 [org.antlr/stringtemplate "3.2"]]
  :plugins [[lein-ring "0.8.3"]]
  :ring {:handler helo.handler/app}
  :geocoder-config {:Bing {:key "AscRfO7Whq8KrfHmjteLpt46DN0i24WR0ZREBehz6FeId_yYWR58QqrnAeZxUJye"}}
  :profiles
  {:dev {:dependencies [[ring-mock "0.1.3"]]}})
