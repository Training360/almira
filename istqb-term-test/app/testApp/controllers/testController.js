angular.module("testApp").controller("testController", function ($scope, $http) {
    $scope.init = function () {
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
        function (response) {
            $scope.jsonData = response.data.questions;
        },
        function (response) {
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

    function generateQuestion(answerIndexes) {
        var aQuestion = {};
        aQuestion.type = generateQuestionType();
        aQuestion.question = setQuestion(aQuestion.type, answerIndexes[0]);
        aQuestion.answers = setRandomAnswers(aQuestion.type, answerIndexes);
        aQuestion.rightAnswer = aQuestion.answers[0];
        aQuestion.userAnswer = "";
        aQuestion.isCorrect = null;
        return aQuestion;
    }

    $scope.generateQuestions = function () {
        var i = 0;
        var aQuestion = {};
        var answerIndexes;
        while (i < $scope.numberOfQuestions) {
            answerIndexes = generateRandomNumbers(4, $scope.jsonData.length);
            aQuestion = generateQuestion(answerIndexes);
            aQuestion.answers = randomizeElements(aQuestion.answers);
            $scope.testQuestions.push(aQuestion);
            i++;
        }
        $scope.started = true;
    };

    $scope.setUserAnswer = function (answer, parentId, id) {
        var formId = "form" + parentId;
        $scope.colorized = "test" + parentId + id + "label";
        $scope.currectForm = document.forms[formId];
        $scope.testQuestions[parentId].userAnswer = answer;
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

    function getRightAnswerIndex(parentId) {
        var formId = "form" + parentId;
        for (var i = 0; i < document.getElementById(formId).elements.length; i++) {
            if (document.getElementById(formId).elements[i].value == $scope.testQuestions[parentId].rightAnswer) {
                return i;
            }
        }
    }

    $scope.checkAnswer = function (e, parentId, id) {
        if ($scope.testQuestions[parentId].userAnswer == "") {
            if ($scope.language == 'hu') {
                alert('Először jelölj be egy válaszlehetőséget!');
            }
            else {
                alert('Please select an answer!');
            }
            return;
        }

        if ($scope.testQuestions[parentId].isCorrect == null && $scope.testQuestions[parentId].userAnswer != "") {
            disableFormElements();
            var radioButtonId = getRightAnswerIndex(parentId);
            var rightAnswerId = "test" + parentId + radioButtonId + "label";
            if ($scope.testQuestions[parentId].userAnswer == $scope.testQuestions[parentId].rightAnswer) {
                $scope.testQuestions[parentId].isCorrect = true;
                document.getElementById($scope.colorized).className += " success-color";
                $scope.rightAnswer++;
            } else {
                $scope.testQuestions[parentId].isCorrect = false;
                document.getElementById($scope.colorized).className += " error-color";
                document.getElementById(rightAnswerId).className += " success-color";
            }
        }
    };

    $scope.previousQuestion = function () {
        $scope.actualTestIndex -= 1;
    };

    $scope.nextQuestion = function () {
        $scope.actualTestIndex += 1;
    };
});