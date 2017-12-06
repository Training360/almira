angular.module("testApp").directive("indexDirective", function () {
    return {
        templateUrl: "./app/testApp/views/indexView.html",
        controller: "testController"
    };
});