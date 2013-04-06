'use strict';

/* Filters */

angular.module('helo.filters', []).
  filter('relative', function() { 
    return function(text) {
      return String(text).replace(/^(?:\/\/|[^\/]+)*\//, "#/");
    }
  }).filter('typer', function() {
      return function(text) {
          return String(text).replace(/type\//,"");
      }
  });
