[
  ;; An External Key. Every entity has to have one. It is generic too.
  ;; and it should never change in the world of REST.
  {:db/id #db/id[:db.part/db]
   :db/ident :url
   :db/valueType :db.type/string
   :db/unique :db.unique/identity
   :db/index true
   :db/cardinality :db.cardinality/one
   :db.install/_attribute :db.part/db
  }

  ;; An External Key. Every entity has to have one. It is generic too.
  {:db/id #db/id[:db.part/db]
   :db/ident :uuid
   :db/valueType :db.type/uuid
   :db/unique :db.unique/identity
   :db/index true
   :db/cardinality :db.cardinality/one
   :db.install/_attribute :db.part/db
  }

  ;; Keep trying to ident entities by assuming a namespaced key
  ;; why not just type it then can query a generic key?
  ;; When moved away from a well known :{entity-type}/uuid, there is a requirement
  ;; to have something well-known to select on
  {:db/id #db/id[:db.part/db]
   :db/ident :type
   :db/valueType :db.type/ref
   :db/cardinality :db.cardinality/one
   :db.install/_attribute :db.part/db
  }

  ;; Every entity should be represented below
  [:db/add #db/id [:db.part/user] :db/ident :type/person]
  [:db/add #db/id [:db.part/user] :db/ident :type/org]
  [:db/add #db/id [:db.part/user] :db/ident :type/referral]
  [:db/add #db/id [:db.part/user] :db/ident :type/note]
  [:db/add #db/id [:db.part/user] :db/ident :type/cchannel]
  [:db/add #db/id [:db.part/user] :db/ident :type/comm]
  [:db/add #db/id [:db.part/user] :db/ident :type/address]

  ;; Every entity should have one of these. That way clients can assume it.
  {:db/id #db/id[:db.part/db]
   :db/ident :name
   :db/valueType :db.type/string
   :db/cardinality :db.cardinality/one
   :db/fulltext true
   :db.install/_attribute :db.part/db
  }

  {:db/id #db/id[:db.part/db]
   :db/ident :created
   :db/valueType :db.type/instant
   :db/cardinality :db.cardinality/one
   :db.install/_attribute :db.part/db
  }

  ;who created?
  {:db/id #db/id[:db.part/db]
   :db/ident :created-by
   :db/valueType :db.type/ref
   :db/cardinality :db.cardinality/one
   :db.install/_attribute :db.part/db
  }

  {:db/id #db/id[:db.part/db]
   :db/ident :updated
   :db/valueType :db.type/instant
   :db/cardinality :db.cardinality/one
   :db.install/_attribute :db.part/db
   :db/index true
  }

  ;who did the last update?
  {:db/id #db/id[:db.part/db]
   :db/ident :updated-by
   :db/valueType :db.type/ref
   :db/cardinality :db.cardinality/one
   :db.install/_attribute :db.part/db
  }

  ;; person

  {:db/id #db/id[:db.part/db]
   :db/ident :person/first-name
   :db/valueType :db.type/string
   :db/cardinality :db.cardinality/one
   :db.install/_attribute :db.part/db
  }

  {:db/id #db/id[:db.part/db]
   :db/ident :person/last-name
   :db/valueType :db.type/string
   :db/cardinality :db.cardinality/one
   :db.install/_attribute :db.part/db
  }

  {:db/id #db/id[:db.part/db]
   :db/ident :person/cchannels
   :db/valueType :db.type/ref
   :db/cardinality :db.cardinality/many
   :db.install/_attribute :db.part/db
  }

  {:db/id #db/id[:db.part/db]
   :db/ident :person/org
   :db/valueType :db.type/ref
   :db/cardinality :db.cardinality/one
   :db.install/_attribute :db.part/db
  }

  {:db/id #db/id[:db.part/db]
   :db/ident :person/type
   :db/valueType :db.type/ref
   :db/cardinality :db.cardinality/one
   :db.install/_attribute :db.part/db
  }

  [:db/add #db/id [:db.part/user] :db/ident :person.type/team-member]
  [:db/add #db/id [:db.part/user] :db/ident :person.type/partner]
  [:db/add #db/id [:db.part/user] :db/ident :person.type/client]
  [:db/add #db/id [:db.part/user] :db/ident :person.type/vendor]

  {:db/id #db/id[:db.part/db]
   :db/ident :person/acct-mgr
   :db/valueType :db.type/ref
   :db/cardinality :db.cardinality/one
   :db.install/_attribute :db.part/db
  }

  {:db/id #db/id[:db.part/db]
   :db/ident :person/pin
   :db/valueType :db.type/string
   :db/cardinality :db.cardinality/one
   :db/unique :db.unique/value
   :db.install/_attribute :db.part/db
  }

  {:db/id #db/id[:db.part/db]
   :db/ident :person/pin-thru
   :db/valueType :db.type/instant
   :db/cardinality :db.cardinality/one
   :db.install/_attribute :db.part/db
  }

  {:db/id #db/id[:db.part/db]
   :db/ident :person/tkey
   :db/valueType :db.type/uuid
   :db/cardinality :db.cardinality/one
   :db.install/_attribute :db.part/db
  }

  {:db/id #db/id[:db.part/db]
   :db/ident :person/tkey-thru
   :db/valueType :db.type/instant
   :db/cardinality :db.cardinality/one
   :db.install/_attribute :db.part/db
  }

  {:db/id #db/id[:db.part/db]
   :db/ident :person/roles
   :db/valueType :db.type/ref
   :db/cardinality :db.cardinality/many
   :db.install/_attribute :db.part/db
  }

  ;; referral
  ;; the goal 
  ;;

  ;; referrER-TAG 
  {:db/id #db/id[:db.part/db]
   :db/ident :referral/er-tag
   :db/valueType :db.type/string
   :db/cardinality :db.cardinality/one
   :db.install/_attribute :db.part/db }

  ;; er-tag-type
  ;; referrER-TAG-TYPE ->  referral-tag.type enum
  ;; as will referrEE-TAG-TYPE
  {:db/id #db/id[:db.part/db]
   :db/ident :referral/er-tag-type
   :db/valueType :db.type/ref
   :db/cardinality :db.cardinality/one
   :db.install/_attribute :db.part/db
  }

  {:db/id #db/id[:db.part/db]
   :db/ident :referral/ee-tag
   :db/valueType :db.type/string
   :db/cardinality :db.cardinality/one
   :db.install/_attribute :db.part/db
  }

  {:db/id #db/id[:db.part/db]
   :db/ident :referral/ee-tag-type
   :db/valueType :db.type/ref
   :db/cardinality :db.cardinality/one
   :db.install/_attribute :db.part/db
  }

  [:db/add #db/id[:db.part/user] :db/ident :referral-tag.type/phone]
  [:db/add #db/id[:db.part/user] :db/ident :referral-tag.type/cell]
  [:db/add #db/id[:db.part/user] :db/ident :referral-tag.type/land-line]
  [:db/add #db/id[:db.part/user] :db/ident :referral-tag.type/email]
  [:db/add #db/id[:db.part/user] :db/ident :referral-tag.type/unknown]

  ;; the person who referred (if known/identified)
  {:db/id #db/id[:db.part/db]
   :db/ident :referral/er-cchannel
   :db/valueType :db.type/ref
   :db/cardinality :db.cardinality/one
   :db.install/_attribute :db.part/db
  }

  ;; the person who was referred (if known)
  {:db/id #db/id[:db.part/db]
   :db/ident :referral/ee-cchannel
   :db/valueType :db.type/ref
   :db/cardinality :db.cardinality/one
   :db.install/_attribute :db.part/db
  }

  ;; TEAM1 person who owns it
  {:db/id #db/id[:db.part/db]
   :db/ident :referral/owner
   :db/valueType :db.type/ref
   :db/cardinality :db.cardinality/one
   :db.install/_attribute :db.part/db
  }

  {:db/id #db/id[:db.part/db]
   :db/ident :referral/status
   :db/valueType :db.type/ref
   :db/cardinality :db.cardinality/one
   :db.install/_attribute :db.part/db
  }

  [:db/add #db/id[:db.part/user] :db/ident :referral.status/new]
  [:db/add #db/id[:db.part/user] :db/ident :referral.status/owned]
  [:db/add #db/id[:db.part/user] :db/ident :referral.status/scheduled]
  [:db/add #db/id[:db.part/user] :db/ident :referral.status/in]
  [:db/add #db/id[:db.part/user] :db/ident :referral.status/completed]
  [:db/add #db/id[:db.part/user] :db/ident :referral.status/other]
  [:db/add #db/id[:db.part/user] :db/ident :referral.status/cancelled]

  ;; Notes should be able to be attached to any other 
  ;; entity
  {:db/id #db/id[:db.part/db]
   :db/ident :note/note
   :db/valueType :db.type/string
   :db/cardinality :db.cardinality/one
   :db.install/_attribute :db.part/db
  }
 
  {:db/id #db/id[:db.part/db]
   :db/ident :note/parent
   :db/valueType :db.type/ref
   :db/cardinality :db.cardinality/one
   :db.install/_attribute :db.part/db
  }

  {:db/id #db/id[:db.part/db]
   :db/ident :note/by
   :db/valueType :db.type/ref
   :db/cardinality :db.cardinality/one
   :db.install/_attribute :db.part/db
  }

  {:db/id #db/id[:db.part/db]
   :db/ident :note/visibility
   :db/valueType :db.type/ref
   :db/cardinality :db.cardinality/one
   :db.install/_attribute :db.part/db
  }

  ;; internal->partner->all
  [:db/add #db/id [:db.part/user] :db/ident :visibility/team-member]
  [:db/add #db/id [:db.part/user] :db/ident :visibility/partner]
  [:db/add #db/id [:db.part/user] :db/ident :visibility/client]

  ;; Organization 
  {:db/id #db/id[:db.part/db]
   :db/ident :org/acct-mgr
   :db/valueType :db.type/ref
   :db/cardinality :db.cardinality/one
   :db.install/_attribute :db.part/db
  }

  {:db/id #db/id[:db.part/db]
   :db/ident :org/parent
   :db/valueType :db.type/ref
   :db/cardinality :db.cardinality/one
   :db.install/_attribute :db.part/db
  }

  {:db/id #db/id[:db.part/db]
   :db/ident :org/cchannels
   :db/valueType :db.type/ref
   :db/cardinality :db.cardinality/many
   :db.install/_attribute :db.part/db
  }

  {:db/id #db/id[:db.part/db]
   :db/ident :org/type
   :db/valueType :db.type/ref
   :db/cardinality :db.cardinality/one
   :db.install/_attribute :db.part/db
  }

  [:db/add #db/id [:db.part/user] :db/ident :org.type/insuror]
  [:db/add #db/id [:db.part/user] :db/ident :org.type/agency]
  [:db/add #db/id [:db.part/user] :db/ident :org.type/vendor]
  [:db/add #db/id [:db.part/user] :db/ident :org.type/partner]
  [:db/add #db/id [:db.part/user] :db/ident :org.type/client]

  {:db/id #db/id[:db.part/db]
   :db/ident :address/parent
   :db/valueType :db.type/ref
   :db/cardinality :db.cardinality/one
   :db.install/_attribute :db.part/db
  }


 {:db/id #db/id[:db.part/db]
   :db/ident :address/address
   :db/valueType :db.type/string
   :db/cardinality :db.cardinality/one
   :db.install/_attribute :db.part/db
 }
  
 {:db/id #db/id[:db.part/db]
   :db/ident :address/street
   :db/valueType :db.type/string
   :db/cardinality :db.cardinality/one
   :db.install/_attribute :db.part/db
  }

  {:db/id #db/id[:db.part/db]
   :db/ident :address/city
   :db/valueType :db.type/string
   :db/cardinality :db.cardinality/one
   :db.install/_attribute :db.part/db
  }
 
  {:db/id #db/id[:db.part/db]
   :db/ident :address/region
   :db/valueType :db.type/string
   :db/cardinality :db.cardinality/one
   :db.install/_attribute :db.part/db
  }

  {:db/id #db/id[:db.part/db]
   :db/ident :address/postal-code
   :db/valueType :db.type/string
   :db/cardinality :db.cardinality/one
   :db.install/_attribute :db.part/db
  }

  {:db/id #db/id[:db.part/db]
   :db/ident :address/latitude
   :db/valueType :db.type/double
   :db/cardinality :db.cardinality/one
   :db.install/_attribute :db.part/db
  }

  {:db/id #db/id[:db.part/db]
   :db/ident :address/longitude
   :db/valueType :db.type/double
   :db/cardinality :db.cardinality/one
   :db.install/_attribute :db.part/db
  }

  {:db/id #db/id[:db.part/db]
   :db/ident :address/provider
   :db/valueType :db.type/string
   :db/cardinality :db.cardinality/one
   :db.install/_attribute :db.part/db
  }

  {:db/id #db/id[:db.part/db]
   :db/ident :cchannel/cchannel
   :db/valueType :db.type/string
   :db/cardinality :db.cardinality/one
   :db/unique :db.unique/identity
   :db/index true
   :db.install/_attribute :db.part/db
  }

  {:db/id #db/id[:db.part/db]
   :db/ident :cchannel/type
   :db/valueType :db.type/ref
   :db/cardinality :db.cardinality/one
   :db.install/_attribute :db.part/db
  }

  [:db/add #db/id [:db.part/user] :db/ident :cchannel.type/cell]
  [:db/add #db/id [:db.part/user] :db/ident :cchannel.type/sms]
  [:db/add #db/id [:db.part/user] :db/ident :cchannel.type/phone]
  [:db/add #db/id [:db.part/user] :db/ident :cchannel.type/fax]
  [:db/add #db/id [:db.part/user] :db/ident :cchannel.type/email]
  [:db/add #db/id [:db.part/user] :db/ident :cchannel.type/unknown]
 
  {:db/id #db/id[:db.part/db]
   :db/ident :cchannel/category
   :db/valueType :db.type/ref
   :db/cardinality :db.cardinality/one
   :db.install/_attribute :db.part/db
  }

  [:db/add #db/id [:db.part/user] :db/ident :cchannel.category/home]
  [:db/add #db/id [:db.part/user] :db/ident :cchannel.category/work]
  [:db/add #db/id [:db.part/user] :db/ident :cchannel.category/cell]
  [:db/add #db/id [:db.part/user] :db/ident :cchannel.category/unknown]

  {:db/id #db/id[:db.part/db]
   :db/ident :cchannel/primary
   :db/valueType :db.type/boolean
   :db/cardinality :db.cardinality/one
   :db.install/_attribute :db.part/db
  }
  
  {:db/id #db/id[:db.part/db]
   :db/ident :cchannel/routes
   :db/valueType :db.type/ref
   :db/cardinality :db.cardinality/many
   :db.install/_attribute :db.part/db
  }

  {:db/id #db/id[:db.part/db]
   :db/ident :cchannel/callerName
   :db/valueType :db.type/string
   :db/cardinality :db.cardinality/one
   :db.install/_attribute :db.part/db
  }

  {:db/id #db/id[:db.part/db]
   :db/ident :cchannel/callerCityStateZip
   :db/valueType :db.type/string
   :db/cardinality :db.cardinality/one
   :db.install/_attribute :db.part/db
  }

  {:db/id #db/id[:db.part/db]
   :db/ident :user/user
   :db/valueType :db.type/string
   :db/cardinality :db.cardinality/one
   :db/unique :db.unique/identity
   :db/index true
   :db.install/_attribute :db.part/db
  }

  {:db/id #db/id[:db.part/db]
   :db/ident :user/password
   :db/valueType :db.type/string
   :db/cardinality :db.cardinality/one
   :db.install/_attribute :db.part/db
  }

  {:db/id #db/id[:db.part/db]
   :db/ident :user/roles
   :db/valueType :db.type/ref
   :db/cardinality :db.cardinality/many
   :db.install/_attribute :db.part/db
  }

  [:db/add #db/id [:db.part/user] :db/ident :role.person/client]
  [:db/add #db/id [:db.part/user] :db/ident :role.person/partner]
  [:db/add #db/id [:db.part/user] :db/ident :role.person/vendor]
  [:db/add #db/id [:db.part/user] :db/ident :role.person/team-member]
  [:db/add #db/id [:db.part/user] :db/ident :role.person/admin]
  [:db/add #db/id [:db.part/user] :db/ident :role.machine/api]
  [:db/add #db/id [:db.part/user] :db/ident :role.machine/phone]

  ;; comm 
  {:db/id #db/id[:db.part/db]
   :db/ident :comm/from
   :db/valueType :db.type/ref
   :db/cardinality :db.cardinality/one
   :db.install/_attribute :db.part/db
  }

  {:db/id #db/id[:db.part/db]
   :db/ident :comm/to
   :db/valueType :db.type/ref
   :db/cardinality :db.cardinality/one
   :db.install/_attribute :db.part/db
  }

  {:db/id #db/id[:db.part/db]
   :db/ident :comm/body
   :db/valueType :db.type/string
   :db/cardinality :db.cardinality/one
   :db.install/_attribute :db.part/db
  }

  {:db/id #db/id[:db.part/db]
   :db/ident :comm/type
   :db/valueType :db.type/ref
   :db/cardinality :db.cardinality/one
   :db.install/_attribute :db.part/db
  }

  [:db/add #db/id [:db.part/user] :db/ident :comm.type/email]
  [:db/add #db/id [:db.part/user] :db/ident :comm.type/call]
  [:db/add #db/id [:db.part/user] :db/ident :comm.type/sms]

  {:db/id #db/id[:db.part/db]
   :db/ident :comm/status
   :db/valueType :db.type/ref
   :db/cardinality :db.cardinality/one
   :db.install/_attribute :db.part/db
  }

  [:db/add #db/id [:db.part/user] :db/ident :comm.status/queued]
  [:db/add #db/id [:db.part/user] :db/ident :comm.status/ringing]
  [:db/add #db/id [:db.part/user] :db/ident :comm.status/in-progress]
  [:db/add #db/id [:db.part/user] :db/ident :comm.status/completed]
  [:db/add #db/id [:db.part/user] :db/ident :comm.status/busy]
  [:db/add #db/id [:db.part/user] :db/ident :comm.status/failed]
  [:db/add #db/id [:db.part/user] :db/ident :comm.status/no-answer]
  [:db/add #db/id [:db.part/user] :db/ident :comm.status/canceled]


  {:db/id #db/id[:db.part/db]
   :db/ident :comm/duration
   :db/valueType :db.type/long
   :db/cardinality :db.cardinality/one
   :db.install/_attribute :db.part/db
  }

  {:db/id #db/id[:db.part/db]
   :db/ident :comm/recordingUrl
   :db/valueType :db.type/uri
   :db/cardinality :db.cardinality/one
   :db.install/_attribute :db.part/db
  }

  {:db/id #db/id[:db.part/db]
   :db/ident :comm/recordingSid
   :db/valueType :db.type/uuid
   :db/cardinality :db.cardinality/one
   :db.install/_attribute :db.part/db
  }

  {:db/id #db/id[:db.part/db]
   :db/ident :comm/callSid
   :db/valueType :db.type/string
   :db/unique :db.unique/identity
   :db/index true
   :db/cardinality :db.cardinality/one
   :db.install/_attribute :db.part/db
  }

  {:db/id #db/id[:db.part/db]
   :db/ident :route/description
   :db/valueType :db.type/string
   :db/cardinality :db.cardinality/one
   :db.install/_attribute :db.part/db
  }

  {:db/id #db/id[:db.part/db]
   :db/ident :route/template
   :db/valueType :db.type/string
   :db/cardinality :db.cardinality/one
   :db.install/_attribute :db.part/db
  }

]
