// Выражение $(function(){}); - это и есть краткое определение функции jquery.
// Данная функция включает весь код на языке javascript,
//     который будет выполняться при загрузке страницы.

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
            }),
            updateTable: function () {
                $.get("ajax/admin/users/", updateTable);
            }
        }
    );
});

function checkEnabled(checkBox,id) {
    //alert($('#check').is(':checked'));
    var check = checkBox.is(':checked');
    $.post(context.ajaxUrl+id, {enabled: check } );
    checkBox.closest("tr").attr("data-userEnabled", check);
    successNoty(check ? "enabled": "disabled");
}