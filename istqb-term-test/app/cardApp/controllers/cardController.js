angular.module("cardApp").controller("cardController", function ($scope, $http) {
    $scope.type = 'name';
    $scope.tempType = null;
    $scope.typeClassName = 'btn btn-lg active';
    $scope.typeClassDescription = 'btn btn-lg';

    $scope.setRandomData = function () {
        var randomIndex = Math.floor(Math.random() * $scope.jsonData.length);
        $scope.data = $scope.jsonData[randomIndex];
        if($scope.tempType){
            $scope.type = $scope.tempType;
            $scope.tempType = null;
        }
    };

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
        if ($scope.type == 'name'){
            $scope.typeClassName = 'btn btn-lg active';
            $scope.typeClassDescription = 'btn btn-lg';
        }
        else{
            $scope.typeClassName = 'btn btn-lg';
            $scope.typeClassDescription = 'btn btn-lg active';
        }
    };

    $scope.changeCard = function () {
        $scope.tempType =  $scope.type;
        $scope.type =  $scope.type == 'name' ?  'description' : 'name';
    }
});