

angular.module('templateapp').controller('PoemFieldController', function($scope, $http, $interval, $location, ValidateService, PoemDataService, DraftExchangeService, ngDialog) {

		var ctrl = this;
		linescounter = 0;
		counter = 0;
		tagCounter = 0;
		ctrl.displayedForms = [];
		ctrl.displayedInputs = [];
		//rules of writing depending on poem type
		$scope.rules = {};
		// type of poem received from previous form
		$scope.type = "АБВ";
		// array of rhymes from server
		$scope.RhymeData = {};
		$scope.firstline = "";
		// lines of text in input fields
		$scope.lines = {};
		$scope.lines.count=0;
		$scope.forms= {};
		$scope.linesConfirm = [];
		$scope.fromDraft = {};
		$scope.usedDraft = false;
		// determine whether or not display first letters of acro tele poem
		$scope.acrotrue = false;
		$scope.Acroword = "";



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
			$scope.firstline= draft.content[0];
			$scope.type = draft.genre;
			for (i=1;i<draft.content.length;i++) {
				ctrl.displayedInputs.push("input.tpl.html");
				//$("#line"+i-1).val(draft.content[i]);
				var innerindex = (i-1).toString();
				$("#line"+innerindex).val(draft.content[i]);
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
				$scope.linesConfirm.push(false);
				$scope.usedDraft = true;
				$scope.type = PoemDataService.getPoemType();
				$scope.rules = ValidateService.getRules($scope.type);
				$scope.lines.count=$scope.fromDraft.content.length;

			}
			// если пришел из начального экрана
			else {
				$scope.type = PoemDataService.getPoemType();
				$scope.existingRhyme = "";
				$scope.linesConfirm.push(false);
				ngDialog.close();
				console.log("Type received from storage: " + $scope.type);
				$scope.usedDraft = false;

				$scope.rules = ValidateService.getRules($scope.type);
			}

		};

		$scope.$watch('firstline', function() {
			if ($scope.type=="Акростих" || $scope.type=="Телестих") {
				$scope.acrotrue = true;
				$scope.firstLetter = [];
				if ($scope.type=="Акростих") {
					$scope.firstLetter=$scope.firstline[0];
					$scope.Acroword = $scope.firstLetter;
				}
				else {
					$scope.firstLetter=$scope.firstline[$scope.firstline.length-1];
					$scope.Acroword = $scope.firstLetter;;
				}
			}
			if ($scope.rules.vowelPattern.length>0) {
				firstIndex = $scope.rules.vowelPattern[0];
				if ($scope.type=="Пирожок" || $scope.type=="Порошок") {
					if ($("#firstline").val().length > 0) {
						firstLinecounter = 0;
						var text = $("#firstline").val();
						for (i = 0; i < text.length; i++) {
							if (text[i] == 'а' || text[i] == 'о' || text[i] == 'у' || text[i] == 'э' ||
								text[i] == 'ы' || text[i] == 'е' || text[i] == 'ё' || text[i] == 'ю' ||
								text[i] == 'я' || text[i] == 'и') {
								firstLinecounter++;
							}
						}
						if (firstLinecounter == $scope.rules.vowelPattern[0]) {
							$scope.linesConfirm[0] = true;
						}
						else {
							$scope.linesConfirm[0] = false;
						}
					}
				}

			}
		},true);

		$scope.$watch('lines', function(){
			if ($scope.lines.count>0) {
				if ($scope.type=="Акростих" || $scope.type=="Телестих") {
					$scope.acrotrue = true;
					$scope.acroLetters=[];
					$scope.Acroword = $scope.firstLetter.toString();
					for (var key in $scope.lines) {
						console.log($scope.lines);
						if ($scope.lines.hasOwnProperty(key)) {
							if ($scope.type=="Акростих" && key!="count") {
								$scope.acroLetters[parseInt(key)]= $scope.lines[key][0];
							}
							if ($scope.type=="Телестих" && key!="count") {
								$scope.acroLetters[parseInt(key)]= $scope.lines[key][$scope.lines[key].length-1];
							}
						}
					}
					for (i=0;i<$scope.acroLetters.length;i++) {
						if ($scope.acroLetters[i]==undefined) {
							$scope.Acroword+="";
						}
						else {
							$scope.Acroword += $scope.acroLetters[i];
						}
					}
				}
				if ($scope.rules.vowelPattern.length>0) {
					if ($scope.type=="Пирожок" || $scope.type=="Порошок") {
						for (var key in $scope.lines) {
							console.log($scope.lines);
							if ($scope.lines.hasOwnProperty(key)) {
								if ($scope.lines[key] == "" || key == "count" || $scope.lines[key] == "\n") {
									continue;
								}

								if (countVowels(key)) {
									$scope.linesConfirm[parseInt(key) + 1] = true;
								}
								else {
									$scope.linesConfirm[parseInt(key) + 1] = false;
								}
							}
						}
					}

				}	
			}
		}, true);

		var countVowels = function(num) {
			index = num;
			if ( $('#line'+index).length ) {
				text = $('#line'+index).val();
				counter = 0;
				for (i=0;i<text.length;i++) {
					if (text[i]=='а'||text[i]=='о'||text[i]=='у'||text[i]=='э'||
						text[i]=='ы'||text[i]=='е'||text[i]=='ё'||text[i]=='ю'||
						text[i]=='я'||text[i]=='и') {
						counter++;
					}
				}
				if (counter==$scope.rules.vowelPattern[parseInt(num)+1]) {
					return true;
				}
				else {return false;}
			}
		}


		$scope.saveDraft = function() {
			poemText = "";
			firstline = $('#firstline').val();
			poemText += firstline + "\n";
			poemText = makeItCultural(poemText);
			for (var key in $scope.lines) {
				if ($scope.lines.hasOwnProperty(key)) {
					if ($scope.lines[key]=="" || key=="count" || $scope.lines[key]=="\n") {continue;}

					text = makeItCultural($scope.lines[key]);
					
					poemText+= text+"\n";
				
				}
			}
			poemText = poemText.slice(0, -1);
			console.log(poemText);
			//console.log(poemText);
			// Grab Tags into single line
			tags = "";
			for (var key in $scope.forms) {
				if ($scope.forms.hasOwnProperty(key)) {
					tags += $scope.forms[key] + " ";
				}
			}
			poemType = PoemDataService.revertPoemType($scope.type);
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
			if (firstline=="") {
				firstline += " ";
			}
			else {
			poemText += firstline + "\n";
			poemText = makeItCultural(poemText);
			}
			for (var key in $scope.lines) {
				if ($scope.lines.hasOwnProperty(key)) {
					if ($scope.lines[key]==""|| key=="count" || $scope.lines[key]=="\n") {continue;}
					if ($scope.lines[key]=="!новаястрофа!") {poemText+= '          \n'; continue;} // 10 пробелов
					text = makeItCultural($scope.lines[key]);
					poemText+= text+"\n";
				}
			}
			// remove the last \n symbol
			poemText = poemText.slice(0, -1);
			console.log(poemText);
			//console.log(poemText);
			// Grab Tags into single line
			tags = "";
			for (var key in $scope.forms) {
				if ($scope.forms.hasOwnProperty(key)) {
					tags += $scope.forms[key] + " ";
				}
			}
			poemType = PoemDataService.revertPoemType($scope.type);
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
			if (tagCounter == 3) {
				return;
			}
			tagCounter ++;
			ctrl.displayedForms.push("form1.tpl.html");

		};

		$scope.delTagField = function() {
			if (tagCounter==0) {
				return;
			}
			tagCounter--;
			delete $scope.forms[tagCounter];
			ctrl.displayedForms.pop();
		};
		
    	$scope.addInputField = function(event) {
    		if (event.keyCode == 13) {
				if (ctrl.displayedInputs.length+1 >= $scope.rules.linesCount) {
					Materialize.toast("Больше строк добавлять нельзя!", 3500);
					return;
				}
				ctrl.displayedInputs.push("input.tpl.html");
				$scope.linesConfirm.push(false);
				$scope.lines.count++;
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
					if ($scope.lines[key]=="" || key=="count" || $scope.lines[key]=="\n") {continue;}
					if ($scope.lines[key]=="!новаястрофа!" || ($scope.lines[key].indexOf("!новаястрофа!") > -1))
					{poemText+= '          \n'; continue;} // 10 пробелов
					poemText+= text+"\n";
				}
			}
			poemText = poemText.slice(0, -1);
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
			// Resolve poem type correctly
			poemType = PoemDataService.revertPoemType($scope.type);
			// Collect all in one
			if ($scope.usedDraft==true) {
				toSend = {"token":token, "poem":poemText,
					"genre":poemType, "tags":tags,
					"usedDraft":true, "id": $scope.fromDraft.id};
			}
			else {
				toSend = {"token":token, "poem":poemText,
					"genre":poemType, "tags":tags,
					"usedDraft":false, "draftId": ""};
			}
			toSend = JSON.stringify(toSend);
			console.log(toSend);
			

			$http.post("http://localhost:8080/rest/poem/add", toSend)
			.then(function(response) {
				//raw = JSON.stringify(response.data);
                //result = JSON.parse(raw);
                if (response.data.result=="OK") {
                	Materialize.toast('Стих отправлен успешно!', 4500);
					$location.path("/");
                }
                console.log(response);
                //console.log(result);
                
			});
		}

	$scope.PreSaveDraft = function (id) {
		toDelete = id;
		console.log(toDelete);

	}

	$scope.askGeo = function() {
		if (navigator.geolocation) {
			navigator.geolocation.getCurrentPosition(showPosition, showError);
		} else {
			x.innerHTML = "Geolocation is not supported by this browser.";
		}
	}
	function showPosition(position) {
		console.log( position.coords.latitude + "   " +
			 position.coords.longitude);
	}
	function showError(error) {
		switch(error.code) {
			case error.PERMISSION_DENIED:
				console.log("User denied the request for Geolocation.");
				break;
			case error.POSITION_UNAVAILABLE:
				console.log("Location information is unavailable.");
				break;
			case error.TIMEOUT:
				console.log(x.innerHTML = "The request to get user location timed out.");
				break;
			case error.UNKNOWN_ERROR:
				console.log(x.innerHTML = "An unknown error occurred.");
				break;
		}
	}

	});

