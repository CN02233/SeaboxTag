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
		<link href="${localProjectName}/pageCss/sys/user/userAddEdit.css?v=${revision}" rel="stylesheet">
		<script src="${localProjectName}/pageJs/sys/user/userViewList.js?v=${revision}"></script>
		<script src="${localProjectName}/pageJs/sys/user/userAddEdit.js?v=${revision}"></script>
		<div id="titleDiv" class="body-title">
			用户管理 <span class="secord-title"> > </span>
			<c:choose>
				<c:when test="${action == 'edit'}"><span class="secord-title">编辑用户</span></c:when>
				<c:otherwise><span class="secord-title">新建用户</span></c:otherwise>
			</c:choose>
		</div>
		<div id="splitDiv" class="split_title"><!-- title和内容页的分割线START-----splitDiv -->
		</div>	<!-- title和内容页的分割线END-----splitDiv -->
		<div id="centerdiv" class="contend_div" >
			<form id="addEditUserForm" >
				<input type="hidden" id="action" name="action" value="${action}"/>
				<input type="hidden" id="currPage" name="currPage" value="${currPage}"/>
				<input type="hidden" id="edit_user_id" name="edit_user_id" value="${editUser.user_id}"/>
				<input type="hidden" id="order_id" name="order_id" value="${order_id}" />
				<input type="hidden" id="order_type" name="order_type" value="${order_type}" />
				<c:if test="${action == 'edit'}"><input type="hidden" id="pregroup" name="pregroup" value="${editUser.user_group_id}"/></c:if>
				<div class="error-message" id="uperror" style="display:none">
					<span id="pwdmsg" class="error-text"></span>
				</div>
				<table id="center-tab" class="center-tab-css">
				<!-- tr style="width:100%;height:15%;">
					<td style="width:30%;">登陆验证</td>
					<td style="width:70%;"><input id="username" class="form-control" type="text" value=""  ></td>
				</tr -->
					<c:if test="${action == 'add'}">
						<tr >
							<td style="width:30%;">用户名</td>
							<td style="text-align:left;width:70%;"><input id="username" name="username" class="form-control" type="text" value="${editUser.username}" ></td>
						</tr>
						<tr >
							<td >密码</td>
							<td ><input id="password" name="password" type="password"  class="form-control"/> </td>
						</tr>
						<tr >
							<td >确认密码</td>
							<td ><input type="password" class="form-control" id="confpwd"  name="confpwd"/></td>
						</tr>
					</c:if>
					<tr >
						<td >真实姓名</td>
						<td ><input type="text" class="form-control" id="realname" name="realname" value="${editUser.user_real_nm}"/></td>
					</tr>
					<tr >
						<td >选择用户组</td>
						<td >
							<select id="usergroup" name="usergroup" class="form-control">
							 	<option value="0">暂不加入用户组</option>
							 	<c:forEach var="group" items="${groupList}">
							 		<option value="${group.group_id}" <c:if test="${group.group_id == editUser.user_group_id}">selected="selected"</c:if>>${group.group_name}</option>
							 	</c:forEach>
							</select>
						</td>
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
						<button id="saveUserBtn" name="saveUserBtn" type="button" class="btn save_btn" onclick="saveUser()">保存</button>
						<td style="width:20%;height:100%;text-align:right;">
							<a href="#" onclick="clickReturnBtn()">返回</a>
						</td>
					</td>
				</tr>
			</table>
		</div>
		<jsp:include  page="/pageStatic/seaboxBaseOnload.jsp"/>
	</body>
</html>
			