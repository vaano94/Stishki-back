/**
 * Created by Ivan on 4/27/2016.
 */
angular.module('app').controller('PoemChooseController',function($scope, $http, ngDialog, PoemDescFactory){
	//$scope.chooseData = 'abc';

	$scope.showPoemDescription = function(event) {
		
		uri = "http://localhost:8080/rest/poeminfo/"+event.target.id;
		$http.get(uri).
		then(function(response){

			respond = JSON.stringify(response.data);
			var result = JSON.parse(respond);
			// Обработка данных Как писать
			
			var howToParsed = result.howTo.split("\n");
			result.howTo = howToParsed;

			for (i=0;i<result.examples.length;i++) {
        		result.examples[i] = result.examples[i].split("\n");
        	};
				

			$scope.chooseData = result;
			
			console.log($scope);
			PoemDescFactory.set($scope.chooseData);

			

			ngDialog.open({ template: 'poemdescription.html', className: 'ngdialog-theme-default', width:'50%' });

		});

		
		
	}



});
