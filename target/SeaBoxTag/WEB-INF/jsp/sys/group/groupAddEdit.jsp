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
		<link href="${localProjectName}/pageCss/sys/user/userAddEdit.css?v=${revision}" rel="stylesheet"/>
		<script src="${localProjectName}/pageJs/sys/group/groupAddEdit.js?v=${revision}"></script>
		<div id="titleDiv" class="body-title">
			用户组管理 <span class="secord-title"> > </span>
			<c:choose>
				<c:when test="${action == 'edit'}"><span class="secord-title">编辑用户组</span></c:when>
				<c:otherwise><span class="secord-title">新建用户组</span></c:otherwise>
			</c:choose>
		</div>
		<div id="splitDiv" class="split_title"><!-- title和内容页的分割线START-----splitDiv -->
		</div>	<!-- title和内容页的分割线END-----splitDiv -->
		<div id="centerdiv" class="contend_div" >
			<form id="addEditGroupForm" >
				<input type="hidden" id="action" name="action" value="${action}"/>
				<input type="hidden" id="currPage" name="currPage" value="${currPage}"/>
				<input type="hidden" id="edit_group_id" name="edit_group_id" value="${editGroup.group_id}"/>
				<input type="hidden" id="order_id" name="order_id" value="${order_id}" />
				<input type="hidden" id="order_type" name="order_type" value="${order_type}" />
				<c:if test="${action == 'edit'}"><input type="hidden" id="preadmin" name="preadmin" value="${editGroup.camp_admin_id}"/></c:if>
				<div class="error-message" id="uperror" style="display:none">
					<span id="pwdmsg" class="error-text"></span>
				</div>
				<table id="center-tab" class="center-tab-css">
				<!-- tr style="width:100%;height:15%;">
					<td style="width:30%;">登陆验证</td>
					<td style="width:70%;"><input id="username" class="form-control" type="text" value=""  ></td>
				</tr -->
					<tr >
						<td style="width:30%;">用户组名</td>
						<td style="text-align:left;width:70%;">
							<c:choose>
								<c:when test="${action == 'edit'}">
									<input id="groupname" name="groupname" class="form-control" type="text" value="${editGroup.group_org}_${editGroup.group_dep}" readonly="true">
								</c:when>
								<c:otherwise>
									<input id="groupname" name="groupname" class="form-control" type="text" readonly="true">
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
					<tr >
						<td >组织</td>
						<td ><input type="text" class="form-control" id="group_org" name="group_org" value="${editGroup.group_org}"/></td>
					</tr>
					<tr >
						<td >部门</td>
						<td ><input type="text" class="form-control" id="group_dep" name="group_dep" value="${editGroup.group_dep}"/></td>
					</tr>
					<tr >
						<td >活动管理员</td>
						<td >
							<c:choose>
								<c:when test="${action == 'edit'}">
									<c:choose>
										<c:when test="${fn:length(userList) == 0}">
											<select id="camp_admin_id" name="camp_admin_id" class="form-control">
									 			<option value="0">向该组加入用户后可选择管理员</option>
											</select>
										</c:when>
										<c:otherwise>
											<select id="camp_admin_id" name="camp_admin_id" class="form-control">
											 	<option value="0">暂不选择管理员</option>
											 	<c:forEach var="user" items="${userList}">
											 		<option value="${user.user_id}" <c:if test="${user.user_id == editGroup.camp_admin_id}">selected="selected"</c:if>>${user.user_real_nm}</option>
											 	</c:forEach>
											</select>
										</c:otherwise>
									</c:choose>
								</c:when>
								<c:otherwise>
									<select id="camp_admin_id" name="camp_admin_id" class="form-control">
									 	<option value="0">向该组加入用户后可选择管理员</option>
									</select>
								</c:otherwise>
							</c:choose>
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
			