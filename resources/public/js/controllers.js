'use strict';

function OrgCtrl($scope, $routeParams, $http) {
  $scope.data = [0, 5, 3,2,4,5,2,3,1,0,0,2,1,3,2,1,2,3,5,0,0,1,3];

  $scope.id = $routeParams.id;
  $scope.url = '/orgs/' + $scope.id;
  $http.get($scope.url).success(function(data) {
        $scope.org = data;
        $scope.latitude = $scope.org.lat;
        $scope.longitude = $scope.org.lon;
        $scope.latLon =  new google.maps.LatLng($scope.latitude, $scope.longitude)
        $scope.marker = new google.maps.Marker({map: $scope.myMap, position: $scope.latLon});
        $scope.myMap.panTo($scope.latLon);
      }).error(function(data) {
        $scope.message = data.message;
      });

  $scope.message = "";
  $scope.messageType = "";
  $scope.resetMessage = function() {
    $scope.message = "";
    $scope.messageType = "";
  }
  $scope.setMessage = function(msg, msgType) {
    $scope.message = msg;
    $scope.messageType = msgType;
  }
  
  $scope.setSelected = function(name) {
    $scope.selected = name;
    $scope.mapOptions = {
      center: new google.maps.LatLng($scope.latitude, 0.000),
      zoom: 13,
      mapTypeId: google.maps.MapTypeId.ROADMAP
    }
  }

  $scope.getShow = function(name) {
    if(name === $scope.selected) {
      return "selected";
    } else {
      return "";
    }
  }

  $scope.setSelected('view');

  $scope.mapOptions = {
    center: new google.maps.LatLng(36.125349,-89.9662929),
    zoom: 10,
    mapTypeId: google.maps.MapTypeId.ROADMAP
  }
}

function OrgsCtrl($scope, $http, $location) {
  $scope.offset = "0";
  $scope.limit = "30";
  $scope.message = "";
  $scope.messageType = "";
  $scope.resetMessage = function() {
    $scope.message = "";
    $scope.messageType = "";
  }
  $scope.setMessage = function(msg, msgType) {
    $scope.message = msg;
    $scope.messageType = msgType;
  }
  
  $scope.setSelected = function(name) {
    $scope.selected = name;
    $scope.getOrgs();
  }

  $scope.getShow = function(name) {
    if(name === $scope.selected) {
      return "selected";
    } else {
      return "";
    }
  }
  
  $scope.orgTypes = [{'label': 'Insuror', 'value': 'insuror'},
                     {'label': 'Agency', 'value': 'agency'},
                     {'label': 'Vendor', 'value': 'vendor'},
                     {'label': 'Partner', 'value': 'partner'},
                     {'label': 'Client', 'value': 'client'}];

  $scope.getOrgs = function() {
    $http.get('/orgs', {params: {'limit':  $scope.limit,
                                 'offset': $scope.offset}
                       }).success(
                           function(data) {
                             $scope.orgs = data.results;
                             }).error(
                               function(data) {
                               $scope.message = data.message;
                               $scope.messageType = 'alert';
                               });
  }


  $scope.getOrgsList = function() {
    $http.get('/search/orgs-list', {params: {'q': $scope.orgsFilter + '*'}}).success(
                           function(data) {
                             $scope.orgs = data;
                             }).error(
                               function(data) {
                               $scope.message = data.message;
                               $scope.messageType = 'alert';
                               });
  }

  $scope.$watch('orgsFilter', function(newV,oldV,scope) {
     if(newV == '') {
       $scope.getOrgs();
     } else {
       $scope.getOrgsList();
     }
  });

  $scope.addOrg = function() {
    if(! $scope.acctMgr) {
      console.log("setting it");
      $scope.acctMgr = {id: ''};
    } else {
      if(! $scope.acctMgr.id) {
        $scope.acctMgr.id = '';
      }
    }

    if(! $scope.pid) {
      console.log("setting it");
      $scope.pid = {id: ''};
    } else {
      if(! $scope.pid.id) {
        $scope.pid.id = '';
      }
    }

    $http.post('/orgs', {"name": $scope.name,
                         "address": $scope.address,
                         "email": $scope.email,
                         "work": $scope.work,
                         "fax": $scope.fax,
                         "orgType": $scope.orgType,
                         "parent": $scope.pid.id,
                         "acctMgr": $scope.acctMgr.id,
                         "note": $scope.note}).success(
                      function(data) {
                        $scope.message = data.message;
                        $scope.messageType = 'success';
                        $location.path('/');
                        //$scope.resetAddOrg();
                        //$scope.setSelected('all');
                      }).error(
                        function(data) {
                          $scope.message = data.message;
                          $scope.messageType = 'alert';
                        });
  }

  $scope.resetAddOrg = function() {
    $scope.name = "";
    $scope.address = "";
    $scope.email = "";
    $scope.work = "";
    $scope.fax = "";
    $scope.orgType = "";
    $scope.pid = "";
    $scope.acctMgr = "";
    $scope.note = "";
  }

  $scope.setSelected('all');

  $scope.org = '';
  $scope.parentOrgOpts = {
    placeholder: "Parent Org",
    minimumInputLength: 2,
    quietMillis: 700,
    ajax: {
      url: "/search/orgs",
      datatype: 'json',
      data: function(term, page) {
        term = term + '*';
        return {
          q: term
        };
      },
      results: function(data, page) {
        return {results: data}; 
      }
    }
  }

  $scope.acctMgr = '';
  $scope.acctMgrOpts = {
    placeholder: "Acct Mgr",
    minimumInputLength: 2,
    quietMillis: 700,
    ajax: {
      url: "/search/acctMgr",
      datatype: 'json',
      data: function(term, page) {
        term = term + '*';
        return {
          q: term,
          personType: 'team'};
        },
      results: function(data, page) {
        return {results: data}; 
      }
    }
  }
}

