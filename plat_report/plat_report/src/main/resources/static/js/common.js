//绑定字典内容到指定的Select控件
function BindSelect(ctrlName, url) {
    var control = $('#' + ctrlName);
    //设置Select2的处理
    control.select2({
        allowClear: true,
        formatResult: formatResult,
        formatSelection: formatSelection,
        escapeMarkup: function (m) {
            return m;
        }
    });

    //绑定Ajax的内容
    $.getJSON(url, function (data) {
        control.empty();//清空下拉框
        $.each(data, function (i, item) {
            control.append("<option value='" + item.Value + "'>&nbsp;" + item.Text + "</option>");
        });
    });
}
//退出
function LoginOut() {
    $.ajax({
        url: "/ProMain/LoginOut",
        type: "post",
        success: function (data) {
            if (data.state != 1) {
                //$.layerMsg(data.message, "warning");
                top.layer.msg(data.message, { icon: 0, time: 2000 });
            }
            else {
                //退出成功
                $('#username').val("");
                var returnUrl = location.href;
                location.href = "/ProMain/Index?returnUrl=" + returnUrl;
            }
        }
    });
}
//修改密码
function modifyPwd() {
    layer.open({
        id: "modify",
        type: 2,
        title: "修改密码",
        area: ['400px', '180px'],
        content: ["/UserInfo/ModifyPwd", 'no']
    });
}

var btnIds = [];
//获取按钮列表
/*function GetButtonList() {
    btnIds = [];
    var tabId = $('.menubtnli.active', parent.document).children('a').attr('targe');// $(".menubtnli.active").children('a').attr('targe');
    if (tabId) {
        $.ajax({
            url: "/ButtonInfo/GetButtonListByTabId",
            type: "post",
            data: { tabId: tabId },
            async: false,
            success: function (data) {
                var result = eval(data);
                $(result).each(function (index, item) {
                    btnIds.push(item.ButtonType);
                })
            }
        });
    }
}
$(document).ready(function () {
    GetButtonList();
})*/
//获取grid选中的列
function GetSelectedCheckBox(tableID, checkBoxName) {
    var cklist = $('#' + tableID + " input[name='" + checkBoxName + "'][type='checkbox']:checked")
    var ids = [];
    $(cklist).each(function (index, elem) {
        var fid = $(elem).val();
        ids.push($.trim(fid));
    });
    //for (var i = 0; i < cklist.length; i++) {

    //    var fid = $(cklist[i]).parent().siblings('td[name="rid"]').html();
    //    ids.push($.trim(fid));
    //}
    return ids;
}
//刷新及展开第一个节点
//function CilckZtree(isRefrsh) {
//    var treeObj = $.fn.zTree.getZTreeObj("ztreediv");//获取ztree对象
//    var nodes = treeObj.getNodeByParam("id", pid);// treeObj.getNodes();
//    if (!nodes) {
//        nodes = treeObj.getNodeByParam("id", 11);// treeObj.getNodes();
//    }
//    treeObj.selectNode(nodes);//选择点
//    treeObj.setting.callback.onClick(null, treeObj.setting.treeId, nodes);//调用事件
//    treeObj.expandNode(nodes, true, false, true);//展开节点
//}
//单击树事件
function zTreeOnClick(event, treeId, treeNode) {
    pid = treeNode.id;

    //demo(pid);
}