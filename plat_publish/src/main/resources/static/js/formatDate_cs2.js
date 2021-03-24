
function formatTime2(v){  
	var d=new Date(parseInt(v)-8*60*60*1000).toLocaleStringss();
	if(d=="NaN-NaN-NaN NaN:NaN:NaN"){
		d="暂无";
	}
	return d;

}
Date.prototype.toLocaleStringss = function() {
	var y = this.getFullYear();  
    var m = this.getMonth()+1;  
    m = m<10?'0'+m:m;  
    var d = this.getDate();  
    d = d<10?("0"+d):d;  
     
    return y+"-"+m+"-"+d; 
}
