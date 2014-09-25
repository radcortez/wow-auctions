var app = angular.module('app', ['ngResource', 'ngGrid', 'ui.bootstrap', 'googlechart']);

app.controller('searchController', function ($scope, $rootScope, realmsService) {
    realmsService.query(function (data) {
        $scope.realms = data;
    });

    $scope.submit = function () {
        $rootScope.$broadcast('refreshGrid', $scope.search);
    };

    $scope.formatRealm = function ($model) {
        var inputLabel = '';

        angular.forEach($scope.realms, function (realm) {
            if ($model === realm.id) {
                inputLabel = realm.realmDetail;
            }
        });

        return inputLabel;
    }
});

app.controller('itemsController', function ($scope, $filter, itemsService) {
    $scope.$on('refreshGrid', function (event, id) {
        itemsService.query(id, function (data) {
            $scope.itemData = data;
            $scope.itemId = id.itemId;

            document.getElementById("itemLink").href = "http://www.wowhead.com/item=" + $scope.itemId;

            $WowheadPower.refreshLinks();
            drawChart();
        })
    });

    $scope.gridOptions = {
        data: 'itemData',
        useExternalSorting: false,
        multiSelect: false,
        selectedItems: [],

        columnDefs: [
            { field: 'quantity', displayName: 'Quantity' },
            { field: 'minBid', displayName: 'Min Bid', cellFilter: 'gold' },
            { field: 'maxBid', displayName: 'Max Bid', cellFilter: 'gold' },
            { field: 'avgBid', displayName: 'Avg Bid', cellFilter: 'gold' },
            { field: 'minBuyout', displayName: 'Min Buyout', cellFilter: 'gold' },
            { field: 'maxBuyout', displayName: 'Max Buyout', cellFilter: 'gold' },
            { field: 'avgBuyout', displayName: 'Avg Buyout', cellFilter: 'gold' },
            { field: 'auctionHouse', displayName: 'AH' }
        ]
    };

    function drawChart() {
        var rows = [];
        for (var i = 0; i < $scope.itemData.length; i = i + 2) {
            rows.push({
                c: [
                    {v: new Date($scope.itemData[i].timestamp)},
                    {v: $scope.itemData[i].avgBid / 10000},
                    {v: $scope.itemData[i+1].avgBid / 10000},
                    {v: $scope.itemData[i].avgBuyout / 10000},
                    {v: $scope.itemData[i+1].avgBuyout / 10000}
                ]});
        }

        $scope.chart = {};
        $scope.chart.type = "LineChart";
        $scope.chart.data = {
            "cols": [
                {label: "Timestamp", type: "datetime"},
                {label: "Allience Bid", type: "number"},
                {label: "Horde Bid", type: "number"},
                {label: "Allience Buyout", type: "number"},
                {label: "Horde Buyout", type: "number"}
            ],
            "rows": rows};

        $scope.chart.options = {
            displayExactValues: true,
            pointSize: 2,
            vAxis: {
                title: "",
                gridlines: {count: 5}
            },
            hAxis: {
                title: ""
            },
            series: {
                0: { color: 'dodgerblue' },
                1: { color: 'indianred' },
                2: { color: 'blue' },
                3: { color: 'red' }
            }
        };

        $scope.chart.formatters = {};
    }
});

app.factory('realmsService', function ($resource) {
    return $resource('resources/wowauctions/realms');
});

app.factory('itemsService', function ($resource) {
    return $resource('resources/wowauctions/items');
});

app.filter('gold', function() {
    return function(amount) {
        var gold = parseInt(amount / 10000);
        var silver = parseInt(amount % 10000 / 100);
        var copper = parseInt(amount % 10000 % 100);
        return gold + "G " + silver + "S " + copper + "C";
    };
});
