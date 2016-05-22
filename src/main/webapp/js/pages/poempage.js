

angular.module('templateapp').controller('PoemFieldController', function($scope, $http, $interval, PoemDataService, ngDialog) {

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

		$scope.firstLaunch = function() {
			
			$scope.type = PoemDataService.getPoemType();
			$scope.existingRhyme = "";
			ngDialog.close();
			console.log("Type received from storage: " + $scope.type);

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

	});

