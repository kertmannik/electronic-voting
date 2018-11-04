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
