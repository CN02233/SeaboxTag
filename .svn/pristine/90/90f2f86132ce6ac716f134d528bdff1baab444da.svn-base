var currPage = 1;
var order_id="group_id";
var order_type="asc";

$(document).ready(function() {
	var currPage = $("#currPage").val();
	showPageForSplit('/sys/group/findGroupListByPage.do', listData, currPage);
});

function clickAddBtn() {
	var currPage = $("#currPage").val();
	var order_id = $("#order_id").val();
	var order_type = $("#order_type").val();
	window.location = postUrl
			+ '/sys/group/groupAddEdit.do?action=add&currPage=' + currPage
			+ "&order_id=" + order_id + "&order_type=" + order_type;
	;
}

/**
 * 获取用户组列表数据
 */
function listData(pageDataObj) {
	var tbody = $("#tbody");
	var webAppRoot = "/" + window.location.pathname.substr(1).split('/')[0];
	currPage = pageDataObj.currPage;//当前页号
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
		var group_id = dataList[i].group_id;
		var group_name = dataList[i].group_name;
		var groupTwoUser = dataList[i].twoUserNames;
		var groupAllUser = dataList[i].allUserNames;
		var camp_admin_name = dataList[i].camp_admin_name;
		var create_name = dataList[i].create_name;
		var str_create_time = dataList[i].str_create_time;
		var update_name = dataList[i].update_name;
		var str_update_time = dataList[i].str_update_time;
		var active_ind = dataList[i].active_ind;
		var trObj = $("<tr></tr>");
		if (i % 2 != 0) {
			trObj.attr("class", "active");
		}
		trObj.append("<td class='tbody-td td_group_id' style='padding-top:14px;'>" + group_id + "</td>");
		trObj.append("<td class='tbody-td td_group_name' style='padding-top:14px;' title='" + group_name + "'>" + group_name + "</td>");
		trObj.append("<td class='tbody-td td_group_have_users' style='padding-top:14px;' title='" + groupAllUser + "'>" + groupTwoUser + "</td>");
		trObj.append("<td class='tbody-td td_group_admin' style='padding-top:14px;'>" + camp_admin_name + "</td>");
		trObj.append("<td class='tbody-td td_group_creator' style='padding-top:14px;'>" + create_name + "</td>");
		trObj.append("<td class='tbody-td td_group_date_and_time'style='padding-top:14px;'>" + str_create_time + "</td>");
		trObj.append("<td class='tbody-td td_group_modifier' style='padding-top:14px;'>" + update_name + "</td>");
		trObj.append("<td class='tbody-td td_group_date_and_time' style='padding-top:14px;'>" + str_update_time + "</td>");
		if (active_ind == 1) {
			trObj.append("<td class='tbody-td td_group_operation' style='padding-top:14px;'><a href = '"+webAppRoot+"/sys/group/groupAddEdit.do?action=edit&currPage="+currPage+"&edit_group_id="+group_id+"&order_id="+order_id+"&order_type="+order_type+"'>编辑</a> | <a onclick='submitActiveInd("+group_id+",2)'>禁用</a> | <a onclick='submitDel("+group_id+")'>删除</a></td>");
		} else {
			trObj.append("<td class='tbody-td td_group_operation' style='padding-top:14px;'><a href = '"+webAppRoot+"/sys/group/groupAddEdit.do?action=edit&currPage="+currPage+"&edit_group_id="+group_id+"&order_id="+order_id+"&order_type="+order_type+"'>编辑</a> | <a onclick='submitActiveInd("+group_id+",1)'>激活</a> | <a onclick='submitDel("+group_id+")'>删除</a></td>");
		}
		tbody.append(trObj);
	}

}

function submitDel(delete_group_id){
	var params = new Object(); 
	params = {hide_back_btn : true};
	jQuery.ajax({
		type : 'POST',
		contentType : 'application/json',
		url : postUrl + '/sys/user/findTotalNumByUserGroupId.do?user_group_id='+delete_group_id,
		data : '',
		//dataType : 'json',
		success : function(result) {
			if(result.totalNum > 0){
				createModalAlter('组成员非空，不能删除！','取消','确认',hideCancelBtn,params);
			} else {
				createModalAlter('<div class="modal_mul_content">删除后，数据将不可恢复。<br/>请尽量选择“禁用”功能。<br/>确定要删除此用户组吗？</div>','取消','确认',afterDelGroup,delete_group_id);
			}
		},
		error : function(data) {
			//alert("error");
			createModalAlter('error！','取消','确认',afterCancelBtn,params);
		}
	});
}

function hideCancelBtn() {
}

function afterDelGroup(delete_group_id){
	var params = new Object(); 
	params = {hide_back_btn : true};
	var tbody_tr_length = $("#group_main_table tbody tr").length;
	if(tbody_tr_length == 1 && currPage > 1){
		currPage = currPage - 1;
	}
	jQuery.ajax({
		type : 'POST',
		contentType : 'application/json',
		url : postUrl + '/sys/group/deleteGroupById.do?currPage='+currPage+'&delete_group_id='+delete_group_id,
		data : '',
		success : function(data) {
			//alert("删除成功");
			createModalAlter('删除成功','取消','确认',afterCancelBtn,params);
			showPageForSplit('/sys/group/findGroupListByPage.do',listData,currPage);
		},
		error : function(data) {
			//alert("error");
			createModalAlter('error','取消','确认',afterCancelBtn,params);
		}
	});
}

function submitActiveInd(group_id, active_ind){
	var params = new Object(); 
	params = {group_id : group_id, active_ind : active_ind};
	if(active_ind == 1) {
		createModalAlter('是否激活此用户组？','取消','确认',afterActivelGroup,params);
	} else {
		createModalAlter('是否禁用此用户组？','取消','确认',afterActivelGroup,params);
	}
}

function afterActivelGroup(params){
	var promptParams = new Object(); 
	promptParams = {hide_back_btn : true};
	jQuery.ajax({
		type : 'POST',
		contentType : 'application/json',
		url : postUrl + '/sys/group/modifyGroupActiveInd.do?active_ind='+params.active_ind+'&currPage='+currPage+'&group_id='+params.group_id,
		data : '',
		//dataType : 'json',
		success : function(data) {
			if(params.active_ind == 1){
				//alert("激活成功");
				createModalAlter('激活成功','取消','确认',afterCancelBtn,promptParams);
			} else {
				//alert("禁用成功");
				createModalAlter('禁用成功','取消','确认',afterCancelBtn,promptParams);
			}
			showPageForSplit('/sys/group/findGroupListByPage.do',listData,currPage);
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

	showPageForSplit('/sys/group/findGroupListByPage.do', listData, currPage);
}