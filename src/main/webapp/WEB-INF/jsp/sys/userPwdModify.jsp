<!DOCTYPE html>
<%@ page language='java' pageEncoding='UTF-8' contentType='text/html;charset=UTF-8' %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="en">
	<head>
   		<title>Bootstrap 实例 - 响应式表格</title>
   		<jsp:include  page="/pageStatic/seaboxBase.jsp"/>
   		<%@ include file="/pageStatic/taglibs.jsp" %>
	</head>
	<body>
		<!-- 弹出提示框 start -->
		<link href="${localProjectName}/pageCss/modal-div.css?v=${revision}" rel="stylesheet">
		<script src="${localProjectName}/pageJs/base/modal-div.js?v=${revision}"></script>
		<!-- 弹出提示框 end -->
		<link href="${localProjectName}/pageCss/sys/user/userAddEdit.css?v=${revision}" rel="stylesheet">
		<script src="${localProjectName}/pageJs/sys/userPwdModify.js?v=${revision}"></script>
		<div id="titleDiv" class="body-title">
			修改密码
		</div>
		<div id="splitDiv" class="split_title"><!-- title和内容页的分割线START-----splitDiv -->
		</div>	<!-- title和内容页的分割线END-----splitDiv -->
		<div id="centerdiv" class="contend_div" >
			<form id="modifyPwdForm" >
				<div class="error-message" id="uperror" style="display:none">
					<span id="pwdmsg" class="error-text"></span>
				</div>
				<table id="center-tab" class="center-tab-css">
					<tr >
						<td style="width:30%;">旧密码</td>
						<td style="text-align:left;width:70%;"><input id="oldpwd" name="oldpwd" class="form-control" type="password"/></td>
					</tr>
					<tr >
						<td >新密码</td>
						<td ><input id="newpwd" name="newpwd" type="password"  class="form-control"/> </td>
					</tr>
					<tr >
						<td >重复密码</td>
						<td ><input type="password" class="form-control" id="repeatpwd"  name="repeatpwd"/></td>
					</tr>
				</table>
			</form>
		</div>
		<div id="btn-div" class="contend_div" >
			<table id="btn-tab">
				<tr>
					<td style="width:30%;">
					</td>
					<td style="width:50%;height:100%;text-align:left;">
						<button id="confirmBtn" name="confirmBtn" type="button" class="btn save_btn" onclick="confirmModifyPwd()">确认</button>
					</td>
				</tr>
			</table>
		</div>
		<jsp:include  page="/pageStatic/seaboxBaseOnload.jsp"/>
	</body>
</html>
			