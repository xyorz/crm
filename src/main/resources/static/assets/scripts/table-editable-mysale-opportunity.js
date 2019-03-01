var TableEditable = function () {

    var handleTable = function () {

        function restoreRow(oTable, nRow) {
            var aData = oTable.fnGetData(nRow);
            var jqTds = $('>td', nRow);

            for (var i = 0, iLen = jqTds.length; i < iLen; i++) {
                oTable.fnUpdate(aData[i], nRow, i, false);
            }

            oTable.fnDraw();
        }

        function editRow(oTable, nRow) {
            var aData = oTable.fnGetData(nRow);
            var jqTds = $('>td', nRow);
            // jqTds[0].innerHTML = '<input type="text" class="form-control input-small" value="' + aData[0] + '">';
            // var d = $(nRow).children("#test").val();
            jqTds[1].innerHTML = '<div class="input-group">\n' +
                '                                      <input class="form-control input-small" type="text" id="pro" autocomplete="off" value="' + aData[1] + '" data-id alt>\n' +
                '                                      <div style="position: fixed;z-index: 2">\n' +
                '                                          <ul class="dropdown-menu dropdown-menu-right list-group" role="menu">\n' +
                '                                          </ul>\n' +
                '                                      </div>\n' +
                '                                  </div>';
            input_config_product("variety", true);
            jqTds[2].innerHTML = '<div class="input-group">\n' +
                '                                      <input class="form-control input-small" type="text" id="cus" autocomplete="off" value="' + aData[2] + '" data-id alt>\n' +
                '                                      <div style="position: fixed;z-index: 2">\n' +
                '                                          <ul class="dropdown-menu dropdown-menu-right list-group" role="menu">\n' +
                '                                          </ul>\n' +
                '                                      </div>\n' +
                '                                  </div>';
            input_config_customer("name");
            jqTds[3].innerHTML = '<a class="edit" href="">保存</a>';
            jqTds[4].innerHTML = '<a class="cancel" href="">放弃</a>';
        }

        function editRow1(oTable, nRow) {
            var aData = oTable.fnGetData(nRow);
            var jqTds = $('>td', nRow);
            jqTds[1].innerHTML = '<div class="input-group">\n' +
                '                                      <input class="form-control input-small" type="text" id="pro" autocomplete="off" value="' + aData[1] + '" data-id alt>\n' +
                '                                      <div style="position: fixed;z-index: 2">\n' +
                '                                          <ul class="dropdown-menu dropdown-menu-right list-group" role="menu">\n' +
                '                                          </ul>\n' +
                '                                      </div>\n' +
                '                                  </div>';
            input_config_product("variety", true);
            jqTds[2].innerHTML = '<div class="input-group">\n' +
                '                                      <input class="form-control input-small" type="text" id="cus" autocomplete="off" value="' + aData[2] + '" data-id alt>\n' +
                '                                      <div style="position: fixed;z-index: 2">\n' +
                '                                          <ul class="dropdown-menu dropdown-menu-right list-group" role="menu">\n' +
                '                                          </ul>\n' +
                '                                      </div>\n' +
                '                                  </div>';
            input_config_customer("name");
            jqTds[3].innerHTML = '<a class="edit" href="">保存</a>';
            jqTds[4].innerHTML = '<a class="cancel" href="">放弃</a>';
        }

        function saveRow(oTable, nRow) {
            var jqInputs = $('input', nRow);
            if(jqInputs[5]!=null)
            {
                oTable.fnUpdate(jqInputs[0].value, nRow, 0, false);
                oTable.fnUpdate(jqInputs[1].value, nRow, 1, false);
                oTable.fnUpdate(jqInputs[2].value, nRow, 2, false);
            }
            else
            {
                oTable.fnUpdate(jqInputs[0].value, nRow, 1, false);
                oTable.fnUpdate(jqInputs[1].value, nRow, 2, false);
            }
            oTable.fnUpdate('<a class="edit" href="">编辑</a>', nRow,3, false);
            oTable.fnUpdate('<a class="delete" href="">删除</a>', nRow, 4, false);
            oTable.fnDraw();
        }

        function cancelEditRow(oTable, nRow) {
            var jqInputs = $('input', nRow);
            oTable.fnUpdate(jqInputs[0].value, nRow, 0, false);
            oTable.fnUpdate(jqInputs[1].value, nRow, 1, false);
            oTable.fnUpdate(jqInputs[2].value, nRow, 2, false);
            oTable.fnUpdate('<a class="edit" href="">编辑</a>', nRow, 3, false);
            oTable.fnDraw();
        }

        function gatherRowData(nRow){
            var jqInputs = $('input', nRow);
            var c0V = $(nRow).children("td").eq(0).text();
            if(c0V === ""||c0V===null) action_type = "add";
            else action_type = "update";
            var c1V = $.trim(jqInputs[0].value).split(' ');
            var c2V = jqInputs[1].value;
            var current_id = $("#current_employee_id").attr("value");

            if(!check_null([c1V, c2V, current_id])){
                alert("请完整填写数据");
                return null;
            }

            return {"id": c0V, "productIds": c1V, "customerName": c2V, "findEmployeeId": current_id};
        }

        function ajaxUpload(url, method, data) {
            $.ajax({
                type: method,
                url: url,
                data: JSON.stringify(data),
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

        var action_type = null;

        var table = $('#sample_editable_1');
        var table2 = $('#sample_editable_2');

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
        tableWrapper.find(".dataTables_length select").select2({
            showSearchInput: false //hide search box with special css class
        }); // initialize select2 dropdown

        var oTable2 = table2.dataTable({
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
        var tableWrapper2 = $("#sample_editable_2_wrapper");
        tableWrapper2.find(".dataTables_length select").select2({
            showSearchInput: false //hide search box with special css class
        }); // initialize select2 dropdown
        var nEditing = null;
        var nNew = false;

        $('#sample_editable_1_new').click(function (e) {
            e.preventDefault();
            if(nNew==false && nEditing==null)
            {
                var aiNew = oTable.fnAddData(['', '','', '','']);
                var nRow = oTable.fnGetNodes(aiNew[0]);
                editRow(oTable, nRow);
                nEditing = nRow;
                nNew = true;
            }
            else
            {
                alert("请先保存之前的信息");
            }
        });

        table.on('click', '.delete', function (e) {
            e.preventDefault();

            if (confirm("是否删除此信息？") == false) {
                return;
            }

            var delId = 0;
            try{
                delId = $(this).parent().parent().children('td').eq(0).text();
            }catch (e) {
                delId = 0;
            }

            ajaxUpload("/sale_opportunity/delete", "POST", {"id": delId});

            var nRow = $(this).parents('tr')[0];
            oTable.fnDeleteRow(nRow);
        });

        table.on('click', '.cancel', function (e) {
            e.preventDefault();

            if (nNew) {
                oTable.fnDeleteRow(nEditing);
                nNew = false;
                nEditing = null;
            } else {
                restoreRow(oTable, nEditing);
                nEditing = null;
            }
        });

        table.on('click', '.edit', function (e) {
            e.preventDefault();

            /* Get the row as a parent of the link that was clicked on */
            var nRow = $(this).parents('tr')[0];

            if (nEditing !== null && nEditing != nRow) {
                /* Currently editing - but not this row - restore the old before continuing to edit mode */
                if(nNew==true) {
                    oTable.fnDeleteRow(nEditing);
                }
                else {
                    restoreRow(oTable, nEditing);
                }
                editRow(oTable, nRow);
                nNew = false;
                nEditing = nRow;
            } else if (nEditing == nRow && this.innerHTML == "保存") {
                /* Editing this row and want to save it */
                var data = gatherRowData(nEditing);
                if(data === null) return;
                var url = null;
                if(action_type==="add") url = "/sale_opportunity";
                else url = "/sale_opportunity/update";
                ajaxUpload(url, "POST", data);
                saveRow(oTable, nEditing);
                nEditing = null;
            } else {
                /* No edit in progress - let's start one */
                editRow1(oTable, nRow);
                nEditing = nRow;
            }
        });
        $(".complete").click(function () {
            if(!confirm("是否确认完成？")){
                return false;
            }
            ajaxUpload("/sale_opportunity/complete", "POST", {"saleOpportunityId": $(this).parent().parent().children("td").eq(0).text()});
        });
        table2.on('click', '.sonpage', function (e) {
            window.location.href="/followup?saleOpportunityId="+this.innerHTML;
        });
    };


    return {

        //main function to initiate the module
        init: function () {
            handleTable();
        }

    };

}();