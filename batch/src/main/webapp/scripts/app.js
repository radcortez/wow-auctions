var app = angular.module('app', ['ngRoute', 'ngResource'])
    .config(['$routeProvider', function ($routeProvider) {

        $routeProvider.when('/', {
            templateUrl: 'views/realms.html',
            controller: 'realmsController'
        });

        $routeProvider.when('/batchs', {
            templateUrl: 'views/batchs.html',
            controller: 'batchsController'
        });
    }]);

app.controller('realmsController', function ($scope, realmsService) {
    realmsService.query(function (data) {
        $scope.realms = data;
    })
});

app.controller('batchsController',
    function ($scope, $http) {
        $scope.request = function (param) {
            $http.get('rest/batchexecution?action=' + param);
        }
    });

app.factory('realmsService', function ($resource) {
    return $resource('resources/wowauctions/realms');
});
