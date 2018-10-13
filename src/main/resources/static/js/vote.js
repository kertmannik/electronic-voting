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
    setTimeout(function() {
        notificationMessage.remove();
    }, 5000);
}
 $(function () {
    notificationsContainer = $("#vote-notifications-container");
    connect();
 });