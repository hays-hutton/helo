[
  {:db/id #db/id[:db.part/user -1]
   :name "TEAM1 Auto Body & Glass"
   :type :type/org
   :created #inst "2013-02-05T19:06:00.007-00:00"
   :updated #inst "2013-02-05T19:06:01.007-00:00"
  }

  {:db/id #db/id[:db.part/user -2]
   :name "TEAM1 -- Missouri"
   :org/parent #db/id[:db.part/user -1]
   :type :type/org
   :created #inst "2013-02-05T19:06:01.006-00:00"
   :updated #inst "2013-02-05T19:06:01.006-00:00"
  }

  {:db/id #db/id[:db.part/user -3]
   :name "Clayton"
   :org/parent #db/id[:db.part/user -2]
   :type :type/org
   :created #inst "2013-02-05T19:06:01.005-00:00"
   :updated #inst "2013-02-05T19:06:01.005-00:00"
  }

  {:db/id #db/id[:db.part/user -4]
   :name "Chesterfield"
   :org/parent #db/id[:db.part/user -2]
   :type :type/org
   :created #inst "2013-02-05T19:06:01.004-00:00"
   :updated #inst "2013-02-05T19:06:01.004-00:00"
  }

  {:db/id #db/id[:db.part/user -5]
   :name "Shrewsbury"
   :org/parent #db/id[:db.part/user -2]
   :type :type/org
   :created #inst "2013-02-05T19:06:01.003-00:00"
   :updated #inst "2013-02-05T19:06:01.003-00:00"
  }

  {:db/id #db/id[:db.part/user -6]
   :name "Florissant"
   :org/parent #db/id[:db.part/user -2]
   :type :type/org
   :created #inst "2013-02-05T19:06:01.002-00:00"
   :updated #inst "2013-02-05T19:06:01.002-00:00"
  }

  {:db/id #db/id[:db.part/user -7]
   :name "TEAM1 Glass"
   :org/parent #db/id[:db.part/user -2]
   :type :type/org
   :created #inst "2013-02-05T19:06:01.001-00:00"
   :updated #inst "2013-02-05T19:06:01.001-00:00"
  }

  {:db/id #db/id[:db.part/user -8]
   :name "Hutton, Hays"
   :person/first-name "Hays"
   :person/last-name "Hutton"
   :person/org #db/id[:db.part/user -1]
   :person/type :person.type/team-member
   :person/cchannels [#db/id[:db.part/user -12] #db/id[:db.part/user -13] #db/id[:db.part/user -14]   #db/id[:db.part/user -23]]
   :person/roles [:role.person/admin]
   :type :type/person
   :created #inst "2013-02-05T19:06:01.000-00:00"
   :updated #inst "2013-02-05T19:06:01.000-00:00"
  }

  {:db/id #db/id[:db.part/user -9]
   :name "Blankenship, Faron"
   :person/first-name "Faron"
   :person/last-name "Blankenship"
   :person/org #db/id[:db.part/user -2]
   :person/type :person.type/team-member
   :person/cchannels [#db/id[:db.part/user -15] #db/id[:db.part/user -16] #db/id[:db.part/user -24]  ]
   :person/roles [:role.person/team-member]
   :type :type/person
   :created #inst "2013-02-05T19:06:01.479-00:00"
   :updated #inst "2013-02-05T19:06:01.479-00:00"
  }

  {:db/id #db/id[:db.part/user -10]
   :name "Jones, Shelly"
   :person/first-name "Shelly"
   :person/last-name "Jones"
   :person/org #db/id[:db.part/user -2]
   :person/type :person.type/team-member
   :person/cchannels [#db/id[:db.part/user -17] #db/id[:db.part/user -18]]
   :person/roles [:role.person/team-member]
   :type :type/person
   :created #inst "2013-02-24T19:06:01.479-00:00"
   :updated #inst "2013-02-24T19:06:01.479-00:00"
  }

  {:db/id #db/id[:db.part/user -11]
   :name "Hemann, Julie"
   :person/first-name "Julie"
   :person/last-name "Hemann"
   :person/org #db/id[:db.part/user -2]
   :person/type :person.type/team-member
   :person/cchannels [#db/id[:db.part/user -19] #db/id[:db.part/user -20]]
   :person/roles [ :role.person/team-member]
   :type :type/person
   :created #inst "2013-02-24T19:06:02.479-00:00"
   :updated #inst "2013-02-24T19:06:02.479-00:00"
  }

  {:db/id #db/id[:db.part/user -12]
   :name "tel:+19015505058"
   :cchannel/cchannel "tel:+19015505058"
   :cchannel/type :cchannel.type/cell
   :cchannel/primary true
   :type :type/cchannel
   :created #inst "2013-03-10T18:06:02.000-00:00"
   :updated #inst "2013-03-10T18:06:02.000-00:00"
  }

  {:db/id #db/id[:db.part/user -23]
   :name "sms:+19015505058"
   :cchannel/cchannel "sms:+19015505058"
   :cchannel/type :cchannel.type/sms
   :cchannel/primary true
   :type :type/cchannel
   :created #inst "2013-03-10T18:06:03.000-00:00"
   :updated #inst "2013-03-10T18:06:04.000-00:00"
  }

  {:db/id #db/id[:db.part/user -13]
   :name "email:hays.hutton@team1.com"
   :cchannel/cchannel "email:hays.hutton@team1.com"
   :cchannel/type :cchannel.type/email
   :cchannel/primary false
   :type :type/cchannel
   :created #inst "2013-03-10T18:06:03.000-00:00"
   :updated #inst "2013-03-10T18:06:03.000-00:00"
  }

  {:db/id #db/id[:db.part/user -14]
   :name "tel:+19014356122"
   :cchannel/cchannel "tel:+19014356122"
   :cchannel/type :cchannel.type/home
   :cchannel/primary false
   :type :type/cchannel
   :created #inst "2013-03-10T18:06:03.000-00:00"
   :updated #inst "2013-03-10T18:06:03.000-00:00"
  }
 
  {:db/id #db/id[:db.part/user -15]
   :name "tel:+13145749181"
   :cchannel/cchannel "tel:+13145749181"
   :cchannel/type :cchannel.type/cell
   :cchannel/primary true
   :type :type/cchannel
   :created #inst "2013-03-10T18:06:02.000-00:00"
   :updated #inst "2013-03-10T18:06:02.000-00:00"
  }

  {:db/id #db/id[:db.part/user -24]
   :name "sms:+13145749181"
   :cchannel/cchannel "sms:+13145749181"
   :cchannel/type :cchannel.type/sms
   :type :type/cchannel
   :created #inst "2013-03-10T18:06:02.000-00:00"
   :updated #inst "2013-03-10T18:06:02.000-00:00"
  }

  {:db/id #db/id[:db.part/user -16]
   :name "email:faron.blankenship@team1.com"
   :cchannel/cchannel "eamil:faron.blankenship@team1.com"
   :cchannel/type :cchannel.type/email
   :cchannel/primary false
   :type :type/cchannel
   :created #inst "2013-03-10T18:06:03.000-00:00"
   :updated #inst "2013-03-10T18:06:03.000-00:00"
  }
 
  {:db/id #db/id[:db.part/user -17]
   :name "tel:+13149206710"
   :cchannel/cchannel "tel:+13149206710"
   :cchannel/type :cchannel.type/cell
   :cchannel/primary true
   :type :type/cchannel
   :created #inst "2013-03-10T18:06:02.000-00:00"
   :updated #inst "2013-03-10T18:06:02.000-00:00"
  }

  {:db/id #db/id[:db.part/user -18]
   :name "email:shelly.jones@team1.com"
   :cchannel/cchannel "email:shelly.jones@team1.com"
   :cchannel/type :cchannel.type/email
   :type :type/cchannel
   :created #inst "2013-03-10T18:06:03.000-00:00"
   :updated #inst "2013-03-10T18:06:03.000-00:00"
  }
  
  {:db/id #db/id[:db.part/user -19]
   :name "tel:+13149208422"
   :cchannel/cchannel "tel:+13149208422"
   :cchannel/type :cchannel.type/cell
   :type :type/cchannel
   :created #inst "2013-03-10T18:06:02.000-00:00"
   :updated #inst "2013-03-10T18:06:02.000-00:00"
  }

  {:db/id #db/id[:db.part/user -20]
   :name "email:julie.hemann@team1.com"
   :cchannel/cchannel "email:julie.hemann@team1.com"
   :cchannel/type :cchannel.type/email
   :type :type/cchannel
   :created #inst "2013-03-10T18:06:03.000-00:00"
   :updated #inst "2013-03-10T18:06:03.000-00:00"
  }

  {:db/id #db/id[:db.part/user -21]
   :user/user "api"
   :user/password "ticonderogaHB2"
   :user/roles [:role.machine/api]
  }

  {:db/id #db/id[:db.part/user -22]
   :user/user "phone"
   :user/password "ClaudeElwood1916"
   :user/roles [:role.machine/phone]
  }
  ;system cchannels
  {:db/id #db/id[:db.part/user -25]
   :name "tel:+13143154668"
   :cchannel/cchannel "tel:+13143154668"
   :cchannel/type :cchannel.type/work
   :type :type/cchannel
   :created #inst "2013-03-10T18:06:00.000-00:00"
   :updated #inst "2013-03-10T18:06:00.000-00:00"
  }

  {:db/id #db/id[:db.part/user -26]
   :name "sms:+13143154668"
   :cchannel/cchannel "sms:+13143154668"
   :cchannel/type :cchannel.type/sms
   :cchannel/primary true
   :type :type/cchannel
   :created #inst "2013-03-10T18:06:00.100-00:00"
   :updated #inst "2013-03-10T18:06:00.100-00:00"
  }

  {:db/id #db/id[:db.part/user -27]
   :name "tel:+19012354363"
   :cchannel/cchannel "tel:+19012354363"
   :cchannel/type :cchannel.type/work
   :cchannel/primary true
   :type :type/cchannel
   :created #inst "2013-03-10T18:06:00.200-00:00"
   :updated #inst "2013-03-10T18:06:00.200-00:00"
  }

  {:db/id #db/id[:db.part/user -28]
   :name "sms:+19012354363"
   :cchannel/cchannel "sms:+19012354363"
   :cchannel/type :cchannel.type/sms
   :cchannel/primary true
   :type :type/cchannel
   :created #inst "2013-03-10T18:06:00.300-00:00"
   :updated #inst "2013-03-10T18:06:00.300-00:00"
  }

  {:db/id #db/id[:db.part/user -29]
   :name "Anonymous"
   :person/first-name "Anonymous"
   :person/last-name "User"
   :person/tkey #uuid "5159acae-b10a-4c60-bf16-b8957180afac"
   :type :type/person
   :created #inst "2013-02-05T19:07:02.000-00:00"
   :updated #inst "2013-02-05T19:07:02.000-00:00"
  }
]
