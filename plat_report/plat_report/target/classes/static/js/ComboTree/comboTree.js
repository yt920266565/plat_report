


function listToTree(list,pid) {
		var ret = [];//一个存放结果的临时数组
		for(var i=0; i<list.length;i++) {
			if(list[i].pId == pid) {//如果当前项的父id等于要查找的父id，进行递归
			 	list[i].children = listToTree(list, list[i].id);
			 	ret.push(list[i]);//把当前项保存到临时数组中
			}
		}
		return ret;//递归结束后返回结果
} 

var j="";//前缀符号，用于显示父子关系，这里可以使用其它符号
function creatSelectTree(d){
		 var option="";
		 for(var i=0;i<d.length;i++){
			 if(d[i].children.length){//如果有子集
			  	option+="<option value='"+d[i].id+"'>"+j+d[i].name+"</option>";
				 j+="&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp";//前缀符号加一个符号
			 	 option+=creatSelectTree(d[i].children);//递归调用子集
			 	j=j.slice(0,j.length-30);//每次递归结束返回上级时，前缀符号需要减一个符号
			  }else{//没有子集直接显示
			 	 option+="<option value='"+d[i].id+"'>"+j+d[i].name+"</option>";
			  }
	  }
	 return option;//返回最终html结果
}