$(function() {
    console.log("Init");

    $("#credit-assessment-form").submit(function(event) {
        console.log("Credit assessment submit");

        var mortgage = $("#mortgage-input").val();
        var valueOfTheProperty = $("#value-of-the-property-input").val();

        var data = {
            'mortgage' : mortgage,
            'valueOfTheProperty' : valueOfTheProperty,
        };


        $.ajax({
            url: "api/credit-assessment",
            type : "POST",
            dataType: 'json',
            contentType: "application/json; charset=UTF-8",
            data : JSON.stringify(data),
            success : function(result) {
                console.log(result);
                if (result.errors) {
                    var s = "<div class=\"alert alert-danger\" role=\"alert\">";
                    for (i in result.errors) {
                        s += result.errors[i] + " ";
                    }
                    s += "</div>";
                    $("#credit-assessment-message-div").html(s);
                }
                if (result.workflowType) {
                    var s = "<div class=\"alert alert-success\" role=\"alert\">";
                    if (result.workflowType === "NORMAL") {
                        s += "Következő lépés";
                    }
                    if (result.workflowType === "SENIOR") {
                        s += "Szenior üzletkötő bevonása";
                    }
                    s += "</div>";
                    $("#credit-assessment-message-div").html(s);
                }
            },
            error: function(xhr, resp, text) {
                console.log(xhr, resp, text);
            }
        });
        return false;
    });

});