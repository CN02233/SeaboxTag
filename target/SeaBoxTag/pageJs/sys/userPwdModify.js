//取页面高度
var bodyHeight = $(document).height();// 整个BODY高度

$(document).ready(function() {
	var otherHeight = bodyHeight - 170;
	var tableAllHeight = otherHeight * 0.8;// 整个TABLE占otherHeight的80%,如果80%的高度超过400px
											// 那么tableHeight设置为400px
	var btnAllHeight = otherHeight * 0.2;// 整个下方操作按钮占20%,如果20%的值超过50,那么btnAllHeight设置为50
	var tableDivHeight = tableAllHeight * 3 / 7;
	$("#centerdiv").css("height", tableDivHeight);
	$("#centerdiv").css("margin-top", tableAllHeight / 7);
	var actionValue = '${action}';
	var tableHeight = tableDivHeight;
	tableHeight = 150;
	if (btnAllHeight > 50)
		btnAllHeight = 50;
	$("#btn-div").css("height", btnAllHeight);
	$("#btn-tab").css("height", btnAllHeight);
	$("#center-tab").css("height", tableHeight);
	$('#usergroup').css("height", "100%");
	$('#usergroup').multiselect();
	$('#example-reset-button').click(function() {
		$('#usergroup option:selected').each(function() {
			$(this).prop('selected', false);
		})
		$('#usergroup').multiselect('refresh');
	});
});

function confirmModifyPwd() {
	$("#uperror").hide();
	jQuery.ajax({
		type : 'post',
		url : postUrl + '/sys/modifyUserPwd.do',
		data : $("#modifyPwdForm").serialize(),
		success : function(data) {
			if (data.result == "success") {
				$("#uperror").show();
				// $("#upbank").hide();
				// $("#pwdmsg").removeClass();
				// $("#pwdmsg").addClass("right-text");
				// $("#pwdmsg").text(data.msg);
				$("#pwdmsg").hide();
				$("#oldpwd").val("");
				$("#newpwd").val("");
				$("#repeatpwd").val("");
				var params = new Object();
				params = {
					hide_back_btn : true
				};
				createModalAlter('密码已成功修改', '取消', '确认', hideCancelBtn, params);
			} else if (data.result == "fail") {
				$("#uperror").show();
				$("#pwdmsg").removeClass();
				$("#pwdmsg").addClass("error-text");
				$("#pwdmsg").text(data.msg);
			}
		},
	});
}

function hideCancelBtn() {
}