var TableManaged = function () {

    var initTable1 = function () {

        var table = $('#sample_1');

        // begin first table
        table.dataTable({
            "columns": [{
                "orderable": true
            }, {
                "orderable": false
            }, {
                "orderable": false
            }, {
                "orderable": false
            }, ],
            "lengthMenu": [
                [5, 15, 20, -1],
                [5, 15, 20, "All"] // change per page values here
            ],
            // set the initial value
            "pageLength": 5,
            "pagingType": "bootstrap_full_number",
            "language": {
                "lengthMenu": "  _MENU_ records",
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
                [1, "asc"]
            ] // set first column as a default sort by asc
        });

        var tableWrapper = jQuery('#sample_1_wrapper');

        table.find('.group-checkable').change(function () {
            var set = jQuery(this).attr("data-set");
            var checked = jQuery(this).is(":checked");
            jQuery(set).each(function () {
                if (checked) {
                    $(this).attr("checked", true);
                    $(this).parents('tr').addClass("active");
                } else {
                    $(this).attr("checked", false);
                    $(this).parents('tr').removeClass("active");
                }
            });
            jQuery.uniform.update(set);
        });

        function gatherRowData(nRow){
            var cid = nRow.cells[0].innerText;
            var eid = nRow.cells[2].innerText;

            if(!check_null([cid, eid])){
                alert("请完整填写数据");
                return null;
            }

            return {"customerId": cid, "employeeId": eid};
        }

        function ajaxUpload(url, method, data) {
            $.ajax({
                type: method,
                url: url,
                data: data,
                async: false,
                contentType:"application/json",
                success: function(result){
                    alert(result.message);
                    location.reload();
                },
                error: function (result) {
                    try{
                        alert(result['responseJSON']['message']);
                    }catch (e) {
                        alert(result.responseText);
                    }
                    location.reload();
                }
            })
        }

        table.on('click','.share', function () {
            var nRow = $(this).parents('tr')[0];
            if(nRow.cells[2].innerText[0]=='选') {
                alert("请选择你所要共享给的员工");
            }
            else
            {
                if(!confirm("是否确认共享？")){
                    return false;
                }
                var url = "/customer/shareCustomer";
                var d = gatherRowData(nRow);
                if(d === null) return;
                ajaxUpload(url, "GET", d);
                window.location.reload();
            }
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