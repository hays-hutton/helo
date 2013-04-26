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
  }).filter('initcap', function() {
    return function(text) {
      return text.substring(0,1).toUpperCase() + text.substring(1).toLowerCase();
    }
  });
