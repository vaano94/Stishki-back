/**
 * Created by Ivan on 4/27/2016.
 */
angular.module('app').controller('PoemChooseController',function($scope, $http, ngDialog){

	$scope.showPoemDescription = function() {
		 ngDialog.open({ template: 'poemdescription.html', className: 'ngdialog-theme-default', width:'50%' });
	}



});
