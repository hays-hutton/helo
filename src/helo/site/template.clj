(ns helo.site.template
  (:require [net.cgrand.enlive-html :as html])
)

(def layout "helo/site/layout.html")
(def snips "helo/site/snips.html")

(def lr-shops 
{:shops [
  {:name "West Little Rock"
   :street "12208 West Markham St"
   :city "Little Rock"
   :state "AR"
   :zip "72211"
   :lon "-92.4107525"
   :lat "34.7544551"
   :hours "M-F 8AM-5PM"
   :phone "(501) 224-5828" 
   :fax ""
   :url "/body-shops-little-rock/body-shop-west-little-rock.html"
   :label "1"
  }
  {:name "North Little Rock"
   :street "7100 Landers Rd"
   :city "North Little Rock"
   :state "AR"
   :zip "72117"
   :lon "-92.1979996"
   :lat "34.818306199999"
   :hours "M-F 8AM-5PM"
   :phone "(501) 834-4008" 
   :fax ""
   :url "/body-shops-little-rock/body-shop-north-little-rock.html"
   :label "2"
}
  {:name "Cabot"
   :street "114 Financial Dr"
   :city "Cabot"
   :state "AR"
   :zip "72023"
   :lon "-92.058354"
   :lat "34.9468"
   :hours "M-F 8AM-5PM"
   :phone "(501) 941-3711" 
   :fax "" 
   :url "/body-shops-little-rock/body-shop-cabot.html"
   :label "3"
}] 
  :map {:center "center=34.870264,-92.212372"
        :zoom "zoom=11"
        :size "size=800x494"
       }        
})

(def stl-shops 
{:shops [
  {:name "Clayton"
   :street "125 Hunter Ave"
   :city "Clayton"
   :state "MO"
   :zip "63124"
   :lon "-90.349597 "
   :lat "38.648904"
   :hours "M-F 8AM-5PM"
   :phone "(314) 315-4668" 
   :fax "(314) 288-0085"
   :url "/body-shops-st-louis/body-shop-clayton.html"
   :color "purple"
   :label "1"
  }
  {:name "Shrewsbury"
   :street "7300 Watson Rd"
   :city "Shrewsbury"
   :state "MO"
   :zip "63119"
   :lon "-90.3277304"
   :lat "38.578609"
   :hours "M-F 8AM-5PM"
   :phone "(314) 962-0050" 
   :fax ""
   :url "/body-shops-st-louis/body-shop-shrewsbury.html"
   :color "orange"
   :label "2"
}
  {:name "Chesterfield"
   :street "638 Goddard Ave"
   :city "Chesterfield"
   :state "MO"
   :zip "63005"
   :lon "-90.6349979"
   :lat "38.6672131"
   :hours "M-F 8AM-5PM"
   :phone "(636) 733-9000" 
   :fax "" 
   :url "/body-shops-st-louis/body-shop-chesterfield.html"
   :color "red"
   :label "3"
}
  {:name "Florissant"
   :street "1400 Lindbergh Blvd"
   :city "Florissant"
   :state "MO"
   :zip "63031"
   :lon "-90.32029639999999"
   :lat "38.798079"
   :hours "M-F 8AM-5PM"
   :phone "(314) 921-6569" 
   :fax ""
   :url "/body-shops-st-louis/body-shop-florissant.html"
   :color "green"
   :label "4"
  }] 
  :map {:center "center=38.69092,-90.439453"
        :zoom "zoom=11"
        :size "size=800x494"
       }        
})

(html/defsnippet home snips [:div#home] [])


(html/defsnippet shop snips [:div#shop] [])
(html/defsnippet estimates snips [:div#estimates] [])
(html/defsnippet repair snips [:div#repair] [])
(html/defsnippet dents snips [:div#dents] [])
(html/defsnippet rentals snips [:div#rentals] [])
(html/defsnippet glass snips [:div#glass] [])


(def map-base-url "http://maps.googleapis.com/maps/api/staticmap?")

(defn markers [m]
  (let [shops (:shops m)]
   (apply str
    (map #(str "&markers=color:yellow" "%7Clabel:" (:label %1) "%7C" (:lat %1) "," (:lon %1) ) shops ))
   ))

(defn static-url [m]
  (apply str map-base-url (get-in m [:map :center]) "&"
                          (get-in m [:map :zoom]) "&"
                          (get-in m [:map :size]) (markers m)  "&sensor=false" ))

(html/defsnippet shops-snip snips [:div#shopsList] [m]
  [:div#shop] (html/clone-for [shop (:shops m)]
                [(html/attr= :prop "order")] (html/content (str (shop :label) ".  "))
                [(html/attr= :itemprop "name")] (html/content (shop :name) )
                [(html/attr= :itemprop "street-address")] (html/content (shop :street)) 
                [(html/attr= :itemprop "locality")] (html/content (shop :city)) 
                [(html/attr= :itemprop "region")] (html/content (shop :state)) 
                [(html/attr= :itemprop "tel")] (html/do-> 
                                                 (html/content (shop :phone))  
                                                 (html/set-attr :href (str "tel:" (shop :phone)))) 
                [(html/attr= :itemprop "url")] (html/set-attr :href (shop :url)) ) 
  [:div#map :img] (html/set-attr :src (static-url m))
              
)

(html/deftemplate index layout [ctxt]
  [:div#main] (html/content (home))
)

(html/deftemplate shops-stl layout [ctxt]
  [:div#main] (html/content (shops-snip stl-shops))
)

(html/deftemplate shops-lr layout [ctxt]
  [:div#main] (html/content (shops-snip lr-shops))
)


(html/deftemplate free-estimates layout [ctxt]
  [:div#main] (html/content (estimates))
)

(html/deftemplate collision-repair layout [ctxt]
  [:div#main] (html/content (repair))
)

(html/deftemplate dent-repair layout [ctxt]
  [:div#main] (html/content (dents))
)


(html/deftemplate car-rentals layout [ctxt]
  [:div#main] (html/content (rentals))
)

(html/deftemplate auto-glass layout [ctxt]
  [:div#main] (html/content (glass))
)
