window.onresize = function() {//监控浏览器的窗口大小变化
	var mediaHeg = document.body.offsetHeight;
	var mediaWidth = document.body.offsetWidth;
//	alert(mediaHeg);
	var canUseHeight = mediaHeg-735;
	var canUseWidth = mediaWidth-735;
	$("#logInfoDiv").css("margin-top",canUseHeight/2+"px");
	$("#logInfoDiv").css("margin-left",canUseWidth/2+"px");
};

$(document).ready(function(){
	var mediaHeg = document.body.offsetHeight;
	var mediaWidth = document.body.offsetWidth;
//	alert(mediaHeg);
	var canUseHeight = mediaHeg-735;
	var canUseWidth = mediaWidth-735;
	$("#logInfoDiv").css("margin-top",canUseHeight/2+"px");
	$("#logInfoDiv").css("margin-left",canUseWidth/2+"px");

//	if(mediaHeg>900&&mediaHeg<1000){
//		$("#logInfoDiv").css("margin-top","25em");
//	}
//	
//	if(mediaHeg>800&&mediaHeg<900){
//		$("#logInfoDiv").css("margin-top","20em");
//	}
//	
//	if(mediaHeg<800&&mediaHeg>700){
//		$("#logInfoDiv").css("margin-top","15em");
//	}
//	if(mediaHeg<700&&mediaHeg>600){
//		$("#logInfoDiv").css("margin-top","12em");
//	}
	
	
//	alert(mediaHeg);
	$("#gthpng").hide();
	
});


if(window !=top){  
	top.location.href=location.href; 
}
function login(){
	//$("#subbutton").css("background-image","url(${baseHostUrl}/images/button-2.png)");
	$("#upbank").show();
	$("#uperror").hide();
	//$("#captchaerror").hide();
	var username = $("#username").val();
	var password = $("#password").val();
	if(!validate()){
		return;
	}
	//$("#loginBtn").attr("disabled","true");
	jQuery.ajax({
		type : 'post',
		//contentType : 'application/json;charset=utf-8',
		url : postUrl + '/sys/login.do',
		data : 'username='+username+'&password='+password,
		//dataType : 'json',
		success : function(result) {
			if(result.result=="success"){
				$("#uperror").show();
				$("#upbank").hide();
				$("#gthpng").hide();
				$("#pwdmsg").css("color","green");
				$("#pwdmsg").text(result.msg);
				window.location = postUrl + "/sys/mainframe.do";
			}else if(result.result=="errorpwd"){
				//$("#subbutton").css("background-image","url(${baseHostUrl}/images/button.png)");
				$("#uperror").show();
				$("#upbank").hide();
				$("#gthpng").show();
				$("#pwdmsg").css("color","red");
				$("#pwdmsg").text(result.msg);
				//$("#loginBtn").attr("disabled",null);
				//changeImg();
			}/* else if(data.result=="errorcaptcha"){
				$("#subbutton").css("background-image","url(${baseHostUrl}/images/button.png)");
				$("#cpmsg").text(data.msg);
				$("#loginBtn").attr("disabled",null);
				$("#captchaerror").show();
				changeImg();
			} */
		},
	}); 
}

function validate(){
	var username=$("#username").val();
	var password=$("#password").val();
	//var captcha=$("#captcha").val();
	if(username==null || username.trim()=="" ||password==null ||password.trim()==""){
			//$("#subbutton").css("background-image","url(${baseHostUrl}/images/button.png)");
			$("#uperror").show();
			$("#upbank").hide();
			$("#gthpng").show();
			$("#pwdmsg").css("color","red");
			$("#pwdmsg").text("用户名或密码不能为空");
			//$("#loginBtn").attr("disabled",null);
			return false;
	}
	/* if(captcha==null || captcha.trim()==""){
			$("#subbutton").css("background-image","url(${baseHostUrl}/images/button.png)");
			$("#cpmsg").text("验证码不能为空");
			$("#loginBtn").attr("disabled",null);
			$("#captchaerror").show();
			return false;
	} */
	return true;
}
/* function changeImg(){
	$("#captchaImage").attr("src","${baseHostUrl}/sys/captcha.do?id="+Math.random());
} */