/**
 * Created by Ivan on 5/25/2016.
 */
angular.module('templateapp').controller('DraftController', function($scope, $http, $interval, PoemDataService,DraftDelService,DraftExchangeService, ngDialog) {
    $scope.DraftData = [];
    $scope.DisplayedDraft = {};
    $scope.DisplayedDraft.content="";
    $scope.toDelete = 0;
    $scope.displayedGenre = "";
    this.showChgBtn = false;

    $scope.getDrafts = function() {
        DraftDelService.downloadDraftData($scope.renderDrafts);
    };
    $scope.renderDrafts = function(param){
        $scope.DraftData = param;
    };

    $scope.sendToEdit = function(){
        console.log($scope.DisplayedDraft);
        DraftExchangeService.setDraft($scope.DisplayedDraft);
    };

    this.showDraft = function(event) {
        elementId = event;
        $scope.DisplayedDraft = $scope.DraftData[elementId];
        $scope.DisplayedDraft.text = "";

        PoemDataService.setPoemType($scope.DraftData[elementId].genre);
        //$scope.DisplayedDraft.genre = PoemDataService.getPoemType();
        $scope.displayedGenre = PoemDataService.getPoemType();
        /*for (i=0;i<$scope.DisplayedDraft.content.length;i++){
            $scope.DisplayedDraft.text += $scope.DisplayedDraft.content[i] + '\n';
        }
        $scope.DisplayedDraft.text = stringContainingNewLines.replace(/(\r\n|\n|\r)/gm, "<br>");
        //$scope.DisplayedDraft.hashtags = $scope.DraftData[elementId].hashtags;*/
        this.showChgBtn = true;
    }

    $scope.PreDeleteDraft = function (id, index) {
        DraftDelService.setDeleteId(id);
        DraftDelService.setIndexId(index);
        //$scope.toDelete_index = index;
        /*console.log($scope.toDelete);
        console.log("BEFORE:"+ $scope.DraftData);*/

        //console.log("AFTER:"+ $scope.DraftData);
        ngDialog.open({ template: 'draftDeleteDialog.html',controller: 'DraftController', scope: $scope , className: 'ngdialog-theme-default', width:'30%' });
    }
    $scope.DeleteDraft = function() {
        token = localStorage["token"] || "";
        data = {"token":token, "id":DraftDelService.getDeleteId()};
        $http.post("http://localhost:8080/rest/drafts/delete", data)
            .then(function(response){
                respond = JSON.stringify(response.data);
                result = JSON.parse(respond);
                console.log(result);
                if (response.data.result=="OK") {
                    //delete $scope.DraftData[$scope.toDelete];
                    //console.log("DRAFTDATA:"+$scope.DraftData);
                    //console.log($scope.DraftData.length);
                    toTrim = DraftDelService.getDraftData();
                    toDelIndex = DraftDelService.getIndexId();
                    toTrim.splice(toDelIndex,1);
                    $scope.DraftData = toTrim;
                    DraftDelService.setDraftData($scope.DraftData);
                    Materialize.toast("Черновик был успешно удален!", 3500);
                    $scope.closeDialog();
                }
                if (response.data.result=="BAD") {
                    Materialize.toast("Не удалось удалить черновик", 3500);
                    $scope.closeDialog();
                }
            });
    }

    $scope.closeDialog = function() {
        ngDialog.close(draft_dialog, 0);
        $scope.DraftData = DraftDelService.downloadDraftData($scope.renderDrafts);
    }

    $scope.clickToOpen = function () {
        ngDialog.open({ template: 'poemchoose.html', className: 'ngdialog-theme-default', width:'70%' });
    };

});