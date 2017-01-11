var currPage=1;
var order_id="log_id";
var order_type="desc";

$(document).ready(function() {

});

/**
 * 获取用户列表数据
 */
function listData(pageDataObj) {
	var tbody = $("#tbody");
	var webAppRoot = "/" + window.location.pathname.substr(1).split('/')[0];
	currPage= pageDataObj.currPage;//当前页号
	$("#currPage").val(currPage);
	var pageSize= pageDataObj.pageSize;//当前页面显示条数
	var totalNum= pageDataObj.totalNum;//数据总数
	var dataList= pageDataObj.list;//当前页面元素集
	tbody.empty();
	if (dataList != null && dataList.length > 0) {
	} else {
		return;
	}
	for (var i = 0; i < dataList.length; i++) {
		var log_id = dataList[i].log_id;
		var log_type_nm = dataList[i].log_type_nm;
		if(log_type_nm == null){
			log_type_nm = "";
		}
		var str_log_ts = dataList[i].str_log_ts;
		var user_real_nm = dataList[i].user_real_nm;
		if(user_real_nm == null){
			user_real_nm = "";
		}
		var user_ip = dataList[i].user_ip;
		if(user_ip == null){
			user_ip = "";
		}
		var trObj = $("<tr></tr>");
		if (i % 2 != 0) {
			trObj.attr("class", "active");
		}
		trObj.append("<td class='tbody-td td_log_id' style='padding-top:14px;'>" + log_id + "</td>");
		trObj.append("<td class='tbody-td td_log_type' style='padding-top:14px;'>" + log_type_nm + "</td>");
		trObj.append("<td class='tbody-td td_log_time' style='padding-top:14px;'>" + str_log_ts + "</td>");
		trObj.append("<td class='tbody-td td_log_username' style='padding-top:14px;'>" + user_real_nm + "</td>");
		trObj.append("<td class='tbody-td td_log_ip' style='padding-top:14px;'>" + user_ip + "</td>");
		tbody.append(trObj);
	}

}


function orderList(orderColumn){

	update_Table_With_orderByColumn_Icon(orderColumn);

	showPageForSplit('/sys/log/findLogListByPage.do', listData, currPage);
}

function goToPage(){
	currPage = $("#targetPage").val();
	showPageForSplit('/sys/log/findLogListByPage.do', listData, currPage);
}

