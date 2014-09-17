var app = angular.module('controllers.auctionsstats', []);

app.controller('AuctionsStatsCtrl',
			function($scope, $http) {
		$http.get('rest/itemsstats').success(function(data) {
			$scope.itemsStats = data;
			$scope.selectedItem = $scope.itemsStats[0];
	    });
	});
