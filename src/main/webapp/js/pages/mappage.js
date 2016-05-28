/**
 * Created by Ivan on 5/27/2016.
 */
angular.module('templateapp').controller('MapController', function($scope, $http, $interval, $location, PoemDataService) {

    $scope.initMap = function() {
        var mapDiv = document.getElementById('map');
        var map = new google.maps.Map(mapDiv, {
           center: {lat: 44.540, lng: -78.546},
            zoom: 8
        });
        var infoWindow = new google.maps.InfoWindow({map: map});

        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(function(position) {
                var pos = {
                    lat: position.coords.latitude,
                    lng: position.coords.longitude
                };

                infoWindow.setPosition(pos);
                infoWindow.setContent('Location found.');
                //map.setCenter(pos);

                var marker = new google.maps.Marker({
                    position: pos,
                    icon: '/img/icon.png',
                    map: map
                });
            }, function() {
                handleLocationError(true, infoWindow, map.getCenter());
            });
        } else {
            // Browser doesn't support Geolocation
            handleLocationError(false, infoWindow, map.getCenter());
        }



    }

});