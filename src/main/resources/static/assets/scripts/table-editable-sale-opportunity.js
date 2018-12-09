var TableEditable = function () {

    var handleTable = function () {
        var table = $('#sample_editable_1');

        var oTable = table.dataTable({
            "lengthMenu": [
                [5, 15, 20, -1],
                [5, 15, 20, "All"] // change per page values here
            ],
            // set the initial value
            "pageLength": 10,

            "language": {
                "lengthMenu": " _MENU_ 行"
            },
            "columnDefs": [{ // set default column settings
                'orderable': true,
                'targets': [0]
            }, {
                "searchable": true,
                "targets": [0]
            }],
            "order": [
                [0, "asc"]
            ] // set first column as a default sort by asc
        });

        var tableWrapper = $("#sample_editable_1_wrapper");

        function ajaxUpload(url, method, data) {
            $.ajax({
                type: method,
                url: url,
                data: data,
                async: false,
                contentType:"application/json",
                success: function(result){
                    alert(result.message);
                    window.location.href = "/sale_opportunity/my_opp?employeeId=" + $("#employee_id").attr("value");
                },
                error: function (result) {
                    alert(result.responseText);
                    location.reload();
                }
            })
        }

        tableWrapper.find(".dataTables_length select").select2({
            showSearchInput: false //hide search box with special css class
        }); // initialize select2 dropdown
        table.on('click', '.getopp', function (e) {
            var id = $(this).parent().siblings().eq(0).text();
            var employeeId = $("#employee_id").attr("value");
            ajaxUpload("/sale_opportunity/gain", "GET", {"id": id, "employeeId": employeeId});
        });
    }

    return {

        //main function to initiate the module
        init: function () {
            handleTable();
        }

    };

}();