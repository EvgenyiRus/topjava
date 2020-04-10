// $(document).ready(function () {
$(function () {
    makeEditable({
            ajaxUrl: "ajax/admin/users/",
            datatableApi: $("#datatable").DataTable({
                "paging": false,
                "info": true,
                "columns": [
                    {
                        "data": "name"
                    },
                    {
                        "data": "email"
                    },
                    {
                        "data": "roles"
                    },
                    {
                        "data": "enabled"
                    },
                    {
                        "data": "registered"
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

function checkEnabled(id) {
    //alert($('#check').is(':checked'));
    var check = $('#check').is(':checked');
    $.ajax({
        type: "POST",
        url: context.ajaxUrl + id,
        data: {"enabled": check}
        //data: "enabled=" + check
    })
        .done(function () {
            updateTable();
            successNoty(check ? "enabled": "disabled");
        })
    ;
    //Равносильно ли это ?
    // $.post(context.ajaxUrl+id, {enabled: check } );
    // updateTable();
    // successNoty(check ? "enabled": "disabled");
}