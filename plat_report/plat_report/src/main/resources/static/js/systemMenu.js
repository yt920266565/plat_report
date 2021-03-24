$(document).ready(function () {
    $.ajax({
        type: "post",
        dataType: "json",
        async: false,
        url: "<%=PN%>/systemMenu",
        data: { code: '10001', token: token },
        success: function (data) {
        	alert(data);
        	console.log(data);
            var menustr = getMenuHtml(eval(data), 0);
            $("#menubox").html(menustr);
        }
    })
	 
	 
});
 