function timeWork(){
	   $.ajax({
	        type: "post",
	        dataType: "json",
	        async: false,
	        url: "/timeLessList.action",
	        data: { nextStaName: '<%=staffCode%>'},
	        success: function (data) {
	        	var list = data.list;
	        	var lessList = data.lessList;
	        	var sortList = list.sort(function(a, b){
        			return a.dValue - b.dValue
        			//return 1 or 0 or -1
        		});
	        	
	        	console.log(lessList,123456)
	        	if(lessList.length>0){
	        		//当存在小于2小时的数据时
	        		layer.msg("你有"+lessList.length+"条信息快到时限，请尽快处理", {icon: 0,time:1000,end: function () {
	        			var index = parent.layer.getFrameIndex(window.name);
	                	parent.layer.close(index);}
	        		});
	        	}   
	        	if(sortList.length>0){
	        		var nextTime = sortList[0].dValue
	        		var stime = $("#timeId").val();
					console.log(stime,1234+"sadsda"+56);
	        		if(stime&&stime!=""){
	        			clearTimeout(stime);
	        		} 
					var timeId = setTimeout(function(){ 
						timeWork(); 
						}, nextTime-2.1*60*60*1000);
	        		 $("#timeId").val(timeId);
	        	}
	        	
	        	
	        },error:function (data){
	        	
	        }
	   });
	} 