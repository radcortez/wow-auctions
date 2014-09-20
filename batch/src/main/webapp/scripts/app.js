angular.module('app', [ 'ngRoute', 'controllers.auctionsstats', 'controllers.batch' ])
	.config(['$routeProvider', function($routeProvider) {
    	$routeProvider.when('/stats', {
			templateUrl: "views/allstats.html",
			controller: "AuctionsStatsCtrl"
		})
		.otherwise({
			templateUrl: "views/batch.html",
			controller: "BatchCtrl"
		});
    }]);