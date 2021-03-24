function buttonType() {
	var ButtonType='';
	ButtonType += "<option>请选择</option>";
	for(var type=1;type<12;type++){
	    if(type==1) {  
	    	ButtonType += "<option value=\"" + type + "\" >编辑(通用)</option>";
	    } else if(type==3){ 
	    	ButtonType += "<option value=\"" + type + "\" >导出(通用)</option>";
 	    } else if(type==2){ 
 	    	ButtonType += "<option value=\"" + type + "\" >查询(通用)</option>";
	    } else if(type==4){ 
 	    	ButtonType += "<option value=\"" + type + "\" >新增</option>";
 	    } else if(type==6){ 
 	    	ButtonType += "<option value=\"" + type + "\" >删除</option>";
 	    } else if(type==5){ 
 	    	ButtonType += "<option value=\"" + type + "\" >修改</option>";
	    } else if(type==9){ 
 	    	ButtonType += "<option value=\"" + type + "\" >导出</option>";
	    } else if(type==8){ 
 	    	ButtonType += "<option value=\"" + type + "\" >导入</option>";
	    } else if(type==11){
 	    	ButtonType += "<option value=\"" + type + "\" >查看</option>";
	    } else if(type==10){ 
 	    	ButtonType += "<option value=\"" + type + "\" >其他</option>";
	    } else if(type==7){ 
 	    	ButtonType += "<option value=\"" + type + "\" >查询</option>";
	    } 
	}	
	$('#ButtonType').append(ButtonType);
}