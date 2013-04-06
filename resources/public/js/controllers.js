'use strict';

function OrgCtrl($scope) { }
function OrgsCtrl($scope) { }
function ReferralsCtrl($scope) { }
function ReferralCtrl($scope) { }

function PeopleCtrl($scope) {
  $scope.selected = "addPerson";

  $scope.setSelected = function(name) {
    console.log(name);
    console.log($scope.selected);
    $scope.selected = name;
  }

  $scope.getShow = function(name) {
    if(name === $scope.selected) {
      return "selected";
    } else {
      return "";
    }
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
