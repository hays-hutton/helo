'use strict';

/* Directives */

angular.module('helo.directives', [])
.directive('heloTypeahead', ['$parse', function($parse) {
  'use strict';

  return {
    restrict: 'A',
    require: '?ngModel',
    link: function postLink(scope, element, attrs, controller) {

      var getter = $parse(attrs.heloTypeahead),
          setter = getter.assign,
          value = getter(scope);

      console.log("Value:", value);

      // Watch bsTypeahead for changes
      scope.$watch(attrs.heloTypeahead, function(newValue, oldValue) {
        if(newValue !== oldValue) {
          console.log(newValue);
          value = newValue;
        }
      });

      var typeahead = element.typeahead(value);
      //var typeahead = element.typeahead({name: "hello", local: ["hays", "harlan","laura"]});

      element.bind('typeahead:autocompleted',function(ev, args) {
          console.log("Event:", ev);
          console.log("Args:",args);
          });
      
      element.bind('typeahead:selected',function(ev, args) {
          console.log("Event:", ev);
          console.log("Args:",args);
          });




    }
  };

}]).directive('lwRadio', function($parse) {
    return {
      //probably should allow transclusion if going to have css in here!
      template: '<div><input type="radio"><span class="padded"></div>',
      replace: true,
      restrict: 'A',
      link: function(scope, iElem, iAttrs,ctrl) {
        var iName = iAttrs.name;
        var iNameFn = $parse(iName );
        var iName2 = iNameFn(scope);
        iElem.find('input').attr('name',  iName2);
        var iValue = iAttrs.value;
        var iValueFn = $parse(iValue);
        var iValue2 = iValueFn(scope);
        var iLabel = iAttrs.label;
        var iLabelFn = $parse(iLabel);
        var iLabel2 = iLabelFn(scope);
        iElem.find('span').html(iLabel2);
        var onChange = function() {
          scope.$apply(function(){
            var tem = 'dto["' + iName2 + '"]';
            $parse(tem).assign(scope,iValue2);
          });
        };
        iElem.bind('change', onChange);
      }};
  }).directive('lwInput', function($parse) {
    return {
      //transclusion if going to allow css in here??? same as above???
      template: '<input class="padded">',
      replace: true,
      restrict: 'A',
      link: function(scope, iElem, iAttrs,ctrl) {
        var iName = iAttrs.name;
        var iNameFn = $parse(iName );
        var iName2 = iNameFn(scope);
        var iValue = iAttrs.value;
        var iValueFn = $parse(iValue);
        var iValue2 = iValueFn(scope);
        iElem.val(iValue2);
        var onChange = function() {
          scope.$apply(function(){
            var tem = 'dto["' + iName2 + '"]';
            $parse(tem).assign(scope,iElem.val());
          });
        };
        iElem.bind('change', onChange);
      }};
  }).directive('lwDrop', function($parse) {
    return {
      //needs some polish to say the least
      template: '<div class="padded"><div id="inner">Drop Here!</div></div>',
      replace: true,
      restrict: 'A',
      link: function(scope, iElem, iAttrs,ctrl) {
        var iName = iAttrs.name;
        var iNameFn = $parse(iName );
        var iName2 = iNameFn(scope);
        var iValue = iAttrs.value;
        var iValueFn = $parse(iValue);
        var iValue2 = iValueFn(scope);
        //need to get the accept list from the server probably??
        iElem.droppable({ accept: ".person, .org", drop: function(event, ui) {console.log(event);}});
        var onDrop = function(event, ui) {
          scope.$apply(function(){
          var tem = 'dto["' + iName2 + '"]';
          $parse(tem).assign(scope, $(ui.draggable[0]).attr("uuid"));
          //need to take out the <a href> and put it below
          //to hopefully leave the widget in the search list
          iElem.find("#inner").html(ui.draggable[0]);
          });
        };
        iElem.on('drop', onDrop);
      }};
  }).directive('lwTextarea', function($parse) {
    return {
      //needs some polish to say the least
      template: '<textarea rows="2" class="padded"></textarea>',
      replace: true,
      restrict: 'A',
      link: function(scope, iElem, iAttrs,ctrl) {
        var iName = iAttrs.name;
        var iNameFn = $parse(iName );
        var iName2 = iNameFn(scope);
        var iValue = iAttrs.value;
        var iValueFn = $parse(iValue);
        var iValue2 = iValueFn(scope);
        iElem.val(iValue2);
        var onChange = function() {
          scope.$apply(function(){
            var tem = 'dto["' + iName2 + '"]';
            $parse(tem).assign(scope,iElem.val());
          });
        };
        iElem.bind('change', onChange);
      }};
  })
;
