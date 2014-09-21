var app = angular.module('app', ['ngResource', 'ngGrid', 'ui.bootstrap']);

app.controller('realmsController', function ($scope, realmsService) {
    realmsService.query(function (data) {
        $scope.realms = data;
    });
});

app.controller('itemsController', function ($scope, itemsService) {


});

app.factory('realmsService', function ($resource) {
    return $resource('resources/wowauctions/realms');
});

app.factory('itemsService', function ($resource) {
    return $resource('resources/wowauctions/items');
});
