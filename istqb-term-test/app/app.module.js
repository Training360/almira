var app = angular.module("testApp", []);

app.directive("indexDirective", function() {
  return {
    templateUrl: "./app/views/indexDirective.html",
    controller: "testController"
  };
});

app.directive("testDirective", function() {
  return {
    templateUrl: "./app/views/testDirective.html",
    controller: "testController"
  };
});

app.controller("languageController", function($scope, $http) {
  $scope.language = "hu";

  $http.get("./json/translation.json").then(
    function(response) {
      $scope.dictionary = response.data.dictionary;
      $scope.translate = function(data) {
        return $scope.dictionary[data][$scope.language];
      };
    },
    function(response) {
      alert("Hiba a nyelvesítés betöltésekor!");
    }
  );

  $scope.setLanguage = function(lang) {
    $scope.language = lang;
    $scope.started = true;
  };
});

app.controller("testController", function($scope, $http) {
  // LANGUAGES

  $scope.started = false;
  $scope.numberOfQuestions = 20;
  $scope.rightAnswer = 0;
  $scope.actualTestIndex = 0;
  $scope.testQuestions = [];
  $scope.userAnswer;
  $http.get("./json/questions.json").then(
    function(response) {
      $scope.jsonData = response.data.questions;
    },
    function(response) {
      alert("Hiba a kérdéssor elérésében!");
    }
  );

  function randomizeElements(elements) {
    return elements.sort(() => Math.random() * 2 - 1);
  }

  function generateRandomNumbers(count, maxNumber) {
    var randomNumbers = [];
    var aNumber;
    var i = 0;
    while (i < count) {
      aNumber = Math.floor(Math.random() * maxNumber);
      if (randomNumbers.indexOf(aNumber) == -1) {
        randomNumbers.push(aNumber);
        i++;
      }
    }
    return randomNumbers;
  }

  function generateQuestionType() {
    return Math.random() >= 0.5 ? "name" : "description";
  }

  function setQuestion(type, index) {
    if (type.indexOf("name") != -1) {
      question = $scope.jsonData[index][$scope.language + "Description"];
    } else {
      question = $scope.jsonData[index][$scope.language + "Name"];
    }
    return question;
  }

  function setRandomAnswers(type, answerIndexes) {
    var i = 0;
    var answers = [];

    for (i = 0; i < answerIndexes.length; i++) {
      if (type.indexOf("name") != -1) {
        answers.push(
          $scope.jsonData[answerIndexes[i]][$scope.language + "Name"]
        );
      } else {
        answers.push(
          $scope.jsonData[answerIndexes[i]][$scope.language + "Description"]
        );
      }
    }
    return answers;
  }

  function generateQuestions() {
    var i = 0;
    var aQuestion = {};
    var answerIndexes;

    while (i < $scope.numberOfQuestions) {
      answerIndexes = generateRandomNumbers(4, $scope.jsonData.length);
      aQuestion = {};
      aQuestion.type = generateQuestionType();
      aQuestion.question = setQuestion(aQuestion.type, answerIndexes[0]);
      aQuestion.answers = setRandomAnswers(aQuestion.type, answerIndexes);
      aQuestion.rightAnswer = aQuestion.answers[0];
      aQuestion.userAnswer = "";
      aQuestion.isCorrent = false;
      aQuestion.answers = randomizeElements(aQuestion.answers);
      $scope.testQuestions.push(aQuestion);
      i++;
    }
  }

  $scope.setUserAnswer = function(userAnswer) {
    $scope.testQuestions[$scope.actualTestIndex].userAnswer = userAnswer;
  };

  $scope.checkAnswer = function() {
    if (
      $scope.testQuestions[$scope.actualTestIndex].userAnswer ==
      $scope.testQuestions[$scope.actualTestIndex].rightAnswer
    ) {
      $scope.testQuestions[$scope.actualTestIndex].correct = true;
      $scope.rightAnswer++;
    } else {
      $scope.testQuestions[$scope.actualTestIndex].correct = false;
    }
    $scope.actualTestIndex++;
  };
});

/*
                    function capitalizeFirstLetter(string) {
                        console.log(string.charAt(0).toUpperCase() + string.slice(1));
                        return string.charAt(0).toUpperCase() + string.slice(1);
                    }

                    $scope.getJsonData('./json/concated.json');
                    
                    $scope.generateNewJsonFile = function () {
                        var huJsonData = $scope.jsonData.hu;
                        var enJsonData = $scope.jsonData.en;
                        var newJsonFile = {
                            questions: []
                        };
                        var huObjectIndex;
                        var i, j, k = 0;
                        console.log(huJsonData[0].engNames);
                        for (i = 0; i < enJsonData.length; i++) {
                            for (j = 0; j < huJsonData.length; j++) {
                                console.log(huJsonData[j]);
                                if (huJsonData[j].engNames.indexOf(enJsonData[i].name) != -1) {
                                    huObjectIndex = j;
                                    break;
                                }
                            }

                            if (Array.isArray(enJsonData[i].ref)) {
                                for (k = 0; k < enJsonData[i].ref.length; k++) {
                                    enJsonData[i].ref[k] = capitalizeFirstLetter(enJsonData[i].ref[k]);
                                }
                            }

                            if (Array.isArray(enJsonData[i].seeAlso)) {
                                for (k = 0; k < enJsonData[i].seeAlso.length; k++) {
                                    enJsonData[i].seeAlso[k] = capitalizeFirstLetter(enJsonData[i].seeAlso[
                                        k]);
                                }
                            }

                            if (Array.isArray(enJsonData[i].synonyms)) {
                                for (k = 0; k < enJsonData[i].synonyms.length; k++) {
                                    enJsonData[i].synonyms[k] = capitalizeFirstLetter(enJsonData[i].synonyms[
                                        k]);
                                }
                            }

                            newJsonFile.questions.push({
                               "huName": capitalizeFirstLetter(huJsonData[huObjectIndex].name),
                               "enName": capitalizeFirstLetter(enJsonData[i].name),
                               "huDescription": capitalizeFirstLetter(huJsonData[huObjectIndex].desc),
                               "enDescription": capitalizeFirstLetter(enJsonData[i].desc),
                               "reference": enJsonData[i].ref,
                               "seeAlso": enJsonData[i].seeAlso,
                               "synonyms": enJsonData[i].synonyms
                            });
                        }
                        $scope.jsonData = JSON.stringify(newJsonFile);
                        console.log(JSON.stringify(newJsonFile));
                    }*/
