var ajaxMealUrl = "ajax/profile/meals/"

function updateFilteredTable() {
    $.ajax({
        type: "GET",
        url: ajaxMealUrl + "filter",
        data: $("#filter").serialize()
    }).done(updateTableByData);
}

function clearFilter() {
    $("#filter")[0].reset();
    $.get(ajaxMealUrl, updateTableByData);
}

$(function () {
    makeEditable({
        ajaxUrl: ajaxMealUrl,
        datatableApi: $("#datatable").DataTable({
            "ajax": {
                "url": ajaxMealUrl,
                "dataSrc": ""
            },
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "dateTime",
                    "render": function (date, type, row) {
                        if (type === "display") {
                            return date.replace('T',' ').substring(0,16);//.substring(0, 16);
                        }
                        return date;
                    }
                },
                {
                    "data": "description"
                },
                {
                    "data": "calories"
                },
                {
                    "orderable": false,
                    "defaultContent": "",
                    "render": renderEditBtn
                },
                {
                    "orderable": false,
                    "defaultContent": "",
                    "render": renderDeleteBtn
                }
            ],
            "order": [
                [
                    0,
                    "desc"
                ]
            ],
            "createdRow": function (row, data, dataIndex) {
                $(row).attr("data-mealExcess", data.excess);

            }
        }),
        updateTable: updateFilteredTable
    });
});