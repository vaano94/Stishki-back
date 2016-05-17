/**
 * Created by Ivan on 4/27/2016.
 */
angular.module('templateapp').controller('PoemDescriptionController',function($scope, $http, ngDialog, PoemDescFactory){
	
	$scope.doStuff = function() {
	console.log($scope);
	//$scope.poeminfo = $scope.chooseData;
	$scope.poeminfo = PoemDescFactory.get();
		
	};

});
