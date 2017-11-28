angular.module("testApp").directive("indexDirective", function() {
  return {
    templateUrl: "./app/views/indexView.html",
    controller: "testController"
  };
});
