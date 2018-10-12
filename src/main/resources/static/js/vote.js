/**
 * Example based on https://spring.io/guides/gs/messaging-stomp-websocket/
 */
 var stompClient = null;
var notificationsContainer = null;
 function connect() {
    var socket = new SockJS('/my-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/subscriptions', function (greeting) {
            var subscription = JSON.parse(greeting.body);
            console.log(subscription);
            showNotification(subscription);
        });
    });
}
 function showNotification(subscription) {
    var notificationMessage = $(
        "<div class=\"alert alert-info\"><strong>Info</strong><br>" +
            subscription.name.toUpperCase() + " subscribed with e-mail: " + subscription.email +
        "</div>");
    notificationsContainer.append(notificationMessage);
     setTimeout(function() {
        notificationMessage.remove();
    }, 3000);
}
 $(function () {
    notificationsContainer = $("#subscription-notifications-container");
     // On page load, connect to websocket
    connect();
 });