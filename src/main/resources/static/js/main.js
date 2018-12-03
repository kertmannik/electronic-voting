//tooltip
$(function () {
    $('[data-toggle="tooltip"]').tooltip()
})

//userstatistics banner
$(window).on('load', function () {
    $('#banner').modal('show');
});

//Voting notification
/**
 * Example based on https://spring.io/guides/gs/messaging-stomp-websocket/
 */
var stompClient = null;
var notificationsContainer = null;

function connect() {
    var socket = new SockJS('/vote-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/votes', function (greeting) {
            try {
                var candidate = JSON.parse(greeting.body);
            } catch {
                var candidate = greeting.body;
            }
            console.log(candidate);
            showNotification(candidate);
        });
    });
}

function showNotification(candidate) {
    if (candidate === "Can not vote for yourself!" || candidate === "Enda poolt ei saa hääletada!") {
        var notificationMessage = $(
            "<div class=\"alert alert-danger\" align='center'>" +
            candidate + "</div>");
        notificationsContainer.append(notificationMessage);
        setTimeout(function () {
            notificationMessage.remove();
        }, 5000);
    } else {
        var notificationMessage = $(
            "<div class=\"alert alert-info\" align='center'>" +
            candidate.firstName + " " + candidate.lastName + " +1" + "</div>");
        notificationsContainer.append(notificationMessage);
        setTimeout(function () {
            notificationMessage.remove();
        }, 5000);
    }
}

$(function () {
    notificationsContainer = $("#vote-notifications-container");
    connect();
});

//vote-post
$(function () {
    $(".btn-block").click(function (e) {
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