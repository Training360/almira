$(function() {
    console.log("Init");

    $("#triangle-form").submit(function(event) {
        console.log("Triangle submit");

        var a = $("#a-input").val();
        var b = $("#b-input").val();
        var c = $("#c-input").val();

        var data = {
            'a' : a,
            'b' : b,
            'c' : c
        };

        $.ajax({
            url: "api/triangle",
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
                    $("#triangle-message-div").html(s);
                }
                if (result.type) {
                    var s = "<div class=\"alert alert-success\" role=\"alert\">";
                    if (result.type === "INVALID") {
                        s += "Nem háromszög";
                    }
                    if (result.type === "EQUILATERAL") {
                        s += "Egyenlő oldalú háromszög";
                    }
                    if (result.type === "ISOSCELES") {
                        s += "Egyelő szárú háromszög";
                    }
                    if (result.type === "SCALENE") {
                        s += "Általános háromszög";
                    }
                    s += "</div>";
                    $("#triangle-message-div").html(s);
                }
            },
            error: function(xhr, resp, text) {
                console.log(xhr, resp, text);
            }
        });
        return false;
    });

});