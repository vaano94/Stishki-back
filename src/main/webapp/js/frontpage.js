(function() {

var app = angular.module('app', ['ngDialog']);
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

app.factory('PoemDataFactory', function() {

});

app.service('LikeService', function($http){

	this.answer = "something";


	this.addLike = function(id) {

		var storagetoken = localStorage['token'] || "";
   		console.log(storagetoken);
   		if (storagetoken=="") {
   			Materialize.toast('Чтобы поставить лайк, войдите в систему', 4500);
		}
		else {
			details = {"token": storagetoken, "id": id};
			var promise = $http.post("http://localhost:8080/rest/poem/like", details)
			.then(function(response) {
				respond = response.data;
				jsonresult = respond.result;
				if (jsonresult=="increment") {
					return respond.result;
					return this.answer;
				}
				else if (jsonresult=="decrement") {
					this.answer = "decement";
					return respond.result;
					return this.answer;
				}
				else if (jsonresult=="BAD") {
					this.answer = "BAD";
					return respond.result;
					return this.answer;
				}

			});
			return promise;
		}
	}


	this.addDislike = function(id) {

		var storagetoken = localStorage['token'] || "";
   		console.log(storagetoken);
   		if (storagetoken=="") {
   			Materialize.toast('Чтобы поставить дизлайк, войдите в систему', 4500);
   		}
		else {
			details = {"token": storagetoken, "id": id};
			var promise = $http.post("http://localhost:8080/rest/poem/dislike", details)
			.then(function(response) {
				respond = response.data;
				jsonresult = respond.result;
				if (jsonresult=="increment") {
					return respond.result;
				}
				else if (jsonresult=="decrement") {
					this.answer = "decement";
					return respond.result;
				}
				else if (jsonresult=="BAD") {
					this.answer = "BAD";
					return respond.result;
				}

			});
			return promise;
		}
	}

});


app.controller('dataFetchController', function($scope, $http, LoginService, LikeService, ngDialog) {
	var poems = {};
	$scope.PoemData = {};
	
	 $scope.clickToOpen = function () {
        ngDialog.open({ template: 'poemchoose.html', className: 'ngdialog-theme-default', width:'70%' });
    };

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


	$scope.updateLike = function(id, likes, index) {

		selector = "#like"+id;
		LikeService.addLike(id).then(function(d) {
		console.log('Return after promise: ' + d);
		if (d == "increment") {
			// Вот вместо этого апдейт scope.PoemData
			$scope.PoemData[index].likes += 1;
			//заблочить поле дизлайка
			dislikeid= "#dislike"+id;
			$(dislikeid).prop( "disabled", true );
		}
		else if (d == "decrement") {
			$scope.PoemData[index].likes -= 1;
		
		}
		else if (d == "BAD") {
			console.log(d);
		};
		});
 	};

 	$scope.updateDislike = function(id, likes,index) {

		selector = "#dislike"+id;
		//$scope.PoemData[index].author = "ОООООООООООО";
		LikeService.addDislike(id).then(function(d) {
		console.log('Return after promise: ' + d);
		if (d == "increment") {
			// Вот вместо этого апдейт scope.PoemData
			$scope.PoemData[index].dislikes += 1;
		}
		else if (d == "decrement") {
			$scope.PoemData[index].dislikes -= 1;
		}
		else if (d == "BAD") {
			console.log(d);
		};
		});
		
	};

	});



})();