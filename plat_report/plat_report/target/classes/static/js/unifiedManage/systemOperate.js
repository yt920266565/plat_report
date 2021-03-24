$(document).ready(function () {
	init();
	function init(){
		layui.use('table', function(){
		  var table = layui.table
		  ,form = layui.form;
		  
		  table.render({
		    elem: '#data'
		    ,url:'/system!list.action'
		    ,cols: [[
		      {type:'checkbox', fixed: 'left'}
		      ,{field:'SysCode', sort: true, fixed: true,title:'编码'}
		      ,{field:'SysName', title:'名称'}
		      ,{field:'SysURL', title:'访问路径'}
		      ,{field:'SysImage',templet:'#imgs',title:'图片'}
		      ,{field:'Remark', title:'备注'}
		      ,{fixed: 'right', align:'center', toolbar: '#barDemo',title:'操作'}
		    ]]
		    ,page: true
		  });
		});
	}
	layui.use('table', function(){
		  var table = layui.table;
		  //监听表格复选框选择
		  table.on('checkbox(demo)', function(obj){
		    console.log(obj)
		  });
		  //监听工具条
		  table.on('tool(demo)', function(obj){
		    var data = obj.data;
		    var PN = '';
		    if(obj.event === 'detail'){
		    	layer.open({
		    	    type: 2,
		    	    title: '系统信息',
		    	    shadeClose: true,
		    	    shade: 0.8,
		    	    area: ['780px', '60%'],
		    	    content: '/app/jsp/unifiedManage/system/systemM/sysInfo.jsp?SysCode='+data.SysCode+"&SysName="+data.SysName+"&SysURL="+data.SysURL+"&SysImage="+data.SysImage+"&Remark="+data.Remark
		    	}); 
		    } else if(obj.event === 'del'){
		      layer.confirm('真的删除它吗？', function(index){
		   		  var input = {SysCode:data.SysCode};
		  		  var fun = function (data) {
		              layer.close(index); 
		              init();
		  		  };
		  		  $.post("/deleteSystem.action",input,fun,"json");
		  		  
		      });
		    } else if(obj.event === 'edit'){
		    	layer.open({
		    	    type: 2,
		    	    title: '修改系统信息',
		    	    shadeClose: true,
		    	    shade: 0.8,
		    	    area: ['750px', '55%'],
		    	    content: '/app/jsp/unifiedManage/system/systemM/updateSys.jsp?SysCode='+data.SysCode+"&SysName="+data.SysName+"&SysURL="+data.SysURL+"&SysImage="+data.SysImage+"&Remark="+data.Remark,
		    	    end:function(){
		    	    	init();
		    	    }
		    	}); 
		    	 
		    }
		  });
		  
		  var $ = layui.$, active = {
		    getCheckData: function(){ //获取选中数据 
	    		layer.open({
	    		    type: 2,
	    		    title: '添加系统信息',
	    		    shadeClose: true,
	    		    shade: 0.8,
	    		    area: ['750px', '75%'],
	    		    content: '/app/jsp/unifiedManage/system/systemM/addSys.jsp',
	    	    }); 	 
		    }
		    ,getCheckLength: function(){ //获取选中数目
		      var checkStatus = table.checkStatus('data')
		      ,data = checkStatus.data;
		      layer.msg('选中了：'+ data.length + ' 个');
		    }
		    ,deLink:function () {
	             var checkStatus=table.checkStatus('data'),
	                 data=checkStatus.data,
	                 deList=[];
	             data.forEach(function(n,SysId){
	                  deList.push(n.SysId);
	             });
	             if(deList!=''){
	                  layer.confirm("确定删除所选项吗？",function (index) {
	                     $.ajax({
	                         url: '/batchDelete.action',
	                         type:'post',
	                         dataType:'json',
	                         data:"id="+deList,
	                         success:function (data,statusText) {
	                              if(data.code==='0'){
	                                  layer.msg('删除成功');
	                                  table.reload('data',{});
	                              }else{
	                                  layer.msg('删除失败');
	                             }
	                         },
	                         error:function () {
	                              layer.msg('系统错误');
	                         }
                         });
	                 });
	              }else{
	            	  layer.msg('请选中要删除的数据!', {icon: 6});
	              }
	          }
		    ,isAll: function(){ //验证是否全选
		      var checkStatus = table.checkStatus('data');
		      layer.msg(checkStatus.isAll ? '全选': '未全选')
		    }

		  };
		  
		  $('.demoTable .layui-btn').on('click', function(){
		    var type = $(this).data('type');
		    active[type] ? active[type].call(this) : '';
		  });
	});
	 
	
});  