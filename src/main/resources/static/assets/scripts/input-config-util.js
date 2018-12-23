function input_config_product(kayField, multi) {
    $("input#pro").bsSuggest({
        url: "/product/list",
        idField: "id",
        keyField: kayField,
        showHeader: true,
        showBtn: true,
        autoDropup: true,
        multiWord: multi,
        separator: ' ',
        hideOnSelect: true,
        getDataMethod: 'url',
        data: {
            value: ["id", "variety", "price"]
        },
        listHoverStyle: 'background: #C0C0C0; color:#fff',
        listStyle: {
            'text-align': 'center',
            'cursor': 'default',
            'z-index': ' 9999',
            'overflow': 'visible'
        },
        effectiveFieldsAlias: {id: "产品编号", variety: "产品种类", price: "产品价格"}
    }).on('onSetSelectValue', function (e, selectedData, selectedRawData) {
        if(multi)
            $(this).val($(this).val() + " ");
    })
}

function input_config_customer(kayField) {
    $("input#cus").bsSuggest({
        url: "/customer/list",
        idField: "id",
        keyField: kayField,
        showHeader: true,
        showBtn: true,
        autoDropup: true,
        // multiWord: true,
        // separator: ',',
        hideOnSelect: true,
        getDataMethod: 'url',
        searchFields: [
            'id',
            'name'
        ],
        data: {
            value: ["id", "name", "tel"]
        },
        listHoverStyle: 'background: #C0C0C0; color:#fff',
        listStyle: {
            'text-align': 'center',
            'cursor': 'default',
            'z-index': ' 9999',
            'overflow': 'visible'
        },
        effectiveFieldsAlias: {id: "客户编号", name: "客户名称", tel: "电话号码"}
    }).on('onSetSelectValue', function (e, selectedData, selectedRawData) {
        // $(this).val($(this).val()+",");
    })
}

function input_config_employee(kayField) {
    $("input#emp").bsSuggest({
        url: "/employee/list",
        idField: "id",
        keyField: kayField,
        showHeader: true,
        showBtn: true,
        autoDropup: true,
        // multiWord: true,
        // separator: ',',
        hideOnSelect: true,
        getDataMethod: 'url',
        searchFields: [
            'id',
            'name'
        ],
        data: {
            value: ["id", "name", "tel"]
        },
        listHoverStyle: 'background: #C0C0C0; color:#fff',
        listStyle: {
            'text-align': 'center',
            'cursor': 'default',
            'z-index': ' 9999',
            'overflow': 'visible'
        },
        effectiveFieldsAlias: {id: "员工编号", name: "员工名称", tel: "电话号码"}
    }).on('onSetSelectValue', function (e, selectedData, selectedRawData) {
        // $(this).val($(this).val()+",");
    })
}
