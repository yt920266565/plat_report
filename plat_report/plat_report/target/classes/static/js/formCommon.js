//验证必填项非空,formobj 要验证的form里面的必填项
function IsNull(formobj) {
    var $notNullitem;
    if (formobj) {
        $notNullitem = $(formobj).find("input[lay-verify=required]");
    }
    else {
        $notNullitem = $("input[lay-verify=required]");
    }
    var vilaresult = true;
    for (var i = 0; i < $notNullitem.length; i++) {

        var itemval = $notNullitem[i].value;
        var warmStr = $($notNullitem[i]).attr("placeholder");
        if (itemval == "" || itemval == undefined) {
            $.layerMsg(warmStr == "" ? "必填项不能为空" : warmStr, "warning");
            $($notNullitem[i]).addClass("layui-form-danger");
            $($notNullitem[i]).focus();
            vilaresult = false;
            return;
        }
        //else if (warmStr && warmStr.indexOf('身份证号码')>0) {
        //    vilaresult = IdentityCodeValid(itemval);
        //    if (!vilaresult) {
        //        $($notNullitem[i]).focus();
        //        return;
        //    }
        //}
    }
    //$notNullitem.each(function (index, item) {
    //    var itemval = $(item).val();
    //    var warmStr = $(item).attr("placeholder");
    //    if (itemval == "" || itemval == undefined) {
    //        $.layerMsg(warmStr == "" ? "必填项不能为空" : warmStr, "warning");
    //        $(item).addClass("layui-form-danger");
    //        $(item).focus();
    //        vilaresult = false;
    //        return;
    //    }
    //})
    return vilaresult;
}
//身份证号合法性验证 
//支持15位和18位身份证号
//支持地址编码、出生日期、校验位验证
function IdentityCodeValid(code) {
    var city = { 11: "北京", 12: "天津", 13: "河北", 14: "山西", 15: "内蒙古", 21: "辽宁", 22: "吉林", 23: "黑龙江 ", 31: "上海", 32: "江苏", 33: "浙江", 34: "安徽", 35: "福建", 36: "江西", 37: "山东", 41: "河南", 42: "湖北 ", 43: "湖南", 44: "广东", 45: "广西", 46: "海南", 50: "重庆", 51: "四川", 52: "贵州", 53: "云南", 54: "西藏 ", 61: "陕西", 62: "甘肃", 63: "青海", 64: "宁夏", 65: "新疆", 71: "台湾", 81: "香港", 82: "澳门", 91: "国外 " };
    var tip = "";
    var pass = true;

    if (!code || !/^\d{6}(18|19|20)?\d{2}(0[1-9]|1[12])(0[1-9]|[12]\d|3[01])\d{3}(\d|X)$/i.test(code)) {
        tip = "身份证号格式错误";
        pass = false;
    }

    else if (!city[code.substr(0, 2)]) {
        tip = "地址编码错误";
        pass = false;
    }
    else {
        //18位身份证需要验证最后一位校验位
        if (code.length == 18) {
            code = code.split('');
            //∑(ai×Wi)(mod 11)
            //加权因子
            var factor = [7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2];
            //校验位
            var parity = [1, 0, 'X', 9, 8, 7, 6, 5, 4, 3, 2];
            var sum = 0;
            var ai = 0;
            var wi = 0;
            for (var i = 0; i < 17; i++) {
                ai = code[i];
                wi = factor[i];
                sum += ai * wi;
            }
            var last = parity[sum % 11];
            if (parity[sum % 11] != code[17]) {
                tip = "校验位错误";
                pass = false;
            }
        }
    }
    if (!pass) $.layerMsg(tip, "warning");
    return pass;
}
var todocount = 0;
var waycount = 0;
function settitle(obj, count) {
    var thl = $(obj).html()
    var kcount = thl.indexOf('(');
    var ecount = thl.indexOf(')');
    var newcountstr = "(" + count + ")";
    //if (kcount > -1) {
    var newthl = kcount > -1 ? thl.substr(0, kcount) + newcountstr + thl.substr(ecount, thl.lenght - 1) : thl + newcountstr;
    //}
    $(obj).html(newthl);
}
//获取待办数量及在途数量
function gettodocount() {
    $.ajax({
        url: '/MyWorkFlow/GetTodoCount',
        contentType: 'application/json',
        type: "post",
        dataType: "json",
        async: false,
        //data: { },
        success: function (data) {
            todocount = data.todocount;
            waycount = data.waycount;
            settitle(top.$("a[data-href='/MyWorkFlow/NeedDealting']"), todocount);//设置待办
            //settitle(top.$("a[data-href='/MyWorkFlow/OnWay']"), waycount);//设置在途
            //if (todocount > 0) {
            //    var thl = top.$("a[data-href='/MyWorkFlow/NeedDealting']").text()
            //    var kcount = thl.indexOf('(');
            //    var newcountstr = "(" + todocount + ")";
            //    var newthl = thl;
            //    if (kcount > -1) {
            //        newthl = thl.substr(kcount, thl.length - 1);
            //    }
            //    top.$("a[data-href='/MyWorkFlow/NeedDealting']").text(newthl + kcount);
            //}
            //if (waycount > 0) {
            //    var thl = top.$("a[data-href='/MyWorkFlow/OnWay']").text()
            //    var kcount = thl.indexOf('(');
            //    var newcountstr = "(" + todocount + ")";
            //    var newthl = thl;
            //    if (kcount > -1) {
            //        newthl = thl.substr(kcount, thl.length - 1);
            //    }
            //    top.$("a[data-href='/MyWorkFlow/OnWay']").text(newthl + kcount);
            //}
        }
    })
}

