angular.module("cardApp").directive("indexDirective", function () {
    return {
        templateUrl: "./app/cardApp/views/indexView.html",
        controller: "cardController"
    };
});