(function() {

var app = angular.module('app', ['ngDialog', 'ngRoute']);


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

app.factory('PoemDescFactory', function() {

    var itemsService = "Тут ничего нет";

    itemsService.get = function() {
        return itemsService;
    };
    itemsService.set = function(item) {
        itemsService = item;
        console.log(itemsService);
    };

    return itemsService;
});

app.service('PoemDataService', function($window) {
	this.poemType = localStorage['poemtype'] || "";
	this.setPoemType = function(poem) {
		switch(poem) {
			// Two-syllable rhymes
			case 'Yamb': this.poemType='Ямб'; break;
			case 'Horey': this.poemType='Хорей'; break;
			// Three-syllable rhymes
			case 'Amphibrah': this.poemType='Амфибрахий'; break;
			case 'Anapest': this.poemType='Анапест'; break;
			case 'Daktil': this.poemType='Дактиль'; break;
			// Pirozhok and Poroshok
			case 'Powder': this.poemType='Порошок'; break;
			case 'Cake': this.poemType='Пирожок'; break;
			// Beliy and bezrifmy
			case 'White': this.poemType='Белый стих'; break;
			case 'Norhyme': this.poemType='Верлибр'; break;
			// Stih v proze and Monorim
			case 'Proza': this.poemType='В прозе'; break;
			case 'Monorhyme': this.poemType='Монорим'; break;
			// Acrostih and telestih
			case 'Acro': this.poemType='Акростих'; break;
			case 'Телестих': this.poemType='Телестих'; break;
			// Another
			case 'Another': this.poemType='Вольный'; break;

			default this.poemType = 'Unknown'; break;
		}
		//this.poemType = poem;
		localStorage['poemtype'] = poem; // only strings
		console.log(this.poemType);
	}
	this.getPoemType = function(){
		this.poemType = localStorage['poemtype'] || "";
		return this.poemType;
	}
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


app.directive("scroll", function ($window, $http) {
	var offset = 40;
	var displayed;
    return function($scope, element, attrs) {
      
        angular.element($window).bind("scroll", function() {
        	//console.log("page offset is: " + this.pageYOffset);
        	//console.log("element summary offset is: " + $('#poem115').outerHeight(true)*35);
            if (this.pageYOffset + $('#poem115').outerHeight(true)*5 >= $('#poem115').outerHeight(true)*offset) {
            	offset += 40;
                 $http.post("http://localhost:8080/rest/poem/offset", offset-40 )
                 .then(function(response){
            	 	
            	 	respond = JSON.stringify(response.data);
					var result = JSON.parse(respond);
					console.log(result);
                 	for (i = 0; i<result.poems.length; i++) {
                 		result.poems[i].content = result.poems[i].content.split("\n");
                 		$scope.PoemData.push(result.poems[i]);
             		}
             		displayed = result.offset;
             		console.log("Displayed: " + displayed + " .. poemsLength: " + $scope.PoemData.length);
                 });
             } 
   			$scope.$apply();
        });
    };
});


app.controller('dataFetchController', function($scope, $http, $interval, LoginService, LikeService, ngDialog) {
	/*var poems = {};
	$scope.PoemData = {};*/

	$scope.clickToOpen = function () {
        ngDialog.open({ template: 'poemchoose.html', className: 'ngdialog-theme-default', width:'70%' });
    };

    //$interval(callOnTimeout,1000);

    /*function callOnTimeout() {
    	var height = $('#poem115').outerHeight(true);
    	console.log("Card height is : " + height);
    }*/

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
		//console.log($scope.a);
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