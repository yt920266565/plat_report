//<!--时间格式化-->
function formatTime(v){  
		var d=new Date(parseInt(v)-8*60*60*1000).toLocaleString();
		if(d=="NaN-NaN-NaN NaN:NaN:NaN"){
			d="暂无";
		}
		return d;
	
}
Date.prototype.toLocaleString = function() {
        var y = this.getFullYear();  
        var m = this.getMonth()+1;  
        m = m<10?'0'+m:m;  
        var d = this.getDate();  
        d = d<10?("0"+d):d;  
        var h = this.getHours();  
        h = h<10?("0"+h):h;  
        var M = this.getMinutes();  
        M = M<10?("0"+M):M;  
        var S=this.getSeconds();
        S=S<10?("0"+S):S;  
        return y+"-"+m+"-"+d+" "+h+":"+M+":"+S; 
}
