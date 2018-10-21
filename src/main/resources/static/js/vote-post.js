$(function() {
    $(".btn-block").click(function(e) {
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