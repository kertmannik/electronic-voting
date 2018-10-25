$(function() {
    $("#main-text")[0].style.display = 'none';
    $(".btn-block").click(function(e) {
        $("#main-text")[0].style.display = 'none';
        e.preventDefault();
        $.ajax({
            url: '/smart-id/authentication/start',
            type: 'post',
            dataType: 'json',
            contentType: 'application/json',
            success: function (data) {
                toggleContainerView(data.code);
                console.log("Success");
                console.log(data);
                pollResult();
            },
            error: function (data) {
                console.log("Failure");
                console.log(data);
                toggleContainerView("XXXX");
            },
            data: JSON.stringify(getFormData())
        });
    })
});
function pollResult() {
    $.ajax({
        url: '/smart-id/authentication/poll',
        type: 'post',
        dataType: 'json',
        contentType: 'application/json',
        success: function (data) {
            console.log("Success");
            console.log(data);
            console.log("redirect here");
            window.location.href = "/";
        },
        error: function (data) {
            console.log("Failure");
            console.log(data.responseJSON);
            $("#main-text")[0].style.display = 'block';
            $("#main-text").text(data.responseJSON.errorMessage);
            toggleContainerView("XXXX");
        }
    });
}
function getFormData() {
    var formDataAsJSON = {};
    $.each($("form").serializeArray(), function(index, value) {
        formDataAsJSON[value.name] = value.value;
    });
    formDataAsJSON["countryCode"] = "EE";
    return formDataAsJSON;
}
function toggleContainerView(code) {
    $("#verification-code-text").text(code);
    $("#smart-id-verification-code-container").toggle();
    $("#smart-id-login-container").toggle();
}