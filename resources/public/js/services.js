'use strict';

/* Services */


// Demonstrate how to register services
// In this case it is a simple value service.
angular.module('helo.services', []).
  value('version', '0.1').service('Search', function($http) {
    this.getEnts = function() {
      return $http.get('/orgs');
    }
  });
