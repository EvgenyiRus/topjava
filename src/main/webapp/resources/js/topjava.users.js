// Выражение $(function(){}); - это и есть краткое определение функции jquery.
// Данная функция включает весь код на языке javascript,
//     который будет выполняться при загрузке страницы.

var userUrl = "ajax/admin/users/"
// $(document).ready(function () {
$(function () {
    makeEditable({
            ajaxUrl: userUrl,
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
                $.get(userUrl, updateTable);
            }
        }
    );
});

function checkEnabled(checkBox,id) {
    var check = checkBox.is(':checked');
    $.post(context.ajaxUrl+id, {enabled: check } ).success(checkBox.closest("tr").attr("data-userEnabled", check));
    successNoty(check ? "enabled": "disabled");
}