//Sain inspriratsiooni eelmise aasta projektist, aga tegin palju ümber
function initMap() {
    var myCenter = new google.maps.LatLng(58.378243, 26.714534);
    var mapCanvas = document.getElementById("map");
    var mapOptions = {center: myCenter, zoom: 6};
    var map = new google.maps.Map(mapCanvas, mapOptions);
    var marker = new google.maps.Marker({
        position:myCenter,
        icon: 'images/map.png',
        map: map,
        animation: google.maps.Animation.DROP
    });
    marker.setMap(map);
    console.log("Map created!");

    //Listener for click event, zooming and setting center
    google.maps.event.addListener(marker,'click',function() {
        map.setZoom(14);
        map.setCenter(marker.getPosition());
    });
}