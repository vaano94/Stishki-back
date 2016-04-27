/**
 * Created by Ivan on 4/27/2016.
 */
// проверка пароля
angular.module('app').directive('match', function($parse,$location) {
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
});

angular.module('app').controller('registrationCtrl', function($scope,$http,$timeout) {
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

});