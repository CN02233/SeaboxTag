var currPage=1;
var order_id="user_id";
var order_type="asc";

$(document).ready(function() {
	var currPage = $("#currPage").val();
	showPageForSplit('/sys/user/findUserListByPageNoAdmin.do',listData,currPage);
});

function clickAddBtn() {
	var currPage = $("#currPage").val();
	var order_id = $("#order_id").val();
	var order_type = $("#order_type").val();
	window.location = postUrl + '/sys/user/userAddEdit.do?action=add&currPage='
			+ currPage + "&order_id=" + order_id + "&order_type=" + order_type;
}

/**
 * 获取用户列表数据
 */
function listData(pageDataObj) {
	var tbody = $("#tbody");
	var webAppRoot = "/" + window.location.pathname.substr(1).split('/')[0];
	currPage= pageDataObj.currPage;//当前页号
	$("#currPage").val(currPage);
	order_id = $("#order_id").val();
	order_type = $("#order_type").val();
	$("#order_id").val(order_id);
	$("#order_type").val(order_type);
	var pageSize= pageDataObj.pageSize;//当前页面显示条数
	var totalNum= pageDataObj.totalNum;//数据总数
	var dataList= pageDataObj.list;//当前页面元素集
	tbody.empty();
	if (dataList != null && dataList.length > 0) {
	} else {
		return;
	}
	for (var i = 0; i < dataList.length; i++) {
		var user_id = dataList[i].user_id;
		var user_nm = dataList[i].user_nm;
		var group_name = dataList[i].group_name;
		if(group_name == null){
			group_name = "";
		}
		var user_real_nm = dataList[i].user_real_nm;
		var create_name = dataList[i].create_name;
		if(create_name == null){
			create_name = "";
		}
		var str_create_time = dataList[i].str_create_time;
		if(str_create_time == null){
			str_create_time = "";
		}
		var update_name = dataList[i].update_name;
		if(update_name == null){
			update_name = "";
		}
		var str_update_time = dataList[i].str_update_time;
		if(str_update_time == null){
			str_update_time = "";
		}
		var active_ind = dataList[i].active_ind;
		var trObj = $("<tr></tr>");
		if (i % 2 != 0) {
			trObj.attr("class", "active");
		}
		trObj.append("<td class='tbody-td td_user_id' style='padding-top:14px;' >" + user_id + "</td>");
		trObj.append("<td class='tbody-td td_user_name' style='padding-top:14px;' title='" + user_nm + "'>" + user_nm + "</td>");
		trObj.append("<td class='tbody-td td_user_in_group' style='padding-top:14px;' title='" + group_name + "'>" + group_name + "</td>");
		trObj.append("<td class='tbody-td td_user_realname' style='padding-top:14px;' >" + user_real_nm + "</td>");
		trObj.append("<td class='tbody-td td_user_creator' style='padding-top:14px;' >" + create_name + "</td>");
		trObj.append("<td class='tbody-td td_user_date_and_time' style='padding-top:14px;'>" + str_create_time + "</td>");
		trObj.append("<td class='tbody-td td_user_modifier' style='padding-top:14px;' >" + update_name + "</td>");
		trObj.append("<td class='tbody-td td_user_date_and_time' style='padding-top:14px;'>" + str_update_time + "</td>");
		if (active_ind == 1) {
			trObj.append("<td class='tbody-td td_user_operation' style='padding-top:14px;'><a href = '"+webAppRoot+"/sys/user/userAddEdit.do?action=edit&currPage="+currPage+"&edit_user_id="+user_id+"&order_id="+order_id+"&order_type="+order_type+"'>编辑</a> | <a onclick='submitActiveInd("+user_id+",2)'>禁用</a> | <a onclick='submitDel("+user_id+")'>删除</a></td>");
		} else {
			trObj.append("<td class='tbody-td td_user_operation' style='padding-top:14px;'><a href = '"+webAppRoot+"/sys/user/userAddEdit.do?action=edit&currPage="+currPage+"&edit_user_id="+user_id+"&order_id="+order_id+"&order_type="+order_type+"'>编辑</a> | <a onclick='submitActiveInd("+user_id+",1)'>激活</a> | <a onclick='submitDel("+user_id+")'>删除</a></td>");
		}
		/*if (status == 1) {
			trObj.append("<a href = 'www.baidu.com'>禁用</a>");
		} else {
			trObj.append("<a href = 'www.baidu.com'>激活</a>");
		}
		trObj.append("|<a href = 'www.baidu.com'>删除</a></td>");*/
		tbody.append(trObj);
	}

}

function submitDel(delete_user_id){
	createModalAlter('<div style="width:90%;height:90%;margin-left:5%;margin-right:5%;margin-top:9%;line-height:37px;text-align:center;">删除后，数据将不可恢复。<br/>请尽量选择“禁用”功能。<br/>确定要删除此用户吗？</div>','取消','确认',afterDelUser,delete_user_id);
}

function afterDelUser(delete_user_id){
	var params = new Object(); 
	params = {hide_back_btn : true};
	var tbody_tr_length = $("#user_main_table tbody tr").length;
	if(tbody_tr_length == 1 && currPage > 1){
		currPage = currPage - 1;
	}
	jQuery.ajax({
		type : 'POST',
		contentType : 'application/json',
		url : postUrl + '/sys/user/deleteUserById.do?currPage='+currPage+'&delete_user_id='+delete_user_id,
		data : '',
		//dataType : 'json',
		success : function(data) {
			//alert("删除成功");
			createModalAlter('删除成功','取消','确认',afterCancelBtn,params);
			showPageForSplit('/sys/user/findUserListByPageNoAdmin.do',listData,currPage);
		},
		error : function(data) {
			//alert("error");
			createModalAlter('error','取消','确认',afterCancelBtn,params);
		}
	});
}

function submitActiveInd(user_id, active_ind){
	var param = new Object(); 
	param = {user_id : user_id,active_ind : active_ind};
	if(active_ind == 1) {
		createModalAlter('是否激活此用户？','取消','确认',afterActivelUser,param);
	} else {
		createModalAlter('是否禁用此用户？','取消','确认',afterActivelUser,param);
	}
}

function afterActivelUser(param){
	var promptParams = new Object(); 
	promptParams = {hide_back_btn : true};
	jQuery.ajax({
		type : 'POST',
		contentType : 'application/json',
		url : postUrl + '/sys/user/modifyUserActiveInd.do?active_ind='+param.active_ind+'&currPage='+currPage+'&user_id='+param.user_id,
		data : '',
		//dataType : 'json',
		success : function(data) {
			if(param.active_ind == 1){
				//alert("激活成功");
				createModalAlter('激活成功','取消','确认',afterCancelBtn,promptParams);
				showPageForSplit('/sys/user/findUserListByPageNoAdmin.do',listData,currPage);
			} else {
				//alert("禁用成功");
				createModalAlter('禁用成功','取消','确认',afterCancelBtn,promptParams);
			}
			showPageForSplit('/sys/user/findUserListByPageNoAdmin.do',listData,currPage);
		},
		error : function(data) {
			//alert("error");
			createModalAlter('error','取消','确认',afterCancelBtn,promptParams);
		}
	});
}

function afterCancelBtn(){
}


function orderList(orderColumn){

	update_Table_With_orderByColumn_Icon(orderColumn);

	showPageForSplit('/sys/user/findUserListByPageNoAdmin.do', listData, currPage);
}


