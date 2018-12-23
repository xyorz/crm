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
            jqTds[1].innerHTML = '<div class="input-group">\n' +
                '                                      <input class="form-control input-small" type="text" id="cus" autocomplete="off" value="' + aData[1] + '" data-id alt>\n' +
                '                                      <div style="position: fixed;z-index: 2">\n' +
                '                                          <ul class="dropdown-menu dropdown-menu-right list-group" role="menu">\n' +
                '                                          </ul>\n' +
                '                                      </div>\n' +
                '                                  </div>';
            input_config_customer("id");
            // jqTds[2].innerHTML = '<input type="text" class="form-control input-small" value="' + aData[2] + '">';
            jqTds[3].innerHTML = '<div class="input-group">\n' +
                '                                      <input class="form-control input-small" type="text" id="pro" autocomplete="off" value="' + aData[3] + '" data-id alt>\n' +
                '                                      <div style="position: fixed;z-index: 2">\n' +
                '                                          <ul class="dropdown-menu dropdown-menu-right list-group" role="menu">\n' +
                '                                          </ul>\n' +
                '                                      </div>\n' +
                '                                  </div>';
            input_config_product("id", false);
            jqTds[4].innerHTML = '<input type="text" class="form-control input-small" value="' + aData[4] + '">';
            jqTds[5].innerHTML = '0';
            jqTds[6].innerHTML = '未完成';
            jqTds[7].innerHTML = '未开具';
            jqTds[8].innerHTML = '';
            jqTds[9].innerHTML = '<a class="edit" href="">保存</a>';
            jqTds[10].innerHTML = '<a class="cancel" href="">放弃</a>';
        }
        function editRow1(oTable, nRow) {
            var aData = oTable.fnGetData(nRow);
            var jqTds = $('>td', nRow);
            jqTds[5].innerHTML = '<input type="text" class="form-control input-small" value="' + aData[5] + '">';
            jqTds[7].innerHTML = '          <select class="form-control input-small select2me" id="receipt_status">\n' +
                '                                    <option value="AL">未开具</option>\n' +
                '                                    <option value="WY">已开具</option>\n' +
                '                           </select>';
            // jqTds[7].innerHTML = '<input type="text" class="form-control input-small" value="' + aData[7] + '">';
            jqTds[8].innerHTML = '<input type="text" class="form-control input-small" value="' + aData[8] + '">';
            jqTds[9].innerHTML = '<a class="edit" href="">保存</a>';
            jqTds[10].innerHTML = '<a class="cancel" href="">放弃</a>';
        }

        function saveRow(oTable, nRow) {
            var jqInputs = $('input', nRow);
           if(jqInputs[3]!=null)
            {
                oTable.fnUpdate("", nRow, 0, false);
                oTable.fnUpdate(jqInputs[0].value, nRow, 1, false);
                oTable.fnUpdate(jqInputs[1].value, nRow, 2, false);
                oTable.fnUpdate(jqInputs[2].value, nRow, 3, false);
                oTable.fnUpdate(jqInputs[3].value, nRow, 4, false);
                oTable.fnUpdate('0', nRow, 5, false);
                oTable.fnUpdate('未完成', nRow, 6, false);
                oTable.fnUpdate('未开具', nRow, 7, false);
                oTable.fnUpdate('', nRow, 8, false);
            }
          else
            {
                oTable.fnUpdate(jqInputs[0].value, nRow, 5, false);
                oTable.fnUpdate(jqInputs[1].value, nRow, 7, false);
                oTable.fnUpdate(jqInputs[2].value, nRow, 8, false);
            }
            oTable.fnUpdate('<a class="edit" href="">编辑</a>', nRow, 9, false);
            oTable.fnUpdate('<a class="delete" href="">删除</a>', nRow, 10, false);
            oTable.fnDraw();
        }

        function cancelEditRow(oTable, nRow) {
            var jqInputs = $('input', nRow);
            oTable.fnUpdate(jqInputs[0].value, nRow, 0, false);
            oTable.fnUpdate(jqInputs[1].value, nRow, 1, false);
            oTable.fnUpdate(jqInputs[2].value, nRow, 2, false);
            oTable.fnUpdate(jqInputs[3].value, nRow, 3, false);
            oTable.fnUpdate(jqInputs[4].value, nRow, 4, false);
            oTable.fnUpdate(jqInputs[5].value, nRow, 5, false);
            oTable.fnUpdate(jqInputs[6].value, nRow, 6, false);
            oTable.fnUpdate(jqInputs[7].value, nRow, 7, false);
            oTable.fnUpdate(jqInputs[8].value, nRow, 8, false);
            oTable.fnUpdate('<a class="edit" href="">编辑</a>', nRow, 9, false);
            oTable.fnDraw();
        }

        function gatherRowData(nRow){
            var jqInputs = $('input', nRow);
            var c0V = $(nRow).children("td").eq(0).text();
            if(c0V === ""||c0V===null) action_type = "add";
            var id = 0;
            var customerId = 0;
            var productId = 0;
            var value = 0;
            var paidValue = 0;
            var status = "未完成";
            var receiptStatus = "未开具";
            var record = "";
            if(action_type==='add'){
                id = c0V;
                customerId = jqInputs[0].value;
                productId = jqInputs[1].value;
                value = jqInputs[2].value;
                paidValue = nRow.cells[5].innerText;
                status = false;
                receiptStatus = false;
                record = "";
            }
            else{
                id = c0V;
                customerId = nRow.cells[1].innerText;
                productId = nRow.cells[3].innerText;
                value = nRow.cells[4].innerText;
                paidValue = jqInputs[0].value;
                status = 0;
                console.log(nRow.cells[7].innerText);
                receiptStatus = $("#receipt_status").find("option:selected").text() === "已开具";
                record = jqInputs[1].value;
            }
            if(!check_null([customerId, value, productId, paidValue, status, receiptStatus])){
                alert("请完整填写数据");
                return null;
            }
            if(!isNumber(customerId)){
                alert("客户编号必须是数字");
                return null;
            }
            if(!isNumber(value)) {
                alert("订单总金额必须是数字");
                return null;
            }
            if(!isNumber(productId)) {
                alert("产品编号必须是数字");
                return null;
            }
            if(!isNumber(paidValue)) {
                alert("订单已支付金额必须是数字");
                return null;
            }

            return {"id": id, "customerId": customerId, "value": value, "productId": productId, "paidValue": paidValue,
                "status": status, "receiptStatus": receiptStatus, "record": record};
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

        var nEditing = null;
        var nNew = false;

        $('#sample_editable_1_new').click(function (e) {
            e.preventDefault();
            if(nNew==false && nEditing==null)
            {
                var aiNew = oTable.fnAddData(['', '', '', '', '', '','', '', '', '', '']);
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

            var nRow = $(this).parents('tr')[0];
            if(nRow.cells[5].innerText!=0)
            {
                alert("订单已进行了支付，不能删除");
            }
            else
            {
                var delId = 0;
                try{
                    delId = $(this).parent().parent().children('td').eq(0).text();
                }catch (e) {
                    delId = 0;
                }

                ajaxUpload("order/delete", "POST", {"id": delId});

                oTable.fnDeleteRow(nRow);
            }

        });

        table.on('click', '.cancel', function (e) {
            e.preventDefault();

            if (nNew) {
                oTable.fnDeleteRow(nEditing);
                nEditing = null;
                nNew = false;
            } else {
                restoreRow(oTable, nEditing);
                nEditing = null;
            }
        });

        table.on('click', '.edit', function (e) {
            e.preventDefault();

            /* Get the row as a parent of the link that was clicked on */
            var nRow = $(this).parents('tr')[0];
            var jqInputs = $('input', nRow);


            if (nEditing !== null && nEditing != nRow) {
                /* Currently editing - but not this row - restore the old before continuing to edit mode */
                if(nNew==true) {
                    oTable.fnDeleteRow(nEditing);
                }
                else {
                    restoreRow(oTable, nEditing);
                }
                editRow1(oTable, nRow);
                nNew = false;
                nEditing = nRow;
            } else if (nEditing == nRow && this.innerHTML == "保存") {
                /* Editing this row and want to save it */
                if(nNew==true || nRow.cells[4].innerText>jqInputs[0].value){
                    var data = gatherRowData(nEditing);
                    if(data === null) return;
                    var url = null;
                    if(action_type==="add") url = "order";
                    else url = "order/update";
                    ajaxUpload(url, "POST", data);
                    saveRow(oTable, nEditing);
                    nEditing = null;
                    nNew = false;
                }
                else
                {
                    alert("已支付金额不能大于订单总金额");
                }

            } else {
                /* No edit in progress - let's start one */
                editRow1(oTable, nRow);
                nEditing = nRow;
            }

        });
    }

    return {

        //main function to initiate the module
        init: function () {
            handleTable();
        }

    };

}();