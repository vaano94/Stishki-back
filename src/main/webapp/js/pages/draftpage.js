/**
 * Created by Ivan on 5/25/2016.
 */
angular.module('templateapp').controller('DraftController', function($scope, $http, $interval, PoemDataService, ngDialog) {
    $scope.DraftData = {};
    $scope.DisplayedDraft = {};
    $scope.DisplayedDraft.content="";
    this.showChgBtn = false;

    $scope.getDrafts = function() {
        token = localStorage["token"] || "";
        data = {"token":token};
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
                $scope.DraftData = result.drafts;

            });
    }

    this.showDraft = function(event) {
        elementId = event;
        $scope.DisplayedDraft = $scope.DraftData[elementId];
        $scope.DisplayedDraft.text = "";
        PoemDataService.setPoemType($scope.DraftData[elementId].genre);
        $scope.DisplayedDraft.genre = PoemDataService.getPoemType();
        /*for (i=0;i<$scope.DisplayedDraft.content.length;i++){
            $scope.DisplayedDraft.text += $scope.DisplayedDraft.content[i] + '\n';
        }
        $scope.DisplayedDraft.text = stringContainingNewLines.replace(/(\r\n|\n|\r)/gm, "<br>");
        //$scope.DisplayedDraft.hashtags = $scope.DraftData[elementId].hashtags;*/
        this.showChgBtn = true;
    }

    $scope.PreDeleteDraft = function (id) {
        toDelete = id;
        console.log(toDelete);
        //$scope.clickToOpen = function () {
            ngDialog.open({ template: 'draftDeleteDialog.html', className: 'ngdialog-theme-default', width:'70%' });
        //};

    }

});