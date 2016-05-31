/**
 * Created by Ivan on 5/18/2016.
 */
angular.module('templateapp').controller('UserInfoController', function($scope, $http){
    $scope.userData = {};
    $scope.userData.repPass="";
    changedNick_old = "";
    changedEmail_old = "";
    oldPass_old = "";
    newPass_old = "";
    newPass_r_old = "";


    $scope.getUserProfileInfo = function() {
        token = localStorage['token'] || '';
        data = {"token":token};
        /*$(document).ready(function() {
            $('select').material_select();
        });*/
        $http.post("http://localhost:8080/rest/login/info",data)
            .then(function(response){
                result = JSON.stringify(response.data);
                result = JSON.parse(result);
                $scope.userData.nick = result.nickName;
                $scope.userData.mail = result.email;
                $scope.userData.likescount = result.likes;
                $scope.userData.dislikescount = result.dislikes;
                $scope.userData.poemscount = result.poemcount;
                $scope.userData.genres = result.genres;

                changedNick_old = result.nickName;
                changedEmail_old =result.email;
                oldPass_old = result.likes;
                newPass_old = result.dislikes;
                newPass_r_old = result.poemcount;
                genres_old = result.genres;

                subs = "";
                array = {};
                for (var key in $scope.userData.genres) {
                    if ($scope.userData.genres.hasOwnProperty(key)) {
                        subs += $scope.userData.genres[key] + " ";
                    }
                }
                subs = subs.trim();

                /*$.each(subs.split(" "), function(i,e){
                    $("#sel option[value='" + e + "']").prop("selected", true);
                });*/
                array = subs.split(" ");
                $('#select_sub').val(array);
                //$("#select_sub").multiselect("refresh");
                $('select').material_select();


                a = 4; b= 10;

                // Novice Achievement
                if ($scope.userData.poemscount < 10) {
                    $('#sample_goal').goalProgress({
                        goalAmount: 10,
                        currentAmount: $scope.userData.poemscount,
                        textBefore: 'Написано стишков: ',
                        textAfter: '/' + '10'
                    });
                }
                if ($scope.userData.poemscount>=10) {
                    $('#sample_goal').goalProgress({
                        goalAmount: 10,
                        currentAmount: 10,
                        textBefore: 'Стишков : ',
                        textAfter: ' Новичок'
                    });
                }
                // RhymeMaster Achievement
                if ($scope.userData.poemscount<50) {
                    $('#sample_goal2').goalProgress({
                        goalAmount: 50,
                        currentAmount: $scope.userData.poemscount,
                        textBefore: 'Написано стишков: ',
                        textAfter: '/' + '50'
                    });
                }
                if ($scope.userData.poemscount>=50) {
                    $('#sample_goal2').goalProgress({
                        goalAmount: 50,
                        currentAmount: 50,
                        textBefore: 'Стишков : ',
                        textAfter: ' Рифмоплет'
                    });
                }
                // Adept Achievement
                if ($scope.userData.poemscount < 150) {
                    $('#adept_ach').goalProgress({
                        goalAmount: 150,
                        currentAmount: $scope.userData.poemscount,
                        textBefore: 'Cтишков : ',
                        textAfter: '/' + '150'
                    });
                }
                if ($scope.userData.poemscount>=150) {
                    $('#adept_ach').goalProgress({
                        goalAmount: 150,
                        currentAmount: 150,
                        textBefore: 'Стишков : ',
                        textAfter: ' Адепт'
                    });
                }
                // Master
                if ($scope.userData.poemscount < 500) {
                    $('#master_ach').goalProgress({
                        goalAmount: 500,
                        currentAmount: $scope.userData.poemscount,
                        textBefore: 'Cтишков : ',
                        textAfter: '/' + '500'
                    });
                }
                if ($scope.userData.poemscount>=500) {
                    $('#master_ach').goalProgress({
                        goalAmount: 500,
                        currentAmount: 500,
                        textBefore: 'Стишков : ',
                        textAfter: ' Мастер'
                    });
                }
                if ($scope.userData.likescount < 1000) {
                    $('#recognized_ach').goalProgress({
                        goalAmount: 1000,
                        currentAmount: $scope.userData.likescount,
                        textBefore: 'Лайкнуто : ',
                        textAfter: '/' + '1000'
                    });
                }
                if ($scope.userData.likescount > 1000) {
                    $('#recognized_ach').goalProgress({
                        goalAmount: 1000,
                        currentAmount: 1000,
                        textBefore: '',
                        textAfter: 'Признанный поэт'
                    });
                }
                if ($scope.userData.dislikescount < 500) {
                    $('#recognized_ach').goalProgress({
                        goalAmount: 500,
                        currentAmount: $scope.userData.dislikescount,
                        textBefore: 'Дизлайкнуто : ',
                        textAfter: '/' + '1000'
                    });
                }
                if ($scope.userData.likescount > 500) {
                    $('#recognized_ach').goalProgress({
                        goalAmount: 500,
                        currentAmount: 500,
                        textBefore: '',
                        textAfter: 'Непризнанный поэт'
                    });
                }


            });

    };

    $scope.changeInfo = function() {
        $('select').material_select();
        if ($scope.userData.nick==changedNick_old && $scope.userData.mail==changedEmail_old
        && $scope.userData.genres==genres_old) {
            Materialize.toast("Параметры не были изменены", 3500);
        }
        else {
            token = localStorage['token'] || '';
            data = {
                "token": token, "nickname": $scope.userData.nick, "email": $scope.userData.mail,
                "genres": $scope.userData.genres};

                $http.post("http://localhost:8080/rest/userdata/changeinfo", data)
                .then(function (response) {
                    result = JSON.stringify(response.data);
                    result = JSON.parse(result);
                    if (response.data.result == "OK") {
                        Materialize.toast("Данные успешно изменены", 3500);

                    }
                });
        }
    };

    $scope.changePass = function() {
        // if old password is wrong
        if (oldPass!="") {
            token = localStorage['token'] || '';
            // makeHttpRequest to server and identify that oldPass hash corresponds to this one
            data = {"token":token, "password": $scope.userData.oldPass};
            $http.post("http://localhost:8080/rest/userdata/checkpass",data)
                .then(function(response){
                    result = JSON.stringify(response.data);
                    result = JSON.parse(result);
                    if (response.data.result=="OK") {

                        isTrue = $scope.checkPasswords();
                        if (isTrue) {
                            data = {"token":token, "password": $scope.userData.newPass};
                            $http.post("http://localhost:8080/rest/userdata/changepass",data)
                                .then(function(response){
                                    result = JSON.stringify(response.data);
                                    result = JSON.parse(result);
                                    if (response.data.result=="OK") {
                                        Materialize.toast("Пароль изменен!", 3500);
                                        // and set password fields to :""
                                        $scope.userData.oldPass = "";
                                        $scope.userData.newPass = "";
                                        $scope.userData.repPass = "";
                                    }
                                    else {
                                        Materialize.toast("Пароль не изменен!", 3500);
                                    }
                                });
                        }
                        else {
                            Materialize.toast("Пароли не совпадают!", 3500);
                        }
                    }
                    // else say user that the old password didn't match
                    else {
                        Materialize.toast("Неверный старый пароль", 3500);
                        return;
                    }
                });
        }

    };

    $scope.showSubs = function() {
        $('#select_sub').each(function(){
            console.log($(this).val())
        });

    }
    $scope.checkPasswords = function() {
        oldPass_val = $scope.userData.newPass;
        oldPass_r_val = $scope.userData.repPass;
        var validated =  true;
        if(oldPass_val.length < 8 && oldPass_r_val.length < 8)
            validated = false;
        if(!/\d/.test(oldPass_val) && !/\d/.test(oldPass_r_val))
            validated = false;
        if(!/[a-z]/.test(oldPass_val) && !/[a-z]/.test(oldPass_r_val))
            validated = false;
        if(!/[A-Z]/.test(oldPass_val) && !/[A-Z]/.test(oldPass_r_val))
            validated = false;
        if(/[^0-9a-zA-Z]/.test(oldPass_val) && /[^0-9a-zA-Z]/.test(oldPass_r_val))
            validated = false;
        if (oldPass_val!=oldPass_r_val) {
            validated = false;
        }
        $('#valid_div').text(validated ? "Пароли совпадают" : "Пароли не совпадают");
        return validated;
    }


});