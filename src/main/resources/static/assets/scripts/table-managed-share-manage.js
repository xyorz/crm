var TableManaged = function () {

    var initTable1 = function () {

        var table = $('#sample_1');

        // begin first table
        table.dataTable({
            "columns": [ {
                "orderable": true
            }, {
                "orderable": false
            }, {
                "orderable": true
            }],
            "lengthMenu": [
                [5, 15, 20, -1],
                [5, 15, 20, "All"] // change per page values here
            ],
            // set the initial value
            "pageLength": 5,
            "pagingType": "bootstrap_full_number",
            "language": {
                "lengthMenu": "  _MENU_ 行",
                "paginate": {
                    "previous":"Prev",
                    "next": "Next",
                    "last": "Last",
                    "first": "First"
                }
            },
            "columnDefs": [{  // set default column settings
                'orderable': false,
                'targets': [0]
            }, {
                "searchable": false,
                "targets": [0]
            }],
            "order": [
                [0, "asc"]
            ] // set first column as a default sort by asc
        });

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
                    try{
                        alert(result['responseJSON']['message']);
                    }
                    catch (e) {
                        alert(result.responseText);
                    }
                    location.reload();
                }
            })
        }

        table.on('click','.cancel_share', function () {
            var nRow = $(this).parents('tr')[0];
            if(!confirm("是否确认取消？")){
                return false;
            }
            var url = "/customer/cancelShare";
            ajaxUpload(url, "GET", {"customerName": nRow.cells[0].innerText, "employeeId": $(nRow.cells[2]).children("input").attr("value")});
            window.location.reload();
        });

    };
    return {

        //main function to initiate the module
        init: function () {
            if (!jQuery().dataTable) {
                return;
            }

            initTable1();
        }

    };

}();