(function() {

var app = angular.module('app', []);
// determines whether user is logged in system
// token field
var token = "";
// an array of poems received from hashtag
var poemFromHash;
var globalLogged = false;

app.service('LoginService', function(){
	this.logged = false;

	this.setLogged = function() {
		this.logged = true;
		console.log(this.logged);
	}
	this.setUnlogged = function() {
		this.logged = false;
		console.log(this.logged);
	}
	this.getLogStatus = function() {
		return this.logged;
	}
});

app.controller('dataFetchController', function($scope, $http, LoginService) {
	var poems = {};
	$scope.PoemData = {};

	$scope.$on('navClick', function(ev,data) {

		tags = $('#search').val();
			var loginJson = {"hashtags":tags};
			$http.post("http://localhost:8080/rest/poem/hashtags", loginJson)
			.then(function(response) {

				respond = JSON.stringify(response.data);
				var result = JSON.parse(respond);
				//if (!respond.result=="BAD" && !result.poems.length==0) {
				toParse = result.poems;
				//$scope.PoemData = response.data.poems;
				poemFromHash = response.data.poems;
        		for (var i = 0; i < toParse.length; i++) {
        		toParse[i].content = toParse[i].content.split("\n");
        		};
        		
        		$scope.PoemData = toParse; 
        		console.log($scope.PoemData)});

		console.log('Бля')})

	$scope.ctrlLoggedStatus = function() {
		$scope.a = LoginService.getLogStatus();
		console.log($scope.a);
		return LoginService.getLogStatus();
	}


	this.getByHash = function(tag) {
		console.log(tag);
		tag = "#"+tag;
		var hashtagJson = {"hashtags":tag};
			$http.post("http://localhost:8080/rest/poem/hashtags", hashtagJson)
			.then(function(response) {

				respond = JSON.stringify(response.data);
				var result = JSON.parse(respond);
				//if (!respond.result=="BAD" && !result.poems.length==0) {
				toParse = result.poems;
				//$scope.PoemData = response.data.poems;
				poemFromHash = response.data.poems;
        		for (var i = 0; i < toParse.length; i++) {
        		toParse[i].content = toParse[i].content.split("\n");
        		};
        		$scope.PoemData = toParse; 
	});
		};

	this.getData = function() {

		$http.post("http://localhost:8080/rest/poem/newpoems", {})
    	.then(function(response) {
    	poems = response.data;
        $scope.PoemData = response.data;



        for (var i = 0; i < poems.length; i++) {
        	poems[i].content = poems[i].content.split("\n");
        };

    	});	
	};

  	$scope.$on('navClick', function(ev,data) {console.log(data)})

	this.retrievePoems = function() {
		//poems = response.data;
		//$scope.PoemData = poemservice.getPoems();
		$scope.PoemData = poemFromHash;
		poems = $scope.PoemData;
		for (var i = 0; i < poems.length; i++) {
        	poems[i].content = poems[i].content.split("\n");
        };
	};

	this.fetchnew = function() {
		$http.post("http://localhost:8080/rest/poem/newpoems", {})
    	.then(function(response) {
    	poems = response.data;
        $scope.PoemData = response.data;

        for (var i = 0; i < poems.length; i++) {
        	
        	poems[i].content = poems[i].content.split("\n");
        	if (poems[i].content.length!=0) {
	        	for (j=0;i<poems[i].hashtags;j++) {

	        	}
        	}	
        };
    	});	
	}

		$scope.$watch(
            "PoemData",
                function() {
                console.log($scope.PoemData);
				}	
            );		


});

/*// проверка пароля
app.directive('match', function($parse,$location) {
  return {
    require: 'ngModel',
    link: function(scope, elem, attrs, ctrl) {
      scope.$watch(function() {        
        return $parse(attrs.match)(scope) === ctrl.$modelValue;
      }, function(currentValue) {
        ctrl.$setValidity('mismatch', currentValue);
      });
    }
  };
});*/


/*app.controller('registrationCtrl', function($scope,$http,$timeout) {
	$scope.master = {};
	$scope.regForm = angular.copy($scope.form);
	var data = {};
	this.respond = "";
	$scope.update=function(user) {
		$scope.master=angular.copy(user);
		data = $scope.master;
		var registerJson = {"nickName": data.nickName,
						"password": data.password,
						"firstName":data.firstName,
						"email":data.email,
						"type":"user"};
	$http.post("http://localhost:8080/rest/registerjson/add", registerJson)
	.then(function(response) {
		respond = JSON.stringify(response.data);
		//alert(respond.toString());
		var result = JSON.parse(respond);
		if (result.result=="OK") {
			 Materialize.toast('Регистрация успешна!', 3000, 'rounded');
			 $("#first_name").val('');
			 $("#last_name").val('');
			 $("#email").val('');
			 $("#password").val('');
			 $("#password_C").val('');

			 $timeout(function() {window.location.replace("/page.html");}, 4000);
			 //window.location.replace("/page.html");
    		 //$scope.$apply();       

		}
		else {
			if (result.result=="nicknameIssue") {
			$("#last_name").val('');
			$("#password_c").val('');
			Materialize.toast('Пользователь с таким ником уже существует', 4500);
			}
			if (result.result=="emailIssue") {
			$("#email").val('');
			$("#password_c").val('');
			Materialize.toast('Пользователь с таким email уже существует', 4500);
			}
				
		}
	});
	}

});*/

})();