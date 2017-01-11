function initCheckBox(){

	$(".iconCheckBox input").change(function() {
		var checkBoxObj = $(this);
		var labelObj = $(this).next();
		var id=checkBoxObj.attr("id");
		var isCheck = document.getElementById(id).checked;
		if(isCheck){//选中状态
			$(labelObj).css("background-position","-83px -26px");
		}else{//取消状态
			$(labelObj).css("background-position","-64px -26px");
		}
	});

} 
