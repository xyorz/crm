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
        function editRow1(oTable, nRow) {
            var aData = oTable.fnGetData(nRow);
            var jqTds = $('>td', nRow);
            // jqTds[0].innerHTML = '<input type="text" class="form-control input-small" value="' + aData[0] + '">';
            jqTds[1].innerHTML = '<input type="text" class="form-control input-small" value="' + aData[1] + '">';
            jqTds[2].innerHTML = '<input type="text" class="form-control input-small" value="' + aData[2] + '">';
            jqTds[3].innerHTML = '<input type="text" class="form-control input-small" value="' + aData[3] + '">';
            jqTds[4].innerHTML = '<input type="text" class="form-control input-small" value="' + aData[4] + '">';
            jqTds[5].innerHTML = '<a class="edit" href="">保存</a><br><a class="cancel" href="">放弃</a>';
        }
        function editRow(oTable, nRow) {
            var aData = oTable.fnGetData(nRow);
            var jqTds = $('>td', nRow);
            jqTds[1].innerHTML = '<input type="text" class="form-control input-small" value="' + aData[1] + '">';
            jqTds[2].innerHTML = '<input type="text" class="form-control input-small" value="' + aData[2] + '">';
            jqTds[3].innerHTML = '<input type="text" class="form-control input-small" value="' + aData[3] + '">';
            jqTds[4].innerHTML = '<input type="text" class="form-control input-small" value="' + aData[4] + '">';
            jqTds[5].innerHTML = '<a class="edit" href="">保存</a><br><a class="cancel" href="">放弃</a>';
        }
        function saveRow(oTable, nRow) {
            var aData = oTable.fnGetData(nRow);
            var jqTds = $('>td', nRow);
            var jqInputs = $('input', nRow);
            if(jqInputs[4]!=null)
            {
                oTable.fnUpdate('<a href="link_man.html">'+jqInputs[0].value+'</a>', nRow, 0, false);
                oTable.fnUpdate(jqInputs[1].value, nRow, 1, false);
                oTable.fnUpdate(jqInputs[2].value, nRow, 2, false);
                oTable.fnUpdate(jqInputs[3].value, nRow, 3, false);
                oTable.fnUpdate(jqInputs[4].value, nRow, 4, false);
            }
            else
            {
                oTable.fnUpdate(jqInputs[0].value, nRow, 1, false);
                oTable.fnUpdate(jqInputs[1].value, nRow, 2, false);
                oTable.fnUpdate(jqInputs[2].value, nRow, 3, false);
                oTable.fnUpdate(jqInputs[3].value, nRow, 4, false);
            }
            oTable.fnUpdate('<a class="edit" href="">编辑</a>', nRow, 5, false);
            oTable.fnDraw();
        }

        function gatherRowData(nRow){
            var jqInputs = $('input', nRow);
            var c0V = $(nRow).children("td").eq(0).text();
            if(c0V === ""||c0V===null) action_type = "add";
            else action_type = "update";
            var c1V = jqInputs[0].value;
            var c2V = jqInputs[1].value;
            var c3V = jqInputs[2].value;
            var c4V = jqInputs[3].value;
            return {"id": c0V, "name": c1V, "tel": c2V, "address": c3V, "credit": c4V, "text": " "};
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

        var table = $('#sample_editable_1');

        var action_type = null;

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

            var aiNew = oTable.fnAddData(['', '', '', '', '', '']);
            var nRow = oTable.fnGetNodes(aiNew[0]);
            editRow1(oTable, nRow);
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
                delId = $(this).parent().parent().children('td').eq(0).text();
            }catch (e) {
                delId = 0;
            }

            ajaxUpload("customer/delete", "POST", {"id": delId});

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

            if (nEditing !== null && nEditing !== nRow) {
                /* Currently editing - but not this row - restore the old before continuing to edit mode */
                restoreRow(oTable, nEditing);
                editRow(oTable, nRow);
                nEditing = nRow;
            } else if (nEditing === nRow && this.innerHTML === "保存") {
                /* Editing this row and want to save it */
                var data = gatherRowData(nEditing);
                var url = null;
                if(action_type==="add") url = "customer";
                else url = "customer/update";
                ajaxUpload(url, "POST", data);
                saveRow(oTable, nEditing);
                nEditing = null;
            } else {
                /* No edit in progress - let's start one */
                editRow(oTable, nRow);
                nEditing = nRow;
            }
        });
        table.on('click', '.sonpage', function (e) {
            window.location.href="customer_man.html?id="+this.innerHTML;
        });
    }

    return {

        //main function to initiate the module
        init: function () {
            handleTable();
        }

    };

}();