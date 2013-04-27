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

function OrgsCtrl($scope, $http) {
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
    $http.get('/orgs', {params: {'offset': $scope.offset,
                                 'limit': $scope.limit}}).success(
                           function(data) {
                             $scope.orgs = data.results;
                             $scope.offset = data.offset;
                             $scope.limit = data.limit;
                             $scope.count = data.count;
                             }).error(
                               function(data) {
                               $scope.message = data.message;
                               $scope.messageType = 'alert';
                               });
  }

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
                        $scope.resetAddOrg();
                        $scope.setSelected('all');
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

function ReferralsCtrl($scope) { }
function ReferralCtrl($scope) { }

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
          }); }

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
                            "org": $scope.org.id,
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

function SearchCtrl($scope, $routeParams, $http) {
  $scope.searchModel = '';
  $scope.sel = {
    placeholder: "Parent Org",
    minimumInputLength: 2,
    quietMillis: 700,
    ajax: {
      url: "/orgs",
      datatype: 'json',
      data: function(term, page) {
        term = term + '*';
        return {
          search: term,
          limit: 20,
          offset: 0,
          format: 'search'};
      },
      results: function(data, page) {
        console.log(data);
        var temp = [];
        data.results.forEach(function(val) {
            temp.push({id: val.id, text: val.name});
            });
        console.log(temp);
        return {results: temp}; 
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
    if($scope.count > $scope.offset) {
      return true;
    } else {
      return false;
    }
  }

  $scope.appendMore = function() {
    var rOffset = $scope.offset + $scope.limit;
    if($scope.count > $scope.offset) {
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
