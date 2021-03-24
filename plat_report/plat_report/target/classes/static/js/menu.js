$(document).ready(function () {
    
	$('.inactive').click(function(){
		if($(this).siblings('ul').css('display')=='none'){
			$(this).parent('li').siblings('li').removeClass('inactives');
			$(this).addClass('inactives');
			$(this).siblings('ul').slideDown(100).children('li');
			if($(this).parents('li').siblings('li').children('ul').css('display')=='block'){
				$(this).parents('li').siblings('li').children('ul').parent('li').children('a').removeClass('inactives');
				$(this).parents('li').siblings('li').children('ul').slideUp(100);

			}
		}else{
			//控制自身变成+号
			$(this).removeClass('inactives');
			//控制自身菜单下子菜单隐藏
			$(this).siblings('ul').slideUp(100);
			//控制自身子菜单变成+号
			$(this).siblings('ul').children('li').children('ul').parent('li').children('a').addClass('inactives');
			//控制自身菜单下子菜单隐藏
			$(this).siblings('ul').children('li').children('ul').slideUp(100);
			//控制同级菜单只保持一个是展开的（-号显示）
			$(this).siblings('ul').children('li').children('a').removeClass('inactives');
		}
	})
	var btn=$(".menubtnli");
	for(var i=0;i<btn.length;i++){
		btn[i].onclick=function(){
			for(var j=0;j<btn.length;j++){
				btn[j].className="menubtnli";
				$(btn[j]).children().css("color","#c2c2c2");
			}
			this.className="menubtnli active";
			$(this).children().css("color","#fff");
		}
	}
});
 
/*
* 利用递归方法拼接菜单html
* 初始化传递顶级的父级ID,例如0
*/
function getMenuHtml(json, pid) {
    //当前循环中的html
    var currHtmlStr = "";
    //子级的html
    var childStr = "";
    currHtmlStr += "<ul>";
    for (var i = 0; i < json.length; i++) {
        if (json[i].fatherId == pid) {
            //判断是否有子级
            if (ExamChild(json, json[i].TId)) {
                currHtmlStr += "<li class='menu_li'><a href='" + json[i].PageUrl + "' class='inactive' targe='" + json[i].TId + "'>" +
                "<i class='" + json[i].Icon + "' aria-hidden='true'></i> " + json[i].TabName + "</a>";
                //递归子级菜单
                childStr = getMenuHtml(json, json[i].TId);
                //若子层有菜单就和当前菜单拼接上去
                if (childStr != "") {
                    currHtmlStr += childStr;
                }
                currHtmlStr += "</li>";
            }
            else {
                //最底层
                currHtmlStr += "<li class='menubtnli'><a data-href=" + json[i].PageUrl +
                " data-title=" + json[i].TabName + " onclick='Hui_admin_tab(this)' targe='" + json[i].TId + "'>" +
                "<i class='" + json[i].Icon + "' aria-hidden='true'></i> " +
                json[i].TabName + "</a></li>";
            }
        }
        //排除已用数据,减少循环次数
        //json.splice(i, 1);
    }
    currHtmlStr += "</ul>";

    return currHtmlStr;
}

//检查是否还有子级菜单
function ExamChild(json, pid) {
    var result = false;
    for (var i = 0; i < json.length; i++) {
        if (json[i].fatherId == pid) {
            result = true;
            break;
        }
    }
    return result;
}