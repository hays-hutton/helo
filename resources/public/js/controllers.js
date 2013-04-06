'use strict';

function OrgCtrl($scope) { }
function OrgsCtrl($scope) { }
function ReferralsCtrl($scope) { }
function ReferralCtrl($scope) { }
function PeopleCtrl($scope) { }
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

function PersonCtrl($scope, $http, $location) {
    $scope.class1 = '';
    $scope.class2 = '';
    $scope.actions = [];
    $scope.selected = '';
    $scope.name = '';
    $scope.updated = '';
    $scope.title1 = function() {
        return $scope.class1;
    }
    $scope.title2 = function() {
      return $scope.siren.properties.name;
    }
    $scope.classCol1 = function() {
      if($scope.class2 == 'Entity') {
        return 'three fifths';
      } else {
        return 'one fifth';
      }
    }
    $scope.classCol2 = function() {
      if($scope.class2 == 'Collection') {
        return 'four fifths';
      } else {
        return 'two fifths';
      }
    }
    $scope.toggleForm = function(name) {
        $scope.selected = name;
    }
    $scope.reset = function() {
        $scope.selected = '';
    }
    $scope.getShow = function(name) {
        if( name === $scope.selected ) {
            return "selected";
        } else {
            return "";
        }
    }
    $http.get($location.path()).success(
            function(data) {
                $scope.class1 = data.class[0];
                if(data.class[1]) {
                    $scope.class2 = data.class[1];
                };
                $scope.siren = data;
                $scope.actions = $scope.siren.actions;
                $scope.name = data.properties.name;
                $scope.updated = data.properties.updated;
                $scope.created = data.properties.created;
            }).error(
            function(data) {
                $scope.referrals = data.error.message});
}

function lwFormCtrl($scope, $http, $route) {
  $scope.dto = {};
  $scope.content = "{revert: 'invalid'}";
  $scope.doAction = function(m) {
    $http({method : m.method,
       url : m.href,
       data : $scope.dto
       }).success(function(data) {
       console.log("Action response data:" , data);
       $scope.response = data;
       $scope.reset();
       $route.reload();
    }).error(function(data) {
        $scope.error = data.error;
    });
  }
};

function lwSearchCtrl($scope, $http) {
    $scope.doSearch = function(searchTerm) {
        var params = { "name" : $scope.query};
        $http({"method" : "GET",
               "url" : "/entities",
               "params" : params}).success(function(data) {
                console.log("Search response data:" , data);
                $scope.siren = data;
            }).error(function(data) {
                $scope.error = data.error.message});
    }
    $scope.doSearch();
}
