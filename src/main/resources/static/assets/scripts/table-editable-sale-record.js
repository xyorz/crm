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
            jqTds[0].innerHTML = '<input type="date" class="form-control" value="'+aData[0]+'">';
            jqTds[1].innerHTML = '<input type="text" class="form-control" value="' + aData[1] + '">';
            jqTds[2].innerHTML = '<input type="text" class="form-control input-small" value="' + aData[2] + '">';
            jqTds[4].innerHTML = '<a class="edit" href="">保存</a>';
            jqTds[5].innerHTML = '<a class="cancel" href="">放弃</a>';
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
            var c2V = jqInputs[2].value;
            var id_elem = $("#followup_id");
            var id = "";
            if(id_elem !== undefined) id = id_elem.attr("value");

            if(!check_null([c0V, c1V])){
                alert("请完整填写数据");
                return null;
            }

            return {"date": c0V, "record": c1V, "id": id, "cost": c2V,"saleOpportunityId": $("#sale_opportunity_id").attr("value")};
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
                var aiNew = oTable.fnAddData(['', '', '', '', '', '']);
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

            var text = $(this).parent().parent().children("#declare").text();
            if(text === "已申报"){
                alert("不能重复申报");
                return;
            }

            if (confirm("是否确认申报？") == false) {
                return;
            }

            var nRow = $(this).parents('tr')[0];
            var delId = 0;
            try{
                delId = $(this).parent().parent().children("#followup_id").attr("value");
                // delId = $("#followup_id").attr("value");
            }catch (e) {
                delId = 0;
            }

            ajaxUpload("/followup/declare", "POST", {"id": delId});

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

            if (nEditing !== null && nEditing != nRow) {
                /* Currently editing - but not this row - restore the old before continuing to edit mode */
                var text = $(this).parent().parent().children("#declare").text();
                if(text === "已申报"){
                    alert("已申报，无法编辑");
                    return;
                }
                if (text === "已申报"){
                    alert("")
                }
                restoreRow(oTable, nEditing);
                editRow(oTable, nRow);
                nEditing = nRow;
            } else if (nEditing == nRow && this.innerHTML == "保存") {
                /* Editing this row and want to save it */
                var jqInputs = $('input', nEditing);
                // if(jqInputs[])
                var data = gatherRowData(nEditing);
                if(data === null) return;
                var url = null;
                if(action_type==="add") url = "/followup";
                else url = "followup/update";
                ajaxUpload(url, "POST", data);
                saveRow(oTable, nEditing);
                nNew = false;
                nEditing = null;
            } else {
                /* No edit in progress - let's start one */
                var text = $(this).parent().parent().children("#declare").text();
                if(text === "已申报"){
                    alert("已申报，无法编辑");
                    return;
                }
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