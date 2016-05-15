/**
 * Created by Ivan on 5/14/2016.
 */
angular.module('templateapp').controller('dataFetchController', function($scope, $http, $interval, LoginService, LikeService, ngDialog) {
    var poems = {};
    $scope.PoemData = {};

    $scope.clickToOpen = function () {
        ngDialog.open({ template: 'poemchoose.html', className: 'ngdialog-theme-default', width:'70%' });
    };

    $interval(getByTagTest,2000);

    /*function callOnTimeout() {
     var height = $('#poem115').outerHeight(true);
     console.log("Card height is : " + height);
     }*/

    function getByTagTest() {
        tags = $('#search').val();
        if ($scope.existingTag==tags && $scope.PoemData.length!=0){return;}
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
    }


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

    //$scope.$on('navClick', function(ev,data) {console.log(data)})

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

})

angular.module('templateapp').directive("scroll", function ($window, $http) {
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