function HomeCtrl($scope, $http) {
    $scope.message = "";
    $scope.messageType = "";
    $scope.resetMessage = function() {
      $scope.message = "";
      $scope.messageType = "";
    }
    $scope.setMessage = function(msg, msgType) {
      $scope.message = msg;
      $scope.messageType = msgType;
    }


   $scope.limit = 30;
   $scope.offset = 0;
   $scope.sequence = 0;
   var requestSequence = 0;
   
   $scope.getEntities = function() {
     var p = {};
     p.limit = $scope.limit;
     p.offset = $scope.offset;
     if( $scope.entitiesFilter ) {
       p.q = $scope.entitiesFilter + '*';
     }
     requestSequence += 1;
     p.sequence = requestSequence;
     $http.get('/entities', {params: p}).success(function(data){
         //This promotes both to  number for comparison. Note the '+'
         if($scope.sequence < +data.properties.sequence) {
           $scope.sequence = data.properties.sequence;
           $scope.entities = data.entities;
           $scope.properties = data.properties;
         }
       }).error(function(data) {
         $scope.message = data.message;
         $scope.messageType = 'alert';
       });
   }
   $scope.$watch('entitiesFilter', function() {
       $scope.getEntities();
     });
   $scope.iconClass = function(entityType, entitySubType) {
     console.log(entityType, entitySubType);
     switch(entityType) {
       case 'org':
         return 'icon-office';
         break;
       case 'person':
         return 'icon-user-3';
         break;
       case 'cchannel':
         switch(entitySubType) {
           case 'cell':
             return 'icon-mobile-2';
             break;
           case 'work':
             return 'icon-phone';
             break;
           case 'home':
             return 'icon-phone';
             break;
           case 'email':
             return 'icon-envelope';
             break;
         }
         break;
       case 'note':
         return 'icon-book-alt2';
         break;
       case 'referral':
         return 'icon-share';
         break;
       case 'comm':
         return 'icon-arrow-right';
         break;
       default:
         return 'icon-question-sign';
     }
   }
}

function ReferralsCtrl($scope,$http,$location) {
  $scope.erLoud = true;
  $scope.eeLoud = true;

  $scope.addReferral = function() {
    $http.post('/referrals', {"erTag": $scope.erTag,
                              "eeTag": $scope.eeTag,
                              "erQuiet": $scope.erQuiet,
                              "eeQuiet": $scope.eeQuiet,
                              "note" : $scope.note}).success(function(data) {
                                $location.path("/");
           }).error(function(data) {
                                $scope.message = data.message;
                                $scope.messageType = 'alert';

    });
  }
}

