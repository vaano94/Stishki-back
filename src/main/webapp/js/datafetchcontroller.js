/**
 * Created by Ivan on 5/14/2016.
 */
angular.module('templateapp').controller('dataFetchController', function($scope, $http, $interval, LoginService, LikeService, ngDialog) {
    var poems = {};
    $scope.PoemData = {};
    $scope.PoemDataOne = [];
    $scope.PoemDataTwo = [];
    $scope.SubData = {};
    $scope.PopularData = {};
    $scope.SearchData= {};
    $scope.subscriptions = {};

    $scope.clickToOpen = function () {
        ngDialog.open({ template: 'poemchoose.html', className: 'ngdialog-theme-default', width:'70%' });
    };

    //$interval(getByTagTest,2000);

    /*function callOnTimeout() {
     var height = $('#poem115').outerHeight(true);
     console.log("Card height is : " + height);
     }*/
    $scope.searchByTagField = function(event) {
        if (event.keyCode == 13) {
            console.log("SEARCH_A ACTIVE");
                tags = $('#tagsearch').val();
                if ($scope.existingTag == tags || $('#tagsearch').val() == "") {
                    return;
                }
                var data = {"hashtags": tags};
                $http.post("http://localhost:8080/rest/poem/hashtags", data)
                    .then(function (response) {

                        raw = JSON.stringify(response.data);
                        var result = JSON.parse(raw);
                        //if (!respond.result=="BAD" && !result.poems.length==0) {
                        toParse1 = result.poems;
                        //$scope.PoemData = response.data.poems;
                        //poemFromHash = response.data.poems;
                        for (var i = 0; i < toParse1.length; i++) {
                            toParse1[i].content = toParse1[i].content.split("\n");
                        }


                        $scope.SearchData = toParse1;
                        $scope.existingTag = tags;
                        //console.log($scope.PoemData)
                    });
        }
    };

    function getByTagTest() {
        tags = $('#search').val();
        if ($scope.existingTag==tags || $scope.PoemData.length!=0 || $('#search').val()==""){return;}
        var loginJson = {"hashtags":tags};
        $http.post("http://localhost:8080/rest/poem/hashtags", loginJson)
            .then(function(response) {

                raw = JSON.stringify(response.data);
                var result = JSON.parse(raw);
                //if (!respond.result=="BAD" && !result.poems.length==0) {
                toParse1 = result.poems;
                //$scope.PoemData = response.data.poems;
                //poemFromHash = response.data.poems;
                for (var i = 0; i < toParse1.length; i++) {
                    toParse1[i].content = toParse1[i].content.split("\n");
                };

                $scope.SearchData = toParse1;
                $scope.existingTag = tags;
                //console.log($scope.PoemData)
                });
    }

    $scope.$on('navClick', function(ev,data) {

        tags = $('#search').val();
        var loginJson = {"hashtags":tags};
        $http.post("http://localhost:8080/rest/poem/hashtags", loginJson)
            .then(function(response) {

                raw = JSON.stringify(response.data);
                var result = JSON.parse(raw);
                //if (!respond.result=="BAD" && !result.poems.length==0) {
                toParse1 = result.poems;
                //$scope.PoemData = response.data.poems;
                //poemFromHash = response.data.poems;
                for (var i = 0; i < toParse1.length; i++) {
                    toParse1[i].content = toParse1[i].content.split("\n");
                };

                $scope.PoemData = toParse1;
                //console.log($scope.PoemData)
                });

        //console.log('Бля')
        });

    $scope.ctrlLoggedStatus = function() {
        $scope.a = LoginService.getLogStatus();
        //console.log($scope.a);
        return LoginService.getLogStatus();
    };


    this.getByHash = function(tag) {
        //console.log(tag);
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
                }
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
                }

            });
    };

    //$scope.$on('navClick', function(ev,data) {console.log(data)})

    this.retrievePoems = function() {
        $scope.PoemData = poemFromHash;
        poems = $scope.PoemData;
        for (var i = 0; i < poems.length; i++) {
            poems[i].content = poems[i].content.split("\n");
        };
    };

    $scope.fetchnew = function() {
        // initialize tabs
        $('ul.tabs').tabs();

        $scope.existingTag="";
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
                    /*if (i%2==0) {
                        $scope.PoemDataTwo.push(poems[i]);
                    }
                    if (i%2!=0) {
                        $scope.PoemDataOne.push(poems[i]);
                    }*/
                }
            });
    };

    $scope.fetchFortyPopular = function() {
        data = {"offset":0};
        $http.post("http://localhost:8080/rest/poem/popular", 0)
            .then(function(response) {
                respond = JSON.stringify(response.data);
                var result = JSON.parse(respond);
                $scope.pop_offset = 40;
                for (i = 0; i<result.poems.length; i++) {
                        result.poems[i].content = result.poems[i].content.split("\n");
                        //$scope.PopularData.push(result.poems[i]);
                }
                $scope.PopularData = result.poems;
                //$scope.$apply();
            });
    };

    $scope.fetchFortySubs = function() {
        subs = LoginService.getSubscriptions();
        token = localStorage['token'] || "";
        data = {"token":token,"offset":0};
        $http.post("http://localhost:8080/rest/poem/sub", data)
            .then(function(response) {
                respond = JSON.stringify(response.data);
                var result = JSON.parse(respond);
                $scope.pop_offset = 40;
                for (i = 0; i<result.poems.length; i++) {
                    result.poems[i].content = result.poems[i].content.split("\n");

                }
                $scope.SubData = result.poems;
            });
    };


    $scope.updateLike = function(id, likes, index) {

        selector = "#like"+id;
        LikeService.addLike(id).then(function(d) {
            console.log('Return after promise: ' + d);
            if (d == "increment") {
                // Вот вместо этого апдейт scope.PoemData
                if ($("#new_a").hasClass("active")) {
                    $scope.PoemData[index].likes += 1;
                }
                if ($("#pop_a").hasClass("active")) {
                    $scope.PopularData[index].likes += 1;
                }
                if ($("#sub_a").hasClass("active")) {
                    $scope.SubData[index].likes += 1;
                }
                if ($("search_a").hasClass("active")) {
                    $scope.SearchData[index].likes += 1;
                }
                //заблочить поле дизлайка
                dislikeid= "#dislike"+id;
                $(dislikeid).prop( "disabled", true );
            }
            else if (d == "decrement") {
                if ($("#new_a").hasClass("active")) {
                    $scope.PoemData[index].likes -= 1;
                }
                if ($("#pop_a").hasClass("active")) {
                    $scope.PopularData[index].likes -= 1;
                }
                if ($("#sub_a").hasClass("active")) {
                    $scope.SubData[index].likes -= 1;
                }
                if ($("search_a").hasClass("active")) {
                    $scope.SearchData[index].likes -= 1;
                }

            }
            else if (d == "BAD") {
                console.log(d);
            }
        });
    };

    $scope.updateDislike = function(id, likes,index) {

        selector = "#dislike"+id;
        //$scope.PoemData[index].author = "ОООООООООООО";
        LikeService.addDislike(id).then(function(d) {
            console.log('Return after promise: ' + d);
            if (d == "increment") {
                // Вот вместо этого апдейт scope.PoemData
                if ($("#new_a").hasClass("active")) {
                    $scope.PoemData[index].dislikes += 1;
                }
                if ($("#pop_a").hasClass("active")) {
                    $scope.PopularData[index].dislikes += 1;
                }
                if ($("#sub_a").hasClass("active")) {
                    $scope.SubData[index].dislikes += 1;
                }
                if ($("search_a").hasClass("active")) {
                    $scope.SearchData[index].dislikes += 1;
                }
            }
            else if (d == "decrement") {
                if ($("#new_a").hasClass("active")) {
                    $scope.PoemData[index].dislikes -= 1;
                }
                if ($("#pop_a").hasClass("active")) {
                    $scope.PopularData[index].dislikes -= 1;
                }
                if ($("#sub_a").hasClass("active")) {
                    $scope.SubData[index].dislikes -= 1;
                }
                if ($("search_a").hasClass("active")) {
                    $scope.SearchData[index].dislikes -= 1;
                }
            }
            else if (d == "BAD") {
                console.log(d);
            };
        });

    };

});

