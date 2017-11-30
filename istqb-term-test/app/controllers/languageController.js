angular
  .module("testApp")
  .controller("languageController", function($scope, $http) {
    $scope.language = "hu";

    $scope.languageIsSelected = false;

    $http.get("./json/translation.json").then(
      function(response) {
        $scope.dictionary = response.data.dictionary;
        $scope.translate = function(data) {
          return $scope.dictionary[data][$scope.language];
        };
      },
      function(response) {
        $scope.errorMessage = $scope.translate("translationError");
      }
    );

    $scope.setLanguage = function(lang) {
      $scope.language = lang;
      $scope.languageIsSelected = true;
    };
  });