function ReferralCtrl($scope, $routeParams, $route,  $http) {
  $scope.id = $routeParams.id;
  $http.get('/referrals/' + $scope.id).success(function(data) {
      $scope.properties = data.properties;
      $scope.href = data.href;
      $scope.actions = data.actions[0];
      data.entities.forEach(function(entity) {
        console.log("entity", entity.class[0]);
        switch(entity.class[0]){
          case "ee":
            $scope.ee = entity;
            break;
          case "er":
            $scope.er = entity;
            break;
          case "eeCChan":
            $scope.eeCChan = entity;
            break;
          case "erCChan":
            console.log("set");
            $scope.erCChan = entity;
            break;
          case "owner":
            $scope.owner = entity;
            break;
          default:
            console.log(entity['class'][0]);
        };});
      }).error(function(data) {
        });

  $scope.doAction = function(m) {
    var payload = {};
    m.fields.forEach(function(field) {
        payload[field.name] = field.value;
        });

    $http({method : m.method,
       url : m.href,
       data : payload
       }).success(function(data) {
       console.log("Action response data:" , data);
       $scope.response = data;
       //;$scope.reset();
       $route.reload();
    }).error(function(data) {
        $scope.error = data.error;
    });
  }
}

function PeopleCtrl($scope, $http) {
  $scope.message = "";
  $scope.messageType = "";
  $scope.resetMessage = function() {
    $scope.message = "";
    $scope.messageType = "";
  }
  $scope.setMessage = function(msg, msgType) {
    $scope.message = msg;
    $scope.messageType = msgType;
  }

  $scope.perTypes = [{'label': 'Team Member', 'value': 'team'},
                     {'label': 'CSR', 'value': 'csr'},
                     {'label': 'Agent', 'value': 'agent'},
                     {'label': 'Producer', 'value': 'producer'},
                     {'label': 'Adjuster', 'value': 'adjuster'},
                     {'label': 'Claims', 'value': 'claims'},
                     {'label': 'Client', 'value': 'client'},
                     {'label': 'Vendor', 'value': 'vendor'}];

  $scope.setSelected = function(name) {
    $scope.selected = name;
    $scope.getPersons();
  }

  $scope.getShow = function(name) {
    if(name === $scope.selected) {
      return "selected";
    } else {
      return "";
    }
  }

  $scope.resetAddPerson = function() {
    $scope.firstName = "";
    $scope.lastName = "";
    $scope.address = "";
    $scope.cell = "";
    $scope.work = "";
    $scope.home = "";
    $scope.fax = "";
    $scope.note = "";
  }

  $scope.getPersons = function() {
    $http.get('/persons' ).success( function(data) {
        $scope.count = data.count;
        $scope.list = data.results;
        }).error(function(data) {
          $scope.setMessage(data);
          });
  }

  $scope.addPerson = function() {
    if(! $scope.org) {
      console.log("setting it");
      $scope.org = {id: ''};
    } else {
      if(! $scope.org.id) {
        $scope.org.id = '';
      }
    }

    $http.post('/persons', {"firstName": $scope.firstName,
                            "lastName": $scope.lastName,
                            "address": $scope.address,
                            "email": $scope.email,
                            "cell": $scope.cell,
                            "home": $scope.home,
                            "work": $scope.work,
                            "fax": $scope.fax,
                            "parent": $scope.org.id,
                            "personType": $scope.personType,
                            "note": $scope.note}).success(
                      function(data) {
                        $scope.message = data.message;
                        $scope.messageType = 'success';
                        $scope.resetAddPerson();
                        $scope.setSelected('all');
                      }).error(
                        function(data) {
                          $scope.message = data.message;
                          $scope.messageType = 'alert';
                        });
  }

  $scope.setSelected('all');

  $scope.parentOrg = '';

  $scope.parentOrgOpts = {
    placeholder: "Parent Org",
    minimumInputLength: 2,
    quietMillis: 700,
    ajax: {
      url: "/search/orgs",
      datatype: 'json',
      data: function(term, page) {
        term = term + '*';
        return {
          q: term
        };
      },
      results: function(data, page) {
        return {results: data}; 
      }
    }
  }
}

