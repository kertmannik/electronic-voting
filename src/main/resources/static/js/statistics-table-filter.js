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