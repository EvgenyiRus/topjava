var mealUrl = "ajax/profile/meals/"
var mealUrlFilter = "ajax/profile/meals/filter"

$(function () {
    makeEditable({
            ajaxUrl: mealUrl,
            datatableApi: $("#datatable").DataTable({
                "paging": false,
                "info": true,
                "columns": [
                    {
                        "data": "dateTime"
                    },
                    {
                        "data": "description"
                    },
                    {
                        "data": "calories"
                    },
                    {
                        "defaultContent": "Edit",
                        "orderable": false
                    },
                    {
                        "defaultContent": "Delete",
                        "orderable": false
                    }
                ],
                "order": [
                    [
                        0,
                        "desc"
                    ]
                ]
            }),
            updateTable: function () {
                $.get(mealUrlFilter, updateTable);
            }
        }
    );
});

function getBetween() {
    $.get(mealUrlFilter, $("#filterForm").serialize()).done(updateTable);
    successNoty("filtered");
}

function resetFilter() {
    //$("#filterForm")[0].reset();
    $("#filterForm").find(":input").val("");
    $.get(mealUrl, updateTable);
}