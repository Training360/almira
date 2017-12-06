angular.module("testApp").directive("testDirective", function () {
    return {
        templateUrl: "./app/testApp/views/testView.html",
        controller: "testController"
    };
});