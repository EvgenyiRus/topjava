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
            })
        }
    );
});

function getBetween() {
     debugger;
    // $.get("ajax/profile/meals/filter",$("#filterForm").serialize());
    // updateTable();
    // successNoty("filtered");
    $.ajax({
        type: "GET",
        url: context.ajaxUrl+"filter",
        data: $("#filterForm").serialize() //передаваемые данные
    }).done(function () {
        updateTable();
        successNoty("filtered");
    });
}

function resetFilter() {
    //$("#filterForm")[0].reset();
    $("#filterForm").find(":input").val("");
    updateTable();
    //$.get("ajax/profile/meals/",updateTable);
}