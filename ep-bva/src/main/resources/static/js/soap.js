$(function() {

    updateWebservicesLink();

    initSendButton();
});

function updateWebservicesLink() {
    let base = window.location.href;
    let address = base.substring(0, base.lastIndexOf("/")) + "/services";
    link = document.getElementById("webservices-link")
    link.href = address;
    link.innerHTML = address;
}

function initSendButton() {
    $("#send-button").click(function() {
        console.log("Send SOAP request");
        $.ajax({
            url: "services/triangle",
            type : "POST",
            dataType: 'text',
            contentType: "application/xml; charset=UTF-8",
            data : $("#soap-request-code").text(),
            success : function(result) {
                console.log(result);
                $("#soap-response-code").text(vkbeautify.xml(result));
            }
        });
    });
}