

angular.module('templateapp').controller('PoemFieldController', function($scope, $http, $interval, $location, PoemDataService, DraftExchangeService, ngDialog) {

		var ctrl = this;
		linescounter = 0;
		counter = 0;
		ctrl.displayedForms = [];
		ctrl.displayedInputs = [];
		// type of poem received from previous form
		$scope.type = "АБВ";
		// array of rhymes from server
		$scope.RhymeData = {};
		// lines of text in input fields
		$scope.lines = {};
		$scope.forms= {};
		$scope.fromDraft = {};
		$scope.usedDraft = false;



		$interval(getRhyme,3300);

		function getRhyme() {
			rhyme = $('#rhyme_field').val();
			storagetoken = localStorage['token'] || "";
			if ($scope.existingRhyme==rhyme || $scope.RhymeData=={} || $('#rhyme_field').val()==""){return;}
			values = {"rhyme": rhyme, "token": storagetoken};
			$http.post("http://localhost:8080/rest/rhymes/rhyme", values)
			.then(function(response) {
				raw = JSON.stringify(response.data);
                result = JSON.parse(raw);

                $scope.RhymeData = result.rhymes;
                $scope.existingRhyme = rhyme;
                
			});
		}

		$scope.prefillData = function(object) {
			draft = object;
			$("#firstline").val(draft.content[0]);
			$scope.type = draft.genre;
			for (i=1;i<draft.content.length;i++) {
				ctrl.displayedInputs.push("input.tpl.html");
				$scope.lines[i-1] = (draft.content[i]);
			}
			for (i=0;i<draft.hashtags.length;i++) {
				ctrl.displayedForms.push("form1.tpl.html");
				$scope.forms[i]=draft.hashtags[i];
			}
		};

		$scope.firstLaunch = function() {

			// если пришел из черновиков
			$scope.fromDraft = DraftExchangeService.getDraft();
			if (!$.isEmptyObject($scope.fromDraft)) {
				$scope.prefillData($scope.fromDraft);
				$scope.usedDraft = true;
			}
			// если пришел из начального экрана
			else {
				$scope.type = PoemDataService.getPoemType();
				$scope.existingRhyme = "";
				ngDialog.close();
				console.log("Type received from storage: " + $scope.type);
				$scope.usedDraft = false;
			}

		};

		$scope.saveDraft = function() {
			poemText = "";
			firstline = $('#firstline').val();
			poemText += firstline + "\n";
			for (var key in $scope.lines) {
				if ($scope.lines.hasOwnProperty(key)) {
					poemText += $scope.lines[key] + "\n";
				}
			}
			poemText.trim();
			//console.log(poemText);
			// Grab Tags into single line
			tags = "";
			for (var key in $scope.forms) {
				if ($scope.forms.hasOwnProperty(key)) {
					tags += $scope.forms[key] + " ";
				}
			}
			poemType = PoemDataService.getPoemType();;
			token = localStorage['token'] || "";
			draft = { "content": poemText, "genre": poemType, "hashtags": tags};
			toSend = {"token": token, "draft":draft};
			console.log(toSend);
			//toSend = JSON.stringify(toSend);
			//console.log(tags);
			$http.post("http://localhost:8080/rest/drafts/save", toSend)
				.then(function (response) {
					if (response.data.result=="OK") {
						Materialize.toast("Черновик был сохранен",2000);
						$location.path("/drafts");
					}
					else {
						Materialize.toast("Не удалось сохранить черновик",2000);
					}
				});
		}


		$scope.updateDraft = function() {
			poemText = "";
			firstline = $('#firstline').val();
			poemText += firstline + "\n";
			for (var key in $scope.lines) {
				if ($scope.lines.hasOwnProperty(key)) {
					poemText += $scope.lines[key] + "\n";
				}
			}
			poemText.trim();
			//console.log(poemText);
			// Grab Tags into single line
			tags = "";
			for (var key in $scope.forms) {
				if ($scope.forms.hasOwnProperty(key)) {
					tags += $scope.forms[key] + " ";
				}
			}
			poemType = $scope.fromDraft.genre;
			token = localStorage['token'] || "";
			draft = { "content": poemText, "genre": poemType, "hashtags": tags, "id":$scope.fromDraft.id};
			toSend = {"token": token, "draft":draft};
			console.log(toSend);
			//toSend = JSON.stringify(toSend);
			//console.log(tags);
			$http.post("http://localhost:8080/rest/drafts/update", toSend)
				.then(function (response) {
					if (response.data.result=="OK") {
						Materialize.toast("Черновик был обновлен",2000);
						$location.path("/drafts");
					}
					else {
						Materialize.toast("Не удалось обновить черновик",2000);
					}
				});
		}



		$scope.addTagField = function() {
			if (counter == 3) {
				return;
			}
			counter ++;
			ctrl.displayedForms.push("form1.tpl.html");

		};

		$scope.delTagField = function() {
			if (counter==0) {
				return;
			}
			counter--;
			delete $scope.forms[counter];
			ctrl.displayedForms.pop();
		};
		
    	$scope.addInputField = function(event) {
    		if (event.keyCode == 13) {
			ctrl.displayedInputs.push("input.tpl.html");
    		//$('input[name=line'+linescounter+']').focus();
    		/*el = $('#line'+linescounter).get(0);
    		elemLen = el.value.length;  
    		el.selectionStart = elemLen;
   			//el.selectionEnd = elemLen;
    		el.focus();
    		linescounter++;*/

    		}
		};



		$scope.sendPoem = function() {
			// Grab poemtext into single line
			poemText = "";
			firstline = $('#firstline').val();
			poemText+=firstline+"\n";
			for (var key in $scope.lines) {
				if ($scope.lines.hasOwnProperty(key)) {
				poemText+= $scope.lines[key]+"\n";
				}
			}
			console.log(poemText);
			// Grab Tags into single line
			tags = "";
			for (var key in $scope.forms) {
				if ($scope.forms.hasOwnProperty(key)) {
				tags += $scope.forms[key]+" ";
				}
			}
			console.log(tags);

			// Take token
			token = localStorage['token'] || "";
			// Take poem type
			poemType = PoemDataService.getPoemType();
			// Collect all in one
			toSend = {"token":token, "poem":poemText, "genre":poemType, "tags":tags};
			toSend = JSON.stringify(toSend);
			console.log(toSend);
			

			$http.post("http://localhost:8080/rest/poem/add", toSend)
			.then(function(response) {
				//raw = JSON.stringify(response.data);
                //result = JSON.parse(raw);
                if (response.data.result=="OK") {
                	Materialize.toast('Стих отправлен успешно!', 4500);
                }
                console.log(response);
                //console.log(result);
                
			});
		}

	$scope.PreSaveDraft = function (id) {
		toDelete = id;
		console.log(toDelete);

	}

	});

