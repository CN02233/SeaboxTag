<!DOCTYPE html>
<%@ page language='java' pageEncoding='UTF-8' contentType='text/html;charset=UTF-8' %>
<html lang="en">
	<head>
   		<title>Bootstrap 实例 - 响应式表格</title>
   		<jsp:include  page="/pageStatic/seaboxBase.jsp"/>
	</head>
	<body>
	    <link href="${localProjectName}/pageCss/sys/common/dataTable.css?v=${revision}" rel="stylesheet">
		<script src="${localProjectName}/pageJs/sys/group/groupViewList.js?v=${revision}"></script>
		<!-- 弹出提示框 start -->
		<link href="${localProjectName}/pageCss/modal-div.css?v=${revision}" rel="stylesheet">
		<script src="${localProjectName}/pageJs/base/modal-div.js?v=${revision}"></script>
		<!-- 弹出提示框 end -->
		<div id="searchDiv" class="title-search-div" style="display:none">
			<input type="hidden" id="order_id" name="order_id" value="${order_id}" />
			<input type="hidden" id="order_type" name="order_type" value="${order_type}" />
		</div>
		<input type="hidden" id="currPage" name="currPage" value="${currPage}" />
		<div id="titleDiv" class="body-title">
			用户组管理
		</div>
		<div id="splitDiv" class="split_title"></div><!-- title和内容页的分割线Div -->
		<div id="operationDiv" class="operation_div" >
			<ul class="first">   	
				<button type="button" class="btn btn-active" style="width:100px" onclick="clickAddBtn();"  data-toggle="button">新建用户组</button>
			</ul>
		</div><!-- operationDiv end 操作按钮DIV -->
		<div class="table-responsive data-list-table-div">
			<table class="seabox-table">  <!-- table size and style are aligned in "seabox-table" -->
				<tbody>
				<tr class="thread-tr">
					<th class="title_td_css td_group_id" style="text-align:center;">
						<span id="group_id" onclick="orderList(this)" class='glyphicon'>ID</span>
					</th>
					<th class="title_td_css td_group_name" style="text-align:center;">
						<span id="group_nm" onclick="orderList(this)" class='glyphicon'>用户组名</span>
					</th>
					<th class="title_td_css td_group_have_users" style="text-align:center;">
						<span id="group_user" class='glyphicon' style="cursor:default;">组用户</span>
					</th>
					<th class="title_td_css td_group_admin" style="text-align:center;">
						<span id="camp_admin_name" onclick="orderList(this)" class='glyphicon'>活动管理员</span>
					</th>
					<th class="title_td_css td_group_creator" style="text-align:center;">
						<span id="create_name" onclick="orderList(this)" class='glyphicon'>创建者</span>
					</th>
					<th class="title_td_css td_group_date_and_time" style="text-align:center;">
						<span id="create_ts" onclick="orderList(this)" class='glyphicon'>创建时间</span>
					</th>
					<th class="title_td_css td_group_modifier" style="text-align:center;">
						<span id="update_name" onclick="orderList(this)" class='glyphicon'>修改者</span>
					</th>
					<th class="title_td_css td_group_date_and_time" style="text-align:center;">
						<span id="update_ts" onclick="orderList(this)" class='glyphicon'>修改时间</span>
					</th>
					<th class="title_td_css td_user_operation" style="text-align:center;">
						<span class='glyphicon' style="cursor:default;">操作</span>
					</th>
				</tr>
				</tbody>
			</table>

			<table class="table table-hover seabox-table" id="group_main_table">
				<!-- table size and style are aligned in "seabox-table" -->
				<tbody id="tbody"></tbody>
			</table>

			<div id="paginationDiv" class="pageit_div"></div>
		</div>
		<jsp:include  page="/pageStatic/seaboxBaseOnload.jsp"/>
	</body>
</html>
			