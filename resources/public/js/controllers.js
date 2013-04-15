'use strict';

function OrgCtrl($scope) { }
function OrgsCtrl($scope) { }
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
        $scope.list = data.list;
        }).error(function(data) {
          $scope.setMessage(data);
          }); }

  $scope.addPerson = function() {
    $http.post('/persons', {"person/first-name": $scope.firstName,
                            "person/last-name": $scope.lastName,
                            "address/address": $scope.address,
                            "person/email": $scope.email,
                            "person/cell": $scope.cell,
                            "person/home": $scope.home,
                            "person/work": $scope.work,
                            "person/fax": $scope.fax,
                            "note/note": $scope.note}).success(
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


}

function PersonCtrl($scope, $routeParams, $http) {

  $scope.latitude = 34.555;
  $scope.longitude = -35.333;

  $scope.id = $routeParams.id;
  $scope.url = '/persons/' + $scope.id;
  $http.get($scope.url).success(function(data) {
        $scope.person = data;
        $scope.latitude = $scope.person['address/latitude'];
        $scope.longitude = $scope.person['address/longitude'];
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
    console.log($scope.latitude);
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

  $scope.getShowNote = function() {
    if($scope.showNote) {
      return "selected";
    } else {
      return "";
    }
  }

  $scope.toggleNote = function() {
    if($scope.showNote) {
      $scope.showNote = false;
    } else {
      $scope.showNote = true;
    }
  }

  $scope.setSelected('view');
  $scope.mapOptions = {
    center: new google.maps.LatLng(35.125349,-89.9662929),
    zoom: 13,
    mapTypeId: google.maps.MapTypeId.ROADMAP
  }

}

function CommCtrl($scope) { }
function CommsCtrl($scope) { }
function InsurorsCtrl($scope) { }
function InsurorCtrl($scope) { }
function NotesCtrl($scope) { }
function NoteCtrl($scope) { }
function AgenciesCtrl($scope) { }
function AgencyCtrl($scope) { }
function EntityCtrl($scope) { }
