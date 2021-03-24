$(document).ready(function(e) {
	$(this).keydown(function (e){
	if(e.which == "13"){
		var button = document.getElementById('btnConfirm');
			button.click();
	} 
	})
});	