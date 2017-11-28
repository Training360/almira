app.factory("jsonFactory", [
  "$http",
  function($http) {
    $http({
      method: "GET",
      headers: {
        "Content-Type": "application/json"
      },
      url: path
    }).then(
      function(response) {
        return response.data;
      },
      function(response) {
        $scope.jsonError = $scope.translate("jsonError");
      }
    );
  }
]);
