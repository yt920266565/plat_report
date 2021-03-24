//记录访问页面的人和时间
function insertAccessRecord(obj,token){
	var prefix = window.location.href;
	 prefix = prefix.replace("http://","");
	 prefix = prefix.substr(0,prefix.indexOf("/"));
	var url = obj.dataset.href;
	url = prefix + url;
	if(url == '' || token == ''){
		return;
	}
	
	var paramData = {"url":url};
	$.ajax({
		url:'/accessRecord',
		type:'post',
		beforeSend:function(request){
			request.setRequestHeader("Authorization","Bearer "+token);
		},
		contentType:'application/json;charset=utf-8',
		data:JSON.stringify(paramData),
		success:function(){}
	})
}

function dateFormat(val) {
    if (val != null) {
    	val = val.toString();
        var date = new Date(parseInt(val.replace("/Date(", "").replace(")/", ""), 10));
        //月份为0-11所以+1，月份小于10补个0
        var month = date.getMonth() + 1 < 10 ? "0" + (date.getMonth() + 1) : date.getMonth() + 1;
        var currentDate = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
        var hour = date.getHours() < 10 ? "0" + date.getHours() : date.getHours();
        var minute = date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes();
        var second = date.getSeconds() < 10 ? "0" + date.getSeconds() : date.getSeconds();
        var dd = date.getFullYear() + "-" + month + "-" + currentDate + " " + hour + ":" + minute + ":" + second;
        return dd;
    }
}