$(document).ready(function () {
	layui.use('upload', function(){
		var $ = layui.jquery
		,upload = layui.upload;
		var uploadInst = upload.render({
		    elem: '#test1'
		    ,url: '/upload.action'
		     
		    ,before: function(obj){
		      //预读本地文件示例，不支持ie8
		      obj.preview(function(index, file, result){
		        $('#demo1').attr('src', result); //图片链接（base64）
		      });
		    }
		    ,done: function(res){
		      //如果上传失败
		      if(res.code == 1){
		    	  var url = '';
		    	  url +='/images/'+res.imgName
	 	          $('#demo1').attr('src', url); 
	 	          return layer.msg('上传成功');
		      }else{
		    	  var url = '';
			      url +='/images/'+res.imgName
		 	      $('#demo1').attr('src', url); 
			      return layer.msg('上传成功');
		      }
		      //上传成功
		    }
		    ,error: function(){
		      //演示失败状态，并实现重传
		      var demoText = $('#demoText');
		      demoText.html('<span style="color: #FF5722;">上传失败</span> <a class="layui-btn layui-btn-mini demo-reload">重试</a>');
		      demoText.find('.demo-reload').on('click', function(){
		        uploadInst.upload();
		      });
		    }
		});
	});
});