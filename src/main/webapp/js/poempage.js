

angular.module('templateapp').controller('PoemFieldController', function($scope, $http, PoemDataService) {

		var ctrl = this;
		counter = 0;
		ctrl.displayedForms = [];
		$scope.type = "АБВ";

		$scope.firstLaunch = function() {
			$scope.type = PoemDataService.getPoemType();
			console.log("Type received from storage: " + $scope.type);
		}

		$scope.addTagField = function() {
			if (counter == 3) {
				return;
			}
			counter ++;
			ctrl.displayedForms.push("form1.tpl.html");

		}
		
    

	});