angular.module('templateapp').directive("scroll", function ($window, $http) {
    var offset = 40;
    var sub_offs = 40;
    var pop_offs = 40;
    var displayed;
    return function($scope, element, attrs) {

        angular.element($window).bind("scroll", function() {
            //console.log("page offset is: " + this.pageYOffset);
            //console.log("element summary offset is: " + $('#poem115').outerHeight(true)*35);

            // get the first element id of $scope.PoemData


            if ($("#new_a").hasClass("active")) {
                firstInPoems = $scope.PoemData[0].id;
                console.log("NEW_A ACTIVE");
                if (this.pageYOffset + $("#poem" + firstInPoems).outerHeight(true) * 5 >= $("#poem" + firstInPoems).outerHeight(true) * offset) {
                    offset += 40;
                    $http.post("http://localhost:8080/rest/poem/offset", offset - 40)
                        .then(function (response) {

                            respond = JSON.stringify(response.data);
                            var result = JSON.parse(respond);
                            console.log(result);
                            for (i = 0; i < result.poems.length; i++) {
                                result.poems[i].content = result.poems[i].content.split("\n");
                                $scope.PoemData.push(result.poems[i]);
                            }
                            displayed = result.offset;
                            console.log("Displayed: " + displayed + " .. poemsLength: " + $scope.PoemData.length);
                        });
                }
                $scope.$apply();
            }
            if ($("#pop_a").hasClass("active")) {
                console.log("POP_A ACTIVE");
                firstInPopular = $scope.PopularData[0].id;
                //console.log(firstInPopular);
                height = $("#pop" + firstInPopular).outerHeight(true)
                if (height>270) {
                    height = 270;
                }
                console.log("Needed offset " + height*pop_offs);
                actualOffset = this.pageYOffset + height * 9;
                console.log("Actual offset " + actualOffset );
                if (this.pageYOffset + height * 9 >= height * pop_offs) {
                    $http.post("http://localhost:8080/rest/poem/popular", pop_offs)
                        .then(function (response) {
                            respond = JSON.stringify(response.data);
                            var result = JSON.parse(respond);
                            console.log(result);
                            for (i = 0; i < result.poems.length; i++) {
                                result.poems[i].content = result.poems[i].content.split("\n");
                                $scope.PopularData.push(result.poems[i]);
                            }
                            pop_offs += 40;
                            console.log("Displayed: " + pop_offs + " .. poemsLength: " + $scope.PopularData.length);
                            console.log("POPULAR DATA" + $scope.PopularData);
                        });
                }
                $scope.$apply();
            }
            if ($("#sub_a").hasClass("active")) {
                console.log("SUB_A ACTIVE");
                firstInSub = $scope.SubData[0].id;
                height = $("#pop" + firstInSub).outerHeight(true)
                if (height>270) {
                    height = 270;
                }
                if (this.pageYOffset + $("#sub" + firstInSub).outerHeight(true) * 5 >= $("#sub" + firstInSub).outerHeight(true) * sub_offs) {
                    sub_offs += 40;
                    token = localStorage['token'] || "";
                    data = {"token": token, "offset": sub_offs};
                    $http.post("http://localhost:8080/rest/poem/sub", data)
                        .then(function (response) {
                            respond = JSON.stringify(response.data);
                            var result = JSON.parse(respond);
                            console.log(result);
                            for (i = 0; i < result.poems.length; i++) {
                                result.poems[i].content = result.poems[i].content.split("\n");
                                $scope.SubData.push(result.poems[i]);
                            }
                            console.log("Displayed: " + offset + " .. poemsLength: " + $scope.SubData.length);
                            console.log("POPULAR DATA" + $scope.SubData);
                        });
                }
                $scope.$apply();
            }

        });
    };
});

