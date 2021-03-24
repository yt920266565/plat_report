function enterkeyDown(){
 	$('#keyWord').bind('keypress', function(event) {  
        if (event.keyCode == "13") {              
            event.preventDefault();   
            //回车执行查询  
            $('#btnSearch').click();  
        }  
    });
 	
}