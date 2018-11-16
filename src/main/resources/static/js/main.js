//tooltip
$(function () {
    $('[data-toggle="tooltip"]').tooltip()
})

//userstatistics banner
$(window).on('load', function () {
    $('#banner').modal('show');
});

//statistics chart
// Load google charts
google.charts.load('current', {'packages': ['corechart']});
google.charts.setOnLoadCallback(drawChart);

// Draw the chart and set the chart values
function drawChart() {
    var myTableArray = [];
    $("table#votesForPartyTable tr").each(function () {
        var arrayOfThisRow = [];
        var tableData = $(this).find('td');
        if (tableData.length > 0) {
            tableData.each(function () {
                arrayOfThisRow.push($(this).text());
            });
            myTableArray.push(arrayOfThisRow);
        }
    });

    var arrayLength = myTableArray.length;
    var arrayForChart = [];
    arrayForChart.push(['Party', 'Votes']);
    for (var i = 0; i < arrayLength; i++) {
        var row = [];
        for (var j = 0; j < myTableArray[i].length; j++) {
            if (j === 1) {
                row.push(Number(myTableArray[i][j]));
            } else {
                row.push(myTableArray[i][j]);
            }
        }
        arrayForChart.push(row);
    }

    var chartwidth = $('#chartparent').width();
    var data = google.visualization.arrayToDataTable(arrayForChart);
    var options = {'backgroundColor': 'transparent'};
    var chart = new google.visualization.PieChart(document.getElementById('piechart'));
    chart.draw(data, options);
}

$(window).resize(function () {
    drawChart();
});

//homepage table filter
$(document).ready(function () {
    var selectedOption = document.getElementById("selected-language").innerHTML;
    if (selectedOption.trim() === "et") {
        $('#candidateTable').DataTable({
            "info": false,
            "columnDefs": [
                {"orderable": false, "targets": 5}
            ],
            "language": {"url": "https://cdn.datatables.net/plug-ins/1.10.19/i18n/Estonian.json"}
        });
    } else {
        $('#candidateTable').DataTable({
            "info": false,
            "columnDefs": [
                {"orderable": false, "targets": 5}
            ]
        });
    }
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

//statistics table filter
$(document).ready(function () {
    var selectedOption = document.getElementById("selected-language").innerHTML;
    if (selectedOption.trim() === "et") {
        $('#votesTable').DataTable({
            "info": false,
            "language": {"url": "https://cdn.datatables.net/plug-ins/1.10.19/i18n/Estonian.json"}
        });
    } else {
        $('#votesTable').DataTable({
            "info": false
        });
    }
});

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