function PersonCtrl($scope, $routeParams, $http) {
  $scope.data = [0, 5, 3,2,4,5,2,3,1,0,0,2,1,3,2,1,2,3,5,0,0,1,3];

  $scope.id = $routeParams.id;
  $scope.url = '/persons/' + $scope.id;
  $http.get($scope.url).success(function(data) {
        $scope.person = data;
        $scope.latitude = $scope.person.lat;
        $scope.longitude = $scope.person.lon;
        $scope.latLon =  new google.maps.LatLng($scope.latitude, $scope.longitude)
        $scope.marker = new google.maps.Marker({map: $scope.myMap, position: $scope.latLon});
        $scope.myMap.panTo($scope.latLon);
      }).error(function(data) {
        $scope.message = data.message;
      });

  $scope.message = "";
  $scope.messageType = "";
  $scope.resetMessage = function() {
    $scope.message = "";
    $scope.messageType = "";
  }
  $scope.setMessage = function(msg, msgType) {
    $scope.message = msg;
    $scope.messageType = msgType;
  }
  
  $scope.setSelected = function(name) {
    $scope.selected = name;
  }

  $scope.getShow = function(name) {
    if(name === $scope.selected) {
      return "selected";
    } else {
      return "";
    }
  }

  $scope.setSelected('view');

  $scope.mapOptions = {
    center: new google.maps.LatLng(36.125349,-89.9662929),
    zoom: 10,
    mapTypeId: google.maps.MapTypeId.ROADMAP
  }
}

function CommCtrl($scope) { }
function CommsCtrl($scope) { }
function InsurorsCtrl($scope) { }
function InsurorCtrl($scope) { }
function NotesCtrl($scope, $http) {
  $scope.note = '';
  $scope.notes = [];
  $scope.offset = 0;
  $scope.limit = 10;
  $scope.count = 0;

  $scope.addNote = function() {
    $http.post('/notes', {"note": $scope.note,
                          "parent": $scope.id}).success(
                      function(data) {
                        $scope.message = data.message;
                        $scope.messageType = 'success';
                        $scope.reset();
                        $scope.getNotes();
                        $scope.note = '';
                      }).error(
                        function(data) {
                          $scope.message = data.message;
                          $scope.messageType = 'alert';
                        });
  }

  $scope.reset = function() {
    $scope.notes = [];
    $scope.offset = 0;
    $scope.limit = 10;
    $scope.count = 0;
  }

  $scope.getNotes = function() {
    $http.get('/notes', {params: {'parent': $scope.id,
                                  'offset': $scope.offset,
                                   'limit': $scope.limit}}).success(
                           function(data) {
                             $scope.notes = data.results;
                             $scope.offset = data.offset;
                             $scope.limit = data.limit;
                             $scope.count = data.count;
                             }).error(
                               function(data) {
                               $scope.message = data.message;
                               $scope.messageType = 'alert';
                               });
  }

  $scope.more = function() {
    // TODO verify '+' needed
    if(+$scope.count > $scope.offset) {
      return true;
    } else {
      return false;
    }
  }

  $scope.appendMore = function() {
    var rOffset = $scope.offset + $scope.limit;
    //TODO verify '+' needed
    if(+$scope.count > $scope.offset) {
      $http.get('/notes', {params: {'parent': $scope.id,
                                    'offset': rOffset, 
                                    'limit': $scope.limit}}).success(
                                      function(data) {
                                        $scope.notes = $scope.notes.concat(data.results);
                                        $scope.offset = data.offset;
                                        }).error(
                                          function(data) {
                                            $scope.message = data.message;
                                            $scope.messageType = 'alert';
                                            });
    }
  }

  $scope.getNotes();
}

function NoteCtrl($scope) { }
function AgenciesCtrl($scope) { }
function AgencyCtrl($scope) { }
function EntityCtrl($scope) { }
