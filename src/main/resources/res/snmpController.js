/*
 * This program is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License, version 2.1 as published by the Free Software
 * Foundation.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * program; if not, you can obtain a copy at http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 * or from the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * Copyright 2014 Pentaho Corporation. All rights reserved.
 */

define(["angular","angular-resource"], function(angular, Resource){

var app = angular.module('snmpApp', ['ngResource']);

app.factory('Traps', ['$resource',
  function($resource){
    return $resource('../cxf/snmp/traps/list', {}, {
      query: {method:'GET', params:{}, isArray:true}
    });
  }]);

app.controller('TrapController', ['$scope', 'Traps', function($scope, traps) {
  function reloadTraps() {
    var newTraps = traps.query(function(){
      if(!$scope.traps) {
        $scope.traps = newTraps;
      } else if ($scope.traps.length > 0 && $scope.traps[0].variables[1].value != newTraps[0].variables[1].value) {
        $scope.traps = newTraps;
      }
    });

  }
  setInterval(reloadTraps, 1000);
}]);

return app;
});