<!DOCTYPE html>
<%@ page language='java' pageEncoding='UTF-8' contentType='text/html;charset=UTF-8' %>
<html lang="en">
	<head>
   		<title>Bootstrap 实例 - 响应式表格</title>
   		<jsp:include  page="/pageStatic/seaboxBase.jsp"/>
	</head>
	<body>
	    <link href="${localProjectName}/pageCss/sys/common/dataTable.css?v=${revision}" rel="stylesheet">
		<script src="${localProjectName}/pageJs/sys/log/logViewList.js?v=${revision}"></script>
		<script>
			$(document).ready(function(){
				var currPage = $("#currPage").val();
				showPageForSplit('/sys/log/findLogListByPage.do',listData,currPage);
				//searchDataFromBack('/evt/evtView/listMainData.do',listData);
			});
		</script>
		<div id="searchDiv" class="title-search-div" style="display:none">
			<input type="hidden" id="order_id" name="order_id" value="${order_id}" />
			<input type="hidden" id="order_type" name="order_type" value="${order_type}" />
		</div>
		<input type="hidden" id="currPage" name="currPage" value="${currPage}" />
		<div id="titleDiv" class="body-title">
			日志
		</div>
		<div id="splitDiv" class="split_title"><!-- title和内容页的分割线START-----splitDiv -->
		</div>	<!-- title和内容页的分割线END-----splitDiv -->
		<div id="operationDiv" class="no_operation_div" >
		</div><!-- operationDiv end 操作按钮DIV -->
		<div class="table-responsive data-list-table-div">
			<table class="seabox-table"> <!-- table size and style are aligned in "seabox-table" -->
				<tbody>
				<tr class="thread-tr">
					<th class="title_td_css td_log_id">
						<span id="log_id" onclick="orderList(this)" class='glyphicon'>事件ID</span>
					</th>
					<th class="title_td_css td_log_type">
						<span id="log_type" onclick="orderList(this)" class='glyphicon'>事件类型</span>
					</th>
					<th class="title_td_css td_log_time">
						<span id="log_ts" onclick="orderList(this)" class='glyphicon'>时间</span>
					</th>
					<th class="title_td_css td_log_username">
						<span id="user_real_nm" onclick="orderList(this)" class='glyphicon'>用户真实姓名</span>
					</th>
					<th class="title_td_css td_log_ip">
						<span id="user_ip" onclick="orderList(this)" class='glyphicon'>IP</span>
					</th>
				</tr>
				</tbody>
			</table>
			<table class="table table-hover seabox-table" id="log_main_table">
				<!-- table size and style are aligned in "seabox-table" -->
				<tbody id="tbody"></tbody>
			</table>
			<div id="paginationDiv" class="pageit_div">
			</div>
		</div>
		<jsp:include  page="/pageStatic/seaboxBaseOnload.jsp"/>
	</body>
</html>
			