[
  {:db/id #db/id[:db.part/user -1]
   :name "TEAM1 Auto Body & Glass"
   :uuid #uuid "5100513c-3e89-47d3-bc84-96d13023c505"
   :url "https://api.team1.com/orgs/5100513c-3e89-47d3-bc84-96d13023c505"
   :type :type/org
   :created #inst "2013-02-05T19:06:00.007-00:00"
   :updated #inst "2013-02-05T19:06:01.007-00:00"
  }

  {:db/id #db/id[:db.part/user -2]
   :name "TEAM1 -- Missouri"
   :org/parent #db/id[:db.part/user -1]
   :uuid #uuid "5100527c-c5cf-403b-825c-1b949d3f5ff6"  
   :url "https://api.team1.com/orgs/5100527c-c5cf-403b-825c-1b949d3f5ff6"  
   :type :type/org
   :created #inst "2013-02-05T19:06:01.006-00:00"
   :updated #inst "2013-02-05T19:06:01.006-00:00"
  }

  {:db/id #db/id[:db.part/user -3]
   :name "Clayton"
   :org/parent #db/id[:db.part/user -2]
   :uuid #uuid "510052ad-0ce4-42b5-846e-2031aa2c7ea2"
   :url "https://api.team1.com/orgs/510052ad-0ce4-42b5-846e-2031aa2c7ea2"
   :type :type/org
   :created #inst "2013-02-05T19:06:01.005-00:00"
   :updated #inst "2013-02-05T19:06:01.005-00:00"
  }

  {:db/id #db/id[:db.part/user -4]
   :name "Chesterfield"
   :org/parent #db/id[:db.part/user -2]
   :uuid #uuid "510052bd-d80d-42fa-ad4c-88f7c84ea11d"
   :url "https://api.team1.com/orgs/510052bd-d80d-42fa-ad4c-88f7c84ea11d"
   :type :type/org
   :created #inst "2013-02-05T19:06:01.004-00:00"
   :updated #inst "2013-02-05T19:06:01.004-00:00"
  }

  {:db/id #db/id[:db.part/user -5]
   :name "Shrewsbury"
   :org/parent #db/id[:db.part/user -2]
   :uuid #uuid "510052cc-b28d-4a0d-bc87-f93e252d899f"
   :url "https://api.team1.com/orgs/510052cc-b28d-4a0d-bc87-f93e252d899f"
   :type :type/org
   :created #inst "2013-02-05T19:06:01.003-00:00"
   :updated #inst "2013-02-05T19:06:01.003-00:00"
  }

  {:db/id #db/id[:db.part/user -6]
   :name "Florissant"
   :org/parent #db/id[:db.part/user -2]
   :uuid #uuid "510052dd-bce1-4127-a28f-eab075c071cc"
   :url "https://api.team1.com/orgs/510052dd-bce1-4127-a28f-eab075c071cc"
   :type :type/org
   :created #inst "2013-02-05T19:06:01.002-00:00"
   :updated #inst "2013-02-05T19:06:01.002-00:00"
  }

  {:db/id #db/id[:db.part/user -7]
   :name "TEAM1 Glass"
   :org/parent #db/id[:db.part/user -2]
   :uuid #uuid "510052ec-32d1-459d-9e9e-87fb06c2c6a7"
   :url "https://api.team1.com/orgs/510052ec-32d1-459d-9e9e-87fb06c2c6a7"
   :type :type/org
   :created #inst "2013-02-05T19:06:01.001-00:00"
   :updated #inst "2013-02-05T19:06:01.001-00:00"
  }

  {:db/id #db/id[:db.part/user -8]
   :name "hays.hutton"
   :person/first-name "Hays"
   :person/last-name "Hutton"
   :uuid #uuid "51005301-362e-49b9-90f0-39065b71e672"
   :url  "https://api.team1.com/persons/51005301-362e-49b9-90f0-39065b71e672"
   :person/org #db/id[:db.part/user -1]
   :person/type :person.type/team-member
   :person/cchannels [#db/id[:db.part/user -12] #db/id[:db.part/user -13] #db/id[:db.part/user -14]   #db/id[:db.part/user -23]]
   :person/roles [:role.person/admin]
   :type :type/person
   :created #inst "2013-02-05T19:06:01.000-00:00"
   :updated #inst "2013-02-05T19:06:01.000-00:00"
  }

  {:db/id #db/id[:db.part/user -9]
   :name "faron.blankenship"
   :person/first-name "Faron"
   :person/last-name "Blankenship"
   :uuid #uuid "51005376-86b4-414a-a6fe-ef6ddd182cee"
   :url "https://api.team1.com/persons/51005376-86b4-414a-a6fe-ef6ddd182cee"
   :person/org #db/id[:db.part/user -2]
   :person/type :person.type/team-member
   :person/cchannels [#db/id[:db.part/user -15] #db/id[:db.part/user -16] #db/id[:db.part/user -24]  ]
   :person/roles [:role.person/team-member]
   :type :type/person
   :created #inst "2013-02-05T19:06:01.479-00:00"
   :updated #inst "2013-02-05T19:06:01.479-00:00"
  }

  {:db/id #db/id[:db.part/user -10]
   :name "shelly.jones"
   :person/first-name "Shelly"
   :person/last-name "Jones"
   :uuid #uuid "512a9335-a957-42d7-a0b7-cf44a4c6533c"
   :url "https://api.team1.com/persons/512a9335-a957-42d7-a0b7-cf44a4c6533c"
   :person/org #db/id[:db.part/user -2]
   :person/type :person.type/team-member
   :person/cchannels [#db/id[:db.part/user -17] #db/id[:db.part/user -18]]
   :person/roles [:role.person/team-member]
   :type :type/person
   :created #inst "2013-02-24T19:06:01.479-00:00"
   :updated #inst "2013-02-24T19:06:01.479-00:00"
  }

  {:db/id #db/id[:db.part/user -11]
   :name "julie.hemann"
   :person/first-name "Julie"
   :person/last-name "Hemann"
   :uuid #uuid "512a95f8-3514-4ee9-a5c4-7f7518d3d922"
   :url "https://api.team1.com/persons/512a95f8-3514-4ee9-a5c4-7f7518d3d922"
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
   :cchannel/category :cchannel.category/cell
   :cchannel/primary true
   :uuid #uuid "513cf1a8-755e-4104-ae26-1574c547d00e"
   :url  "https://api.team1.com/cchannels/513cf1a8-755e-4104-ae26-1574c547d00e"
   :type :type/cchannel
   :created #inst "2013-03-10T18:06:02.000-00:00"
   :updated #inst "2013-03-10T18:06:02.000-00:00"
  }

  {:db/id #db/id[:db.part/user -23]
   :name "sms:+19015505058"
   :cchannel/cchannel "sms:+19015505058"
   :cchannel/type :cchannel.type/sms
   :cchannel/category :cchannel.category/cell
   :cchannel/primary true
   :uuid #uuid "5140c80c-fbec-45a0-851e-d44bae34cba7"
   :url  "https://api.team1.com/cchannels/5140c80c-fbec-45a0-851e-d44bae34cba7"
   :type :type/cchannel
   :created #inst "2013-03-10T18:06:03.000-00:00"
   :updated #inst "2013-03-10T18:06:04.000-00:00"
  }

  {:db/id #db/id[:db.part/user -13]
   :name "email:hays.hutton@team1.com"
   :cchannel/cchannel "email:hays.hutton@team1.com"
   :cchannel/type :cchannel.type/email
   :cchannel/category :cchannel.category/work
   :cchannel/primary false
   :uuid #uuid "513cf340-61b2-4527-9fd4-ea3313ff2b7d"
   :url  "https://api.team1.com/cchannels/513cf340-61b2-4527-9fd4-ea3313ff2b7d"
   :type :type/cchannel
   :created #inst "2013-03-10T18:06:03.000-00:00"
   :updated #inst "2013-03-10T18:06:03.000-00:00"
  }

  {:db/id #db/id[:db.part/user -14]
   :name "tel:+19014356122"
   :cchannel/cchannel "tel:+19014356122"
   :cchannel/type :cchannel.type/phone
   :cchannel/category :cchannel.category/home
   :cchannel/primary false
   :uuid #uuid "513cf3b1-3ffe-4a04-b986-0e6e2d979f31"
   :url  "https://api.team1.com/cchannels/513cf3b1-3ffe-4a04-b986-0e6e2d979f31"
   :type :type/cchannel
   :created #inst "2013-03-10T18:06:03.000-00:00"
   :updated #inst "2013-03-10T18:06:03.000-00:00"
  }
 
  {:db/id #db/id[:db.part/user -15]
   :name "tel:+13145749181"
   :cchannel/cchannel "tel:+13145749181"
   :cchannel/type :cchannel.type/cell
   :cchannel/category :cchannel.category/cell
   :cchannel/primary true
   :uuid #uuid "513cf473-9014-4195-8164-c3074b9c7671"
   :url  "https://api.team1.com/cchannels/513cf473-9014-4195-8164-c3074b9c7671"
   :type :type/cchannel
   :created #inst "2013-03-10T18:06:02.000-00:00"
   :updated #inst "2013-03-10T18:06:02.000-00:00"
  }

  {:db/id #db/id[:db.part/user -24]
   :name "sms:+13145749181"
   :cchannel/cchannel "sms:+13145749181"
   :cchannel/type :cchannel.type/sms
   :cchannel/category :cchannel.category/cell
   :cchannel/primary true
   :uuid #uuid "5140c8c9-a3bd-4d8f-835e-68c1bad64f7d"
   :url  "https://api.team1.com/cchannels/5140c8c9-a3bd-4d8f-835e-68c1bad64f7d"
   :type :type/cchannel
   :created #inst "2013-03-10T18:06:02.000-00:00"
   :updated #inst "2013-03-10T18:06:02.000-00:00"
  }

  {:db/id #db/id[:db.part/user -16]
   :name "email:faron.blankenship@team1.com"
   :cchannel/cchannel "eamil:faron.blankenship@team1.com"
   :cchannel/type :cchannel.type/email
   :cchannel/category :cchannel.category/work
   :cchannel/primary false
   :uuid #uuid "513cf4d0-496d-4f9d-9d78-9ab5599fd95b"
   :url  "https://api.team1.com/cchannels/513cf4d0-496d-4f9d-9d78-9ab5599fd95b"
   :type :type/cchannel
   :created #inst "2013-03-10T18:06:03.000-00:00"
   :updated #inst "2013-03-10T18:06:03.000-00:00"
  }
 
  {:db/id #db/id[:db.part/user -17]
   :name "tel:+13149206710"
   :cchannel/cchannel "tel:+13149206710"
   :cchannel/type :cchannel.type/cell
   :cchannel/category :cchannel.category/cell
   :cchannel/primary true
   :uuid #uuid "513cf5d2-1bca-431d-a9d0-24f1c611d297"
   :url  "https://api.team1.com/cchannels/513cf5d2-1bca-431d-a9d0-24f1c611d297"
   :type :type/cchannel
   :created #inst "2013-03-10T18:06:02.000-00:00"
   :updated #inst "2013-03-10T18:06:02.000-00:00"
  }

  {:db/id #db/id[:db.part/user -18]
   :name "email:shelly.jones@team1.com"
   :cchannel/cchannel "email:shelly.jones@team1.com"
   :cchannel/type :cchannel.type/email
   :cchannel/category :cchannel.category/work
   :cchannel/primary false
   :uuid #uuid "513cf623-5fc5-48b6-a46c-e6edb8c53ad1"
   :url  "https://api.team1.com/cchannels/513cf623-5fc5-48b6-a46c-e6edb8c53ad1"
   :type :type/cchannel
   :created #inst "2013-03-10T18:06:03.000-00:00"
   :updated #inst "2013-03-10T18:06:03.000-00:00"
  }
  
  {:db/id #db/id[:db.part/user -19]
   :name "tel:+13149208422"
   :cchannel/cchannel "tel:+13149208422"
   :cchannel/type :cchannel.type/cell
   :cchannel/category :cchannel.category/cell
   :cchannel/primary true
   :uuid #uuid "513cf6ba-19b5-4c7e-ade9-8f8beca78b6e"
   :url  "https://api.team1.com/cchannels/513cf6ba-19b5-4c7e-ade9-8f8beca78b6e"
   :type :type/cchannel
   :created #inst "2013-03-10T18:06:02.000-00:00"
   :updated #inst "2013-03-10T18:06:02.000-00:00"
  }

  {:db/id #db/id[:db.part/user -20]
   :name "email:julie.hemann@team1.com"
   :cchannel/cchannel "email:julie.hemann@team1.com"
   :cchannel/type :cchannel.type/email
   :cchannel/category :cchannel.category/work
   :cchannel/primary false
   :uuid #uuid "513cf704-7a20-4e58-9e5f-d1e77a690f6f"
   :url  "https://api.team1.com/cchannels/513cf704-7a20-4e58-9e5f-d1e77a690f6f"
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
   :cchannel/type :cchannel.type/phone
   :cchannel/category :cchannel.category/work
   :cchannel/primary true
   :uuid #uuid "5140d31e-c798-4651-aad7-90474073d9f9"
   :url  "https://api.team1.com/cchannels/5140d31e-c798-4651-aad7-90474073d9f9"
   :type :type/cchannel
   :created #inst "2013-03-10T18:06:00.000-00:00"
   :updated #inst "2013-03-10T18:06:00.000-00:00"
  }

  {:db/id #db/id[:db.part/user -26]
   :name "sms:+13143154668"
   :cchannel/cchannel "sms:+13143154668"
   :cchannel/type :cchannel.type/sms
   :cchannel/category :cchannel.category/work
   :cchannel/primary true
   :uuid #uuid "5140d381-3270-45e9-a31f-b168d007251a"
   :url  "https://api.team1.com/cchannels/5140d381-3270-45e9-a31f-b168d007251a"
   :type :type/cchannel
   :created #inst "2013-03-10T18:06:00.100-00:00"
   :updated #inst "2013-03-10T18:06:00.100-00:00"
  }

  {:db/id #db/id[:db.part/user -27]
   :name "tel:+19012354363"
   :cchannel/cchannel "tel:+19012354363"
   :cchannel/type :cchannel.type/phone
   :cchannel/category :cchannel.category/work
   :cchannel/primary true
   :uuid #uuid "5140d3e3-6824-4f3f-b6ec-b6a397ea0dc6"
   :url  "https://api.team1.com/cchannels/5140d3e3-6824-4f3f-b6ec-b6a397ea0dc6"
   :type :type/cchannel
   :created #inst "2013-03-10T18:06:00.200-00:00"
   :updated #inst "2013-03-10T18:06:00.200-00:00"
  }

  {:db/id #db/id[:db.part/user -28]
   :name "sms:+19012354363"
   :cchannel/cchannel "sms:+19012354363"
   :cchannel/type :cchannel.type/sms
   :cchannel/category :cchannel.category/work
   :cchannel/primary true
   :uuid #uuid "5140d420-e4ed-45ff-abe2-49f5e0140643"
   :url  "https://api.team1.com/cchannels/5140d420-e4ed-45ff-abe2-49f5e0140643"
   :type :type/cchannel
   :created #inst "2013-03-10T18:06:00.300-00:00"
   :updated #inst "2013-03-10T18:06:00.300-00:00"
  }

  {:db/id #db/id[:db.part/user -29]
   :name "Anonymous"
   :person/first-name "Anonymous"
   :person/last-name "User"
   :person/tkey #uuid "5159acae-b10a-4c60-bf16-b8957180afac"
   :uuid #uuid "5159a713-2d08-46a9-bf52-5db732123f46"
   :url  "https://api.team1.com/persons/5159a713-2d08-46a9-bf52-5db732123f46"
   :type :type/person
   :created #inst "2013-02-05T19:07:02.000-00:00"
   :updated #inst "2013-02-05T19:07:02.000-00:00"
  }
]
