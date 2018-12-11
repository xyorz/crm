var TableEditable = function () {

    var handleTable = function () {

        function restoreRow(data,nRow) {
            var jqTds = $('>td', nRow);
            for (var i = 2, iLen = jqTds.length-1; i < iLen; i++) {
                jqTds[i].innerHTML=data[i-2];
            }
            jqTds[6].innerHTML='<a class="edit" href="">编辑</a>'

        }

        function editRow1(nRow) {
            var jqTds = $('>td', nRow);
            jqTds[2].innerHTML = '<input type="text" class="form-control input-small" value="' + nRow.cells[2].innerText + '">';
            jqTds[3].innerHTML = '<input type="text" class="form-control input-small" value="' + nRow.cells[3].innerText + '">';
            jqTds[4].innerHTML = '<input type="text" class="form-control input-small" value="' + nRow.cells[4].innerText + '">';
            jqTds[5].innerHTML = '<input type="text" class="form-control input-small" value="' + nRow.cells[5].innerText + '">';
            jqTds[6].innerHTML = '<a class="edit" href="">保存</a><br><a class="cancel" href="">放弃</a>';
        }
        function saveRow(nRow) {
            var jqInputs = $('input', nRow);
            var jqTds = $('>td', nRow);
            jqTds[2].innerHTML = jqInputs[0].value;
            jqTds[3].innerHTML = jqInputs[1].value;
            jqTds[4].innerHTML = jqInputs[2].value;
            jqTds[5].innerHTML = jqInputs[3].value;
            jqTds[6].innerHTML = '<a class="edit" href="">编辑</a>'
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
            if(action_type === "add"){
               var c5V = jqInputs[4].value;
               return {"id": c0V, 'variety': c1V, "amount": c2V, "price": c3V, "cost": c4V, "analysis": c5V};
            }
            return {"id": c0V, "amount": c1V, "price": c2V, "cost": c3V, "analysis": c4V};
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
        var table2 = $('#sample_editable_2');
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
        var data=["0","0","0","0"];
        $('#sample_editable_1_new').click(function (e) {
                window.location.href="/product"
            });

        table.on('click', '.cancel', function (e) {
            e.preventDefault();
                restoreRow(data,nEditing);
                nEditing = null;
        });
        table.on('click', '.edit', function (e) {
            e.preventDefault();
            var nRow = $(this).parents('tr')[0];
            /* Get the row as a parent of the link that was clicked on */
           if (nEditing == nRow && this.innerHTML == "保存") {
                /* Editing this row and want to save it */
                var data = gatherRowData(nEditing);
                var url = null;
                if(action_type==="add") url = "/product";
                else url = "/product/update";
                ajaxUpload(url, "POST", data);
               saveRow(nEditing);
                nEditing = null;
            } else {
                /* No edit in progress - let's start one */
               // data[0]= nRow.cells[2].innerText;
               // data[1]= nRow.cells[3].innerText;
               // data[2]= nRow.cells[4].innerText;
               // data[3]= nRow.cells[5].innerText;
                editRow1(nRow);
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