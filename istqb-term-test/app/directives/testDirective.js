angular.module("testApp").directive("testDirective", function() {
  return {
    templateUrl: "./app/views/testView.html",
    controller: "testController"
  };
});
