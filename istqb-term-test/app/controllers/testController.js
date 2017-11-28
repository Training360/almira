angular.module("testApp").controller("testController", function($scope, $http) {
  $scope.init = function() {
    $scope.started = false;
    $scope.numberOfQuestions = 20;
    $scope.rightAnswer = 0;
    $scope.currentForm;
    $scope.colorized;
    $scope.actualTestIndex = 0;
    $scope.testQuestions = [];
  };
  $scope.init();

  $http.get("./json/questions.json").then(
    function(response) {
      $scope.jsonData = response.data.questions;
    },
    function(response) {
      $scope.errorMessage = $scope.translate("testError");
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

  $scope.generateQuestions = function() {
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
    $scope.started = true;
  };

  $scope.setUserAnswer = function(answer, parentId, id) {
    var formId = "form" + parentId;
    $scope.colorized = "test" + parentId + id + "label";
    $scope.currectForm = document.forms[formId];
    $scope.testQuestions[$scope.actualTestIndex].userAnswer = answer;
  };

  function disableFormElements() {
    var elements = $scope.currectForm.elements;
    var i;
    for (i = 0; i < elements.length; i++) {
      if (elements[i].type == "radio") {
        elements[i].disabled = true;
      }
    }
  }

  $scope.checkAnswer = function(e, form) {
    e.preventDefault();
    disableFormElements();
    if (
      $scope.testQuestions[$scope.actualTestIndex].userAnswer ==
      $scope.testQuestions[$scope.actualTestIndex].rightAnswer
    ) {
      $scope.testQuestions[$scope.actualTestIndex].correct = true;
      document.getElementById($scope.colorized).className += " success-color";
      $scope.rightAnswer++;
    } else {
      $scope.testQuestions[$scope.actualTestIndex].correct = false;
      document.getElementById($scope.colorized).className += " error-color";
      $scope.currectForm.children[1].innerHTML =
        $scope.testQuestions[$scope.actualTestIndex].rightAnswer;
    }
  };

  $scope.previousQuestion = function() {
    $scope.actualTestIndex--;
  };

  $scope.nextQuestion = function() {
    $scope.actualTestIndex++;
  };
});
