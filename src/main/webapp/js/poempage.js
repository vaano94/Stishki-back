

angular.module('templateapp').controller('PoemFieldController', function($scope, $http, $interval, PoemDataService, ngDialog) {

		var ctrl = this;
		counter = 0;
		ctrl.displayedForms = [];
		$scope.type = "АБВ";
		$scope.RhymeData = {};

		$interval(getRhyme,3300);

		function getRhyme() {
			rhyme = $('#rhyme_field').val();
			storagetoken = localStorage['token'] || "";
			if ($scope.existingRhyme==rhyme || $scope.RhymeData.length==0 || $('rhyme_field').val()==""){return;}
			values = {"rhyme": rhyme, "token": storagetoken};
			$http.post("http://localhost:8080/rest/rhymes/rhyme", values)
			.then(function(response) {
				raw = JSON.stringify(response.data);
                result = JSON.parse(raw);

                $scope.RhymeData = result.rhymes;
                $scope.existingRhyme = rhyme;
                
			});
		}

		$scope.firstLaunch = function() {
			
			$scope.type = PoemDataService.getPoemType();
			$scope.existingRhyme = "";
			ngDialog.close();
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

