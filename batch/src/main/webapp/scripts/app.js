var app = angular.module('app', ['ngResource', 'ngGrid', 'ui.bootstrap']);

app.controller('realmsController', function ($scope, $rootScope, realmsService) {
    realmsService.query(function (data) {
        $scope.realms = data;
    });

    $scope.submit = function () {
        $rootScope.$broadcast('refreshGrid', $scope.search);
    };
});

app.controller('itemsController', function ($scope, itemsService) {
    $scope.gridOptions = {
        data: 'itemData',
        useExternalSorting: false,
        multiSelect: false,
        selectedItems: [],

        columnDefs: [
            { field: 'itemId', displayName: 'Item Id' },
            { field: 'quantity', displayName: 'Quantity' },
            { field: 'bid', displayName: 'Bid' },
            { field: 'minBid', displayName: 'Min Bid' },
            { field: 'maxBid', displayName: 'Max Bid' },
            { field: 'avgBid', displayName: 'Avg Bid' },
            { field: 'buyout', displayName: 'Buyout' },
            { field: 'minBuyout', displayName: 'Min Buyout' },
            { field: 'maxBuyout', displayName: 'Max Buyout' },
            { field: 'avgBuyout', displayName: 'Avg Buyout' },
            { field: 'auctionHouse', displayName: 'AH' },
        ]
    };

    $scope.$on('refreshGrid', function (event, id) {
        itemsService.query(id, function (data) {
            $scope.itemData = data;
        })
    });
});

app.factory('realmsService', function ($resource) {
    return $resource('resources/wowauctions/realms');
});

app.factory('itemsService', function ($resource) {
    return $resource('resources/wowauctions/items');
});
