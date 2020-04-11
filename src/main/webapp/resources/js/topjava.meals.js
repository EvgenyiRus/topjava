$(function () {
    makeEditable({
            ajaxUrl: "ajax/profile/meals/",
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
                        "asc"
                    ]
                ]
            }),
            updateTable: function () {
                $.get("ajax/profile/meals/", updateTable);
            }
        }
    );
});

function getBetween() {
    $.get("ajax/profile/meals/filter", $("#filterForm").serialize()).done(updateTable);
    successNoty("filtered");
}

function resetFilter() {
    //$("#filterForm")[0].reset();
    $("#filterForm").find(":input").val("");
    $.get("ajax/profile/meals/", updateTable);
}