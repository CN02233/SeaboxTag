//取页面高度
var bodyHeight = $(document).height();// 整个BODY高度
$(document).ready(function() {
	var otherHeight = bodyHeight - 170;
	var tableAllHeight = otherHeight * 0.8;// 整个TABLE占otherHeight的80%,如果80%的高度超过400px
	// 那么tableHeight设置为400px
	var btnAllHeight = otherHeight * 0.2;// 整个下方操作按钮占20%,如果20%的值超过50,那么btnAllHeight设置为50
	var tableDivHeight = tableAllHeight * 5 / 7;
	$("#centerdiv").css("height", tableDivHeight);
	$("#centerdiv").css("margin-top", tableAllHeight / 7);
	var actionValue = $("#action").val();
	var tableHeight = tableDivHeight;
	if (actionValue == 'add') {
		// 新增table高度设置
		if (tableDivHeight > 250) {
			tableHeight = 250;
		}
	} else {
		// 编辑table高度设置
		if (tableDivHeight > 100) {
			tableHeight = 100;
		}
		$("#centerdiv").css("height", tableAllHeight * 2 / 7);
	}
	if (btnAllHeight > 50)
		btnAllHeight = 50;

	$("#btn-div").css("height", btnAllHeight);
	$("#btn-tab").css("height", btnAllHeight);
	$("#center-tab").css("height", tableHeight);
});

function saveUser() {
	$("#uperror").hide();
	var currPage = $("#currPage").val();
	var order_id = $("#order_id").val();
	var order_type = $("#order_type").val();
	jQuery.ajax({
		type : 'post',
		// contentType : 'application/json',
		url : postUrl + '/sys/user/saveOneUser.do',
		data : $("#addEditUserForm").serialize(),
		// dataType : 'json',
		success : function(data) {
			if (data.result == "success") {
				$("#uperror").show();
				// $("#upbank").hide();
				$("#pwdmsg").removeClass();
				$("#pwdmsg").addClass("right-text");
				$("#pwdmsg").text(data.msg);
				window.location = postUrl + "/sys/user/main.do?currPage=" + currPage + "&order_id=" + order_id + "&order_type=" + order_type;
			} else if (data.result == "fail") {
				$("#uperror").show();
				$("#pwdmsg").removeClass();
				$("#pwdmsg").addClass("error-text");
				$("#pwdmsg").text(data.msg);
			}
		},
	});
}

function clickReturnBtn() {
	var currPage = $("#currPage").val();
	var order_id = $("#order_id").val();
	var order_type = $("#order_type").val();
	window.location = postUrl + "/sys/user/main.do?currPage=" + currPage + "&order_id=" + order_id + "&order_type=" + order_type;
}