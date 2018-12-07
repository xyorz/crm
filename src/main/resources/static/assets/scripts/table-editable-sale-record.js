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
            jqTds[0].innerHTML = '<input type="date" class="form-control input-medium" value="' + aData[0] + '">';
            jqTds[1].innerHTML = '<input type="text" class="form-control input-small" value="' + aData[1] + '">';
            jqTds[3].innerHTML = '<a class="edit" href="">保存</a>';
            jqTds[4].innerHTML = '<a class="cancel" href="">放弃</a>';
        }

        function saveRow(oTable, nRow) {
            var jqInputs = $('input', nRow);
            var a=jqInputs[0].value.split("/");
            var b=a+'-';
            b=b.substring(0,10);
            oTable.fnUpdate(b, nRow, 0, false);
            oTable.fnUpdate(jqInputs[1].value, nRow, 1, false);
            oTable.fnUpdate('<a class="edit" href="">编辑</a>', nRow, 3, false);
            oTable.fnUpdate('<a class="delete" href="">申报</a>', nRow, 4, false);
            oTable.fnDraw();
        }

        function gatherRowData(nRow){
            var jqInputs = $('input', nRow);
            if(jqInputs.eq(0).parent().parent().attr("class").indexOf("row_old")<0){
                action_type = "add";
            }

            var a=jqInputs[0].value.split("/");
            var b=a+'-';
            var c0V = b.substring(0,10);
            var c1V = jqInputs[1].value;
            var id_elem = $("#followup_id");
            var id = "";
            if(id_elem !== undefined) id = id_elem.attr("value");
            return {"date": c0V, "record": c1V, "id": id, "saleOpportunityId": $("#sale_opportunity_id").attr("value")};
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
                    alert(result.responseText);
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

            if (nNew && nEditing) {
                if (confirm("存在数据没有保存，是否保存？")) {
                    saveRow(oTable, nEditing); // save
                    $(nEditing).find("td:first").html("Untitled");
                    nEditing = null;
                    nNew = false;

                } else {
                    oTable.fnDeleteRow(nEditing); // cancel
                    nEditing = null;
                    nNew = false;

                    return;
                }
            }

            var aiNew = oTable.fnAddData(['', '', '', '','']);
            var nRow = oTable.fnGetNodes(aiNew[0]);
            editRow(oTable, nRow);
            nEditing = nRow;
            nNew = true;
        });

        table.on('click', '.delete', function (e) {
            e.preventDefault();

            if (confirm("是否删除此信息？") == false) {
                return;
            }

            var nRow = $(this).parents('tr')[0];

            var delId = 0;
            try{
                delId = $("#followup_id").attr("value");
            }catch (e) {
                delId = 0;
            }

            ajaxUpload("/followup/delete", "POST", {"id": delId});

            oTable.fnDeleteRow(nRow);
        });

        table.on('click', '.cancel', function (e) {
            e.preventDefault();

            if (nNew) {
                oTable.fnDeleteRow(nEditing);
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

            if (nEditing !== null && nEditing != nRow) {
                /* Currently editing - but not this row - restore the old before continuing to edit mode */
                restoreRow(oTable, nEditing);
                editRow(oTable, nRow);
                nEditing = nRow;
            } else if (nEditing == nRow && this.innerHTML == "保存") {
                /* Editing this row and want to save it */
                var jqInputs = $('input', nEditing);
                // if(jqInputs[])
                var data = gatherRowData(nEditing);
                var url = null;
                if(action_type==="add") url = "/followup";
                else url = "followup/update";
                ajaxUpload(url, "POST", data);
                saveRow(oTable, nEditing);
                nEditing = null;
            } else {
                /* No edit in progress - let's start one */
                editRow(oTable, nRow);
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