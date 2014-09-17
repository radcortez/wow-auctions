var app = angular.module('controllers.batch', []);

app.controller('BatchCtrl',
			function($scope, $http) {
		$scope.request = function(param) {
            $http.get('rest/batchexecution?action=' + param);
		}
	});
