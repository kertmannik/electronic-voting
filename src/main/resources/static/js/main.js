//tooltip
$(function () {
    $('[data-toggle="tooltip"]').tooltip()
})

//userstatistics banner
$(window).on('load', function () {
    $('#banner').modal('show');
});

//map
//Sain inspriratsiooni eelmise aasta projektist, aga tegin palju ümber
function initMap() {
    var myCenter = new google.maps.LatLng(58.378243, 26.714534);
    var mapCanvas = document.getElementById("map");
    var mapOptions = {center: myCenter, zoom: 10};
    var map = new google.maps.Map(mapCanvas, mapOptions);
    var marker = new google.maps.Marker({
        position: myCenter,
        icon: 'images/map.png',
        map: map,
        animation: google.maps.Animation.DROP
    });
    marker.setMap(map);

    //Listener for click event, zooming and setting center
    google.maps.event.addListener(marker, 'click', function () {
        map.setZoom(16);
        map.setCenter(marker.getPosition());
    });
}

//voting
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
            var candidate = JSON.parse(greeting.body);
            console.log(candidate);
            showNotification(candidate);
        });
    });
}

function showNotification(candidate) {
    var notificationMessage = $(
        "<div class=\"alert alert-info\" align='center'>" +
        candidate.firstName + " " + candidate.lastName + " +1" + "</div>");
    notificationsContainer.append(notificationMessage);
    setTimeout(function () {
        notificationMessage.remove();
    }, 5000);
}

$(function () {
    notificationsContainer = $("#vote-notifications-container");
    connect();
});

//vote-post
$(function () {
    $("#error-text")[0].style.display = 'none';
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