/*****上传附件********/
var uploader;
function upload(obj) {
    obj = obj || 1;
    var $ = jQuery,
$list = $('#thelist_' + obj),
//$btn = $('#ctlBtn_' + obj),
state = 'pending',

    // 优化retina, 在retina下这个值是2
    ratio = window.devicePixelRatio || 1,

    // 缩略图大小
    thumbnailWidth = 100 * ratio,
    thumbnailHeight = 100 * ratio,

    uploader = WebUploader.create({
        // 自动上传。
        //auto: true,

        // 不压缩image
        resize: false,

        // swf文件路径
        swf: 'Content/image/Uploader.swf',

        // 文件接收服务端。
        server: '/HolApplica/SaveFiles',

        // 选择文件的按钮。可选。
        // 内部根据当前运行是创建，可能是input元素，也可能是flash.
        pick: '#picker_'+obj
    });


    // 当有文件添加进来的时候
    uploader.on('fileQueued', function (file) {
        var $li = $(
        '<div id="' + file.id + '" class="file-item thumbnail">' +
            '<img>' + '<div class="info">' + file.name + '</div>' +
        '</div>'
        ),
    $img = $li.find('img');
        var idstr = file.id.split('_');

        //var i = idstr.length > 0 ? idstr[idstr.length - 1] : 0;
        //+ '&nbsp;&nbsp;<input type="button" value="删除" class="btn btn-primary radius" onclick=delfile("' + i + '",this) >'
        $list.append('<div id="' + file.id + '" class="item">' +
    '<h4 class="info">' + '</h4>' +
    '<p class="state">' + file.name + ':等待上传...</p>' +
'</div>');
        //$list.append($li);

        // 创建缩略图
        //        uploader.makeThumb(file, function (error, src) {
        //            if (error) {
        //                $img.replaceWith('<span>不能预览</span>');
        //                return;
        //            }

        //            $img.attr('src', src);
        //        }, thumbnailWidth, thumbnailHeight);
    });

    // 文件上传过程中创建进度条实时显示。
    uploader.on('uploadProgress', function (file, percentage) {
        var $li = $('#' + file.id),
    $percent = $li.find('.progress .progress-bar');
        // 避免重复创建
        if (!$percent.length) {
            $percent = $('<div class="progress progress-striped active">' +
      '<div class="progress-bar" role="progressbar" style="width: 0%">' +
      '</div>' +
    '</div>').appendTo($li).find('.progress-bar');
        }
        $li.find('p.state').text('上传中');
        $percent.css('width', percentage * 100 + '%');
    });

    uploader.on('uploadSuccess', function (file, response) {
        $('#' + file.id).find('p.state').text('已上传');
        var imgurl = response;
        var imags = imgurl.split(',');
        for (var i = 0; i < imags.length; i++) {
            var filenames = imags[i].split('\\');
            var filename = filenames[filenames.length - 1];
            if (obj) {
                $("#uploaded_"+obj).append("<div><a href='" + imags[i] + "' id='upload" + i + "'>" + filename + "</a>&nbsp;&nbsp;<input type='button' value='删除' class='btn btn-primary radius' onclick=delfile('" + i + "',this) ><br/></div>");
            }
            else {
                $("#uploaded").append("<div><a href='" + imags[i] + "' id='upload" + i + "'>" + filename + "</a>&nbsp;&nbsp;<input type='button' value='删除' class='btn btn-primary radius' onclick=delfile('" + i + "',this) ><br/></div>");
            }
        }
        //alert(imgurl);
    });

    uploader.on('uploadError', function (file) {
        $('#' + file.id).find('p.state').text('上传出错');
    });

    uploader.on('uploadComplete', function (file) {
        $('#' + file.id).find('.progress').fadeOut();
    });

    uploader.on('all', function (type) {
        if (type === 'startUpload') {
            state = 'uploading';
        } else if (type === 'stopUpload') {
            state = 'paused';
        } else if (type === 'uploadFinished') {
            state = 'done';
        }
        if (state === 'uploading') {
            $('#ctlBtn_' + obj).text('暂停上传');
        } else {
            $('#ctlBtn_' + obj).text('开始上传');
        }
        if (type === 'uploadFinished') {
            clear(obj);
            //refresh();
        }
    });
    // 所有文件上传成功后调用
    uploader.on('uploadFinished', function () {
        //清空队列
        uploader.reset();
    });
    $('#ctlBtn_' + obj).on('click', function () {
        if (state === 'uploading') {
            uploader.stop();
            //buttom按钮阻止提交
            return false;
        } else {
            uploader.upload();
            //buttom按钮阻止提交
            return false;
        }
    });
}
//删除上传的文件
function delfile(i, obj, isdata) {
    var fileid = 0;
    if (isdata) {
        fileid = i;
    }
    var url = $(obj).prev("a").attr("href");
    $.ajax({
        url: '/HolApplica/deletefile',
        type: 'post',
        data: { fileurl: url, fileid: fileid },
        success: function (data) {
            if (data.result == true) {
                $(obj).parent().remove();
                //if (isdata) {
                //    $("#file" + i).remove();
                //}
                //else {
                //    $(obj).parent().remove();
                //   // $("#upload" + i).remove();
                //}
                //$(obj).next("br").remove();
                //$(obj).remove();
                //uploader.reset();
                //uploader.removeFile(fileid);
            }
            else {
                alert("删除失败！");
            }
        }
    })
}
function clear(obj) {
    $('#thelist_' + obj).html('');
}
/*****上传附件结束****/

//去掉前后空格
function Trim(str) {
    return str.replace(/(^\s*)|(\s*$)/g, "");
}
//页面转码解码
var HtmlUtil = {
    /*1.用浏览器内部转换器实现html转码*/
    htmlEncode: function (html) {
        //1.首先动态创建一个容器标签元素，如DIV
        var temp = document.createElement("div");
        //2.然后将要转换的字符串设置为这个元素的innerText(ie支持)或者textContent(火狐，google支持)
        (temp.textContent != undefined) ? (temp.textContent = html) : (temp.innerText = html);
        //3.最后返回这个元素的innerHTML，即得到经过HTML编码转换的字符串了
        var output = temp.innerHTML;
        temp = null;
        return output;
    },
    /*2.用浏览器内部转换器实现html解码*/
    htmlDecode: function (text) {
        //1.首先动态创建一个容器标签元素，如DIV
        var temp = document.createElement("div");
        //2.然后将要转换的字符串设置为这个元素的innerHTML(ie，火狐，google都支持)
        temp.innerHTML = text;
        //3.最后返回这个元素的innerText(ie支持)或者textContent(火狐，google支持)，即得到经过HTML解码的字符串了。
        var output = temp.innerText || temp.textContent;
        temp = null;
        return output;
    }
};