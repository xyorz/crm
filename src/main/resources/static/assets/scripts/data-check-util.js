function isNumber(value) {
    var patrn = /^(-)?\d+(\.\d+)?$/;
    if (patrn.exec(value) == null || value == "") {
        return false
    } else {
        return true
    }
}

function check_null(list) {
    for(var i = 0; i < list.length; i++) {
        console.log(list[i]);
        console.log($.trim(list[i]));
        if (list[i] === "" || list[i] === null || $.trim(list[i]) === "")
            return false;
    }
    return true;
}