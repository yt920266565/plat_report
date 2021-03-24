//初始化车间
function initWorkshop() {
	layui.use('form', function() {
		var form = layui.form, $ = layui.$;
		var workArea = $('#workArea').val();
		$.ajax({
			url : '/top3/workshop',
			type : 'get',
			data : {
				"workArea" : workArea
			},
			dataType : 'json',
			success : function(res) {
				if (res.status != 200) {
					return;
				}
				$('#workshop option').not('option:first').remove();
				for (var i = 0; i < res.data.length; i++) {
					$('#workshop').append(
							'<option " value="' + res.data[i] + '">'
									+ res.data[i] + '</option>')
				}
				form.render('select');
			}
		})
	})

}

// 初始化客户区域
function initWorkArea() {
	$.ajax({
		url : '/index/workAreaNames',
		type : 'get',
		async : false,
		dataType : 'json',
		success : function(res) {
			if (res.status != 200) {
				return layer.msg('系统出问题了，请联系管理员');
			}
			// index_new_detail里面定义了areaNames接收客户区域
			areaNames = res.data;
			$('#workArea option').remove();
			for (var i = 0; i < areaNames.length; i++) {
				$('#workArea').append(
						'<option value=' + areaNames[i] + '>' + areaNames[i]
								+ '</option>');
			}
		},
		error : function() {
			layer.msg('系统出问题了，请联系管理员');
		}
	})
}
$(function() {
	initWorkArea();
})

function dateFormat(val) {
	if (val != null) {
		val = val.toString();
		var date = new Date(parseInt(val.replace("/Date(", "")
				.replace(")/", ""), 10));
		// 月份为0-11所以+1，月份小于10补个0
		var month = date.getMonth() + 1 < 10 ? "0" + (date.getMonth() + 1)
				: date.getMonth() + 1;
		var currentDate = date.getDate() < 10 ? "0" + date.getDate() : date
				.getDate();
		var hour = date.getHours() < 10 ? "0" + date.getHours() : date
				.getHours();
		var minute = date.getMinutes() < 10 ? "0" + date.getMinutes() : date
				.getMinutes();
		var second = date.getSeconds() < 10 ? "0" + date.getSeconds() : date
				.getSeconds();
		var dd = date.getFullYear() + "-" + month + "-" + currentDate + " "
				+ hour + ":" + minute + ":" + second;
		return dd;
	}
}

function secondsFormat(time) {
	if (time == 0) {
		return 0;
	}
	var hour = 0, minute = 0, seconds = 0;
	// 超过一小时
	hour = parseInt(time / 3600);
	if (hour != 0) {
		if (time % 3600 == 0) {
			return hour + "小时";
		}

		if (parseInt((time % 3600) / 60) == 0) {
			seconds = time % 3600;
			return hour + '小时 ' + seconds + '秒';
		}

		minute = parseInt((time % 3600) / 60);
		if ((time % 3600) % 60 == 0) {
			return hour + '小时 ' + minute + '分钟';
		}

		seconds = (time % 3600) % 60;
		return hour + '小时 ' + minute + '分钟 ' + seconds + '秒';
	}
	// 超过一分钟
	minute = parseInt(time / 60);
	if (minute != 0) {
		if (time % 60 == 0) {
			return minute + "分钟";
		}

		seconds = time % 60;
		return minute + "分钟 " + seconds + "秒";
	}
	return time + "秒";
}

function getDay(day) {
	var today = new Date();
	var targetday_milliseconds = today.getTime() + 1000 * 60 * 60 * 24 * day;
	today.setTime(targetday_milliseconds); // 注意，这行是关键代码
	var tYear = today.getFullYear();
	var tMonth = today.getMonth();
	var tDate = today.getDate();
	tMonth = doHandleMonth(tMonth + 1);
	tDate = doHandleMonth(tDate);
	return tYear + "-" + tMonth + "-" + tDate;
}

function doHandleMonth(month) {
	var m = month;
	if (month.toString().length == 1) {
		m = "0" + month;
	}
	return m;
}

// 记录访问页面的人和时间
function insertAccessRecord(obj, token) {
	var prefix = window.location.href;
	prefix = prefix.replace("http://", "");
	prefix = prefix.substr(0, prefix.indexOf("/"));
	var url = obj.dataset.href;
	url = prefix + url;
	if (url == '' || token == '') {
		return;
	}

	var paramData = {
		"url" : url
	};
	$.ajax({
		url : '/accessRecord',
		type : 'post',
		beforeSend : function(request) {
			request.setRequestHeader("Authorization", "Bearer " + token);
		},
		contentType : 'application/json;charset=utf-8',
		data : JSON.stringify(paramData),
		success : function() {
		}
	})
}

// 格式化接口的返回值为layui数据表格要求格式
function parseData(res) {
	if (res.status == 200) {
		return {
			"code" : 0,
			"count" : res.count,
			"data" : res.data
		};
	}
	return {
		"code" : res.status,
		"msg" : res.errMsg + " " + res.exception
	}
}
