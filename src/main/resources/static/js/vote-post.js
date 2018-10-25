$(function() {
    $("#error-text")[0].style.display = 'none';
    $(".btn-block").click(function(e) {
        $("#error-text")[0].style.display = 'none';
        e.preventDefault();
        console.log($(this).val())
        $.ajax({
            url: '/voting/vote',
            type: 'post',
            contentType: 'application/json',
            success: function (data) {
                console.log("Success");
                console.log(data);
            },
            error: function (data) {
                console.log("Failure");
                console.log(data);
                $("#error-text")[0].style.display = 'block';
            },
            data: JSON.stringify(getFormData($(this).val()))
        });
    })
});

function getFormData(candidateId) {
    var formDataAsJSON = {};
    formDataAsJSON["candidateId"] = candidateId;
    return formDataAsJSON;
}