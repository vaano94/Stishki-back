(function() {

var templateapp = angular.module('templateapp',['ngDialog','ngRoute']);


    templateapp.config(['$routeProvider', function($routeProvider){
        $routeProvider
            .when('/', {templateUrl: 'frontpage.tmp.html'
               // controller: 'dataFetchController'
            })
            .when('/b', {templateUrl:'poemwrite.html'
            //controller: 'PoemFieldController'
            })
            .when('/registration', {templateUrl: 'registration.html'});

    }]);

    // determines whether user is logged in system
// token field
    var token = "";
// an array of poems received from hashtag
    var poemFromHash;
    var globalLogged = false;

    templateapp.service('LoginService', function(){
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

    templateapp.factory('PoemDescFactory', function() {

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

    templateapp.service('PoemDataService', function($window) {
        this.poemType = localStorage['poemtype'] || "";
        this.setPoemType = function(poem) {
            this.poemType = poem;
            localStorage['poemtype'] = poem; // only strings
            console.log(this.poemType);
        }
        this.getPoemType = function(){
            this.poemType = localStorage['poemtype'] || "";
            return this.poemType;
        }
    });

    templateapp.service('LikeService', function($http){

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


})();