angular.module('templateapp').directive("scrollpop", function ($window, $http) {
    var offset = 40;
    console.log("Starting offset" + offset);
    return function($scope,element,attrs) {

    angular.element($window).bind("scrollpop", function() {
        
        data = {"offset": offset};
        firstInPopular = $scope.PopularData[0].id;

        if ($("#pop_a").hasClass("active")) {
            if (this.pageYOffset + $("#pop" + firstInPopular).outerHeight(true) * 5 >= $("#pop" + firstInPopular).outerHeight(true) * offset) {
                $http.post("http://localhost:8080/rest/poem/popular", offset)
                    .then(function (response) {
                        respond = JSON.stringify(response.data);
                        var result = JSON.parse(respond);
                        console.log(result);
                        for (i = 0; i < result.poems.length; i++) {
                            result.poems[i].content = result.poems[i].content.split("\n");
                            $scope.PopularData.push(result.poems[i]);
                        }
                        offset += 40;
                        console.log("Displayed: " + $scope.pop_offset + " .. poemsLength: " + $scope.PopularData.length);
                        console.log("POPULAR DATA" + $scope.PopularData);
                    });
            }
        }
        $scope.$apply();
    });
    };

});

angular.module('templateapp').directive("scrollsub", function ($window, $http) {
    var offset = 40;
    console.log("Starting offset" + offset);
    return function($scope,element,attrs) {

        angular.element($window).bind("scrollsub", function() {

            data = {"offset": offset};
            firstInSub = $scope.SubData[0].id;

            if ($("#sub_a").hasClass("active")) {
                if (this.pageYOffset + $("#sub" + firstInSub).outerHeight(true) * 5 >= $("#sub" + firstInSub).outerHeight(true) * offset) {
                    token = localStorage['token'] || "";
                    data = {"token": token, "offset": offset};
                    $http.post("http://localhost:8080/rest/poem/sub", data)
                        .then(function (response) {
                            respond = JSON.stringify(response.data);
                            var result = JSON.parse(respond);
                            console.log(result);
                            for (i = 0; i < result.poems.length; i++) {
                                result.poems[i].content = result.poems[i].content.split("\n");
                                $scope.SubData.push(result.poems[i]);
                            }
                            offset += 40;
                            console.log("Displayed: " + offset + " .. poemsLength: " + $scope.SubData.length);
                            console.log("POPULAR DATA" + $scope.SubData);
                        });
                }
            }
            $scope.$apply();
        });
    };

})



