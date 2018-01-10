angular.module("cardApp").controller("cardController", function ($scope, $http) {
    $scope.type = 'name';
    $scope.lang = 'hu';

    $scope.setRandomData = function () {
        var randomIndex = Math.floor(Math.random() * $scope.jsonData.length);
        $scope.data = $scope.jsonData[randomIndex];
        $scope.type = 'name';
    }

    $http.get("./json/questions.json").then(
        function (response) {
            $scope.jsonData = response.data.questions;
            $scope.setRandomData();
        },
        function (response) {
            $scope.errorMessage = $scope.translate("testError");
        }
    );

    $scope.setType = function (type) {
        $scope.type = type;
    }

    $scope.setLang = function (lang) {
        $scope.lang = lang;
    }

});