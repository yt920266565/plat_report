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