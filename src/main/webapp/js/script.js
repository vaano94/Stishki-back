(function() {

var templateapp = angular.module('templateapp',['ngDialog','ngRoute']);


    templateapp.config(['$routeProvider', function($routeProvider){
        $routeProvider
            .when('/', {templateUrl: 'frontpage.tmp.html',
                controller: 'dataFetchController'
            })
            .when('/index', {templateUrl: 'frontpage.tmp.html',
                controller: 'dataFetchController'
            })
            .when('/b', {templateUrl:'poemwrite.html'
            //controller: 'PoemFieldController'
            })
            .when('/userinfo', {
               templateUrl: 'userinfo.html'
            })
            .when('/drafts',  {
                templateUrl: 'draft.tmp.html'
            })
            .when('/map',  {
                templateUrl: 'map.tmp.html'
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
        this.subscriptions = {};

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

        this.getSubscriptions=function(){
            return this.subscriptions;
        }
        this.setSubscriptions=function(subs){
            this.subscriptions=subs;
        }

    });

    templateapp.factory('PoemDescFactory', function() {

        var itemsService = {};

        itemsService.get = function() {
            return itemsService;
        };
        itemsService.set = function(item) {
            itemsService = item;
            console.log(itemsService);
        };

        return itemsService;
    });

    templateapp.service('DraftDelService', function($http){
        this.deleteId = 0;
        this.indexId = 0;
        this.DraftData = [];
        this.getDeleteId = function(){
            return this.deleteId;
        }
        this.setDeleteId = function(id){
            this.deleteId= id;
        }
        this.setIndexId = function (id) {
            this.indexId=id;
        }
        this.getIndexId = function(){
            return this.indexId;
        }
        this.downloadDraftData = function(callback) {
            token = localStorage["token"] || "";
            data = {"token":token};
            this.DraftData = [];
            $http.post("http://localhost:8080/rest/drafts/get", data)
                .then(function(response) {
                    //raw = response.data.drafts;
                    respond = JSON.stringify(response.data);
                    result = JSON.parse(respond);
                    console.log(result);
                    for (i = 0; i < result.drafts.length; i++) {
                        result.drafts[i].content = result.drafts[i].content.split("\n");
                        //$scope.DraftData.push(result.drafts[i]);
                    }
                    /*for (i=0;i<result.drafts.length;i++) {
                        this.DraftData.push(result.drafts[i]);
                    }*/
                    this.DraftData = result.drafts;
                    callback(this.DraftData);
                    //return this.DraftData;
                });
        }
        this.setDraftData = function(data) {
            this.DraftData = data;
        }
        this.getDraftData = function(){
            return DraftData;
        }

    });

    templateapp.service('DraftExchangeService', function(){
        this.Draft = {};
        this.setDraft = function(draft){
            this.Draft = draft;
        }
        this.getDraft =function(){
            return this.Draft;
        }
    });

    templateapp.service('PoemDataService', function() {
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
            case 'Tele': this.poemType='Телестих'; break;
            // Another
            case 'Another': this.poemType='Вольный'; break;

            default: this.poemType = 'Unknown'; break;
        }
        this.revertPoemType = function(poem) {
            switch(poem) {
            // Two-syllable rhymes
            case 'Ямб': return 'Yamb'; break;
            case 'Хорей': return 'Horey'; break;
            // Three-syllable rhymes
            case 'Амфибрахий': return 'Amphibrah'; break;
            case 'Анапест': return 'Anapest'; break;
            case 'Дактиль': return 'Daktil'; break;
            // Pirozhok and Poroshok
            case 'Порошок': return 'Powder'; break;
            case 'Пирожок': return 'Cake'; break;
            // Beliy and bezrifmy
            case 'Белый стих': return 'White'; break;
            case 'Верлибр': return 'Norhyme'; break;
            // Stih v proze and Monorim
            case 'В прозе': return 'Proza'; break;
            case 'Монорим': return 'Monorhyme'; break;
            // Acrostih and telestih
            case 'Акростих': return 'Acro'; break;
            case 'Телестих': return 'Tele'; break;
            // Another
            case 'Вольный': return 'Another'; break;

            default: this.poemType = 'Unknown'; break;
        }
    }
            //this.poemType = poem;
            localStorage['poemtype'] = this.poemType; // only strings
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