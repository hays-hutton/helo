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
  
  $scope.selected = "addPerson";
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
}

function PersonCtrl($scope) { }
function CommCtrl($scope) { }
function CommsCtrl($scope) { }
function InsurorsCtrl($scope) { }
function InsurorCtrl($scope) { }
function NotesCtrl($scope) { }
function NoteCtrl($scope) { }
function AgenciesCtrl($scope) { }
function AgencyCtrl($scope) { }
function EntityCtrl($scope) { }
