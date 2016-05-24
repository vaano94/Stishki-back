/**
 * Created by Ivan on 4/27/2016.
 */
//angular.module('app').directive("upperNavigation", function($rootScope){
angular.module('templateapp').directive("upperNavigation", function($rootScope){
    return {
      restrict: "E",
      scope: {cb: '&cb'},
      templateUrl: "navigation.html",
      link: function(scope, element, attrs, dateTimeController) {
		    console.log('linked directive, passing argument');
		    scope.send = function (hashtags) {
		      	//console.log('emitting')
		      	//scope.master = angular.copy(hashtags);
				/*data = scope.master;*/
		       	$rootScope.$broadcast('navClick',angular.copy(hashtags));
		    }
		}
    };
  })
	.controller("navigationCtrl", function($scope,$http,$rootScope,LoginService){

		//$scope.$on('navClick', function(ev,data) {console.log(data)})
	
		$(document).ready(function()	{
    	// the "href" attribute of .modal-trigger must specify the modal ID that wants to be triggered
    	$('.modal-trigger').leanModal();
  		});
  		// показывает окно логина
  		var showmodal = function() {
  			 $('#modal1').openModal();
  		}

  		$scope.logged = false;

		var data={};
		this.respond = "";
		$scope.master = {};

		$scope.init = function () {
   		var storagetoken = localStorage['token'] || "";
   		console.log(storagetoken);
   		if (storagetoken!="") 
   			$scope.showlogged();
		};

		$scope.showlogged = function() {
			$scope.logged = true;
			globalLogged = true;
			LoginService.setLogged();
			
		}
		$scope.showunlogged = function() {
			$scope.logged = false;
			globalLogged = false;
			LoginService.setUnlogged();
		}

		$scope.loginuser = function(login) {
		
			$scope.master = angular.copy(login);
			data = $scope.master;
			var loginJson = {"nickName":data.nickName,
							"password":data.password};
			$http.post("http://localhost:8080/rest/login/go", loginJson)
			.then(function(response) {
			respond = JSON.stringify(response.data);
			var result = JSON.parse(respond);
			token = result.token;

			if (result.result=="OK" && token!="") {
				Materialize.toast('Логин успешен!', 4500);
				localStorage['token'] = token; // only strings
				//$scope.logged = true;
				LoginService.setSubscriptions = result.genres;
				$('#modal1').closeModal();
				$scope.showlogged();
			}
			else if (result.result=="BAD" && result.token=="") {
				Materialize.toast('Пользователя с такими данными не существует', 4500);
			}
			});

		}

		$scope.logoutuser = function() {
			localStorage['token']="";
			$scope.showunlogged();
		}
  
  	
	

	});