$(document).ready(function(){
	
	showPageForSplit('/camp/campRec/listData.do',listData);
	
});

function listData(jsonObjPage){
//	alert(JSON.stringify(dataJson));
	
	var jsonObj = jsonObjPage.list;
	var currPageNum = jsonObjPage.currPage;
	$("#searchDiv").append("<input type='hidden' id='currPageNum' value="+currPageNum+">");
	var tbody = $("#tbody");
//	var tbody = $("<tbody></tbody>");
	tbody.empty();
	if(jsonObj!=null&&jsonObj.length>0){
//		tableObj.append(tbody);
	}else{
		return;
	}
	
	for(var i=0;i<jsonObj.length;i++){
		var campId = jsonObj[i].campId;
	    var campDesc = jsonObj[i].campDesc;
	    var ppDesc = jsonObj[i].ppDesc;
	    if(ppDesc==null)
	    	ppDesc = '';
	    var campNm = jsonObj[i].campNm;
	    var startDate = jsonObj[i].startDate;
	    var endDate = jsonObj[i].endDate;
//	    var campId = jsonObj[i].status;
	    var campType = jsonObj[i].campType;
	    var campTypeNm = jsonObj[i].campTypeNm;
	    var hitCount = jsonObj[i].hitCount;
	    var status = jsonObj[i].status;
	    var statusNm = jsonObj[i].statusNm;
	    if(status=='02'||status=='03'||status=='04'){
	    	statusNm = "<span style='color:#FF9900' >"+statusNm+"</span>";
	    }
	    if(status=='01'){
	    	statusNm = "<span style='color:#666666' >"+statusNm+"</span>";
	    }
	    if(status=='05'){
	    	statusNm = "<span style='color:#00cc00' >"+statusNm+"</span>";
	    }
	   
	    var createUser = jsonObj[i].createUser;
	    var createUserNm = jsonObj[i].createUserNm;
		var trObj = $("<tr></tr>");
		trObj.attr("id",campId);
		if(i%2!=0){
			trObj.attr("class","active");
		}
//		trObj.append("<td style='width:4%'>"+
//				"<div class='squaredFour' style='width:100%;'>" +
//					"<input type='checkbox' value='None' id='"+campId+"_c' name='check' style='margin-left: 10px;margin-right: auto;'/>" +
//					"<label for='"+campId+"_c'></label>" +
//				"</div></td>");
//		trObj.append("<td class='tbody-td' style='width:6%' title='"+campId+"' onclick='showCampDetail("+campId+")'>"+campId+"</td>");
////		trObj.append("<td><p class='tbody-td' data-toggle='tooltip' data-animation='background:red' data-placement='bottom' title='"+campDesc+"'>"+campDesc+"</p></td>");
//		trObj.append("<td class='tbody-td' title='"+campNm+"'>"+campNm+"</td>");
//		trObj.append("<td class='tbody-td' >"+startDate+"</td>");
//		trObj.append("<td class='tbody-td'>"+endDate+"</td>");
//		trObj.append("<td class='tbody-td'>"+"</td>");
//		trObj.append("<td class='tbody-td'>"+campType+"</td>");
//		trObj.append("<td class='tbody-td'>"+hitCount+"</td>");
//		trObj.append("<td class='tbody-td'>"+status+"</td>");
//		trObj.append("<td class='tbody-td'>"+createUser+"</td>");
//		
////		trObj.append("<td class='tbody-td' style='padding-top:8px;padding-bottom:0px;'>" +//style中设置放到CSS中无效,待查原因
////				"<label class='sprite-img-first' style='background-position:-107px -2px;' id='"+campId+"' onclick='showOperation(this,event)'></label></td>");
//		tbody.append(trObj);
		
		trObj.append("<td class='td_camp_operation'>"+
				"<div class='iconCheckBox' style='width:100%;'>" +
					"<input type='checkbox' isCheck='false' value='None' id='"+campId+"_c' name='check' style='display:none; margin-left: 10px;margin-right: auto;margin-top:10px;'/>" +
					"<label class='sprite-img-second' style='background-position:-64px -26px; margin-top:10px;' for='"+campId+"_c'></label>" +
				"</div></td>");
		trObj.append("<td class='tbody-td td_camp_id' style='height-line:40px;padding-top:14px;' title='"+campId+"' >"+campId+"</td>");
//		trObj.append("<td><p class='tbody-td' data-toggle='tooltip' data-animation='background:red' data-placement='bottom' title='"+campDesc+"'>"+campDesc+"</p></td>");
		trObj.append("<td class='tbody-td td_camp_name' style='padding-top:14px;' title='"+campNm+"' campId='"+campId+"'>"+campNm+"</td>");
		trObj.append("<td class='tbody-td td_camp_date_and_time' style='padding-top:14px;'>"+startDate+"</td>");
		trObj.append("<td class='tbody-td td_camp_date_and_time' style='padding-top:14px;'>"+endDate+"</td>");
		trObj.append("<td class='tbody-td td_camp_condition_desc' style='padding-top:14px;'  title='"+ppDesc+"'>"+ppDesc+"</td>");
		trObj.append("<td class='tbody-td td_camp_action_way' style='padding-top:14px;'>"+campTypeNm+"</td>");
		trObj.append("<td class='tbody-td td_camp_user_count' style='padding-top:14px;'>"+hitCount+"</td>");
		trObj.append("<td class='tbody-td td_camp_status' style='padding-top:14px;'>"+statusNm+"</td>");
		trObj.append("<td class='tbody-td td_camp_creator' style='padding-top:14px;' title='"+createUserNm+"'>"+createUserNm+"</td>");
//		trObj.append("<td class='tbody-td' style='padding-top:8px;padding-bottom:0px;'>" +//style中设置放到CSS中无效,待查原因
//				"<label class='sprite-img-first' style='background-position:-107px -2px;' id='"+campId+"' status='"+status+"' user='"+createUser+"' onclick='showOperation(this,event)'></label></td>");
//		alert(trObj.html());
		tbody.append(trObj);
//		$("[data-toggle='tooltip']").tooltip();
	 }

	initCheckBox();

}



function orderList(orderColumn){

	update_Table_With_orderByColumn_Icon(orderColumn);
	if($(orderColumn).parent().find("#startDateLabel").length>0||$(orderColumn).parent().find("#endDateLabel").length>0){
		$(orderColumn).parent().find("#orderLabel").css("margin-top","10px");
		$(orderColumn).parent().find("#orderLabel").css("margin-left","5px");
		$(orderColumn).parent().find("#orderLabel").css("float","left");
	}
	showPageForSplit('/camp/campRec/listData.do',listData);

}


function rollbackCamp(){
	var param = new Object();
	
	var allCheckBox = $("input:checkbox:checked");//获取所有已选checkbox
	
	if(allCheckBox==null||allCheckBox.length==0){
		var param = new Object();
		param['hide_back_btn'] = true;
		createModalAlter("请选择要恢复的活动",'取消','确定',null,param);
		//alert("请选择要恢复的活动！");
		return false;
	}
	
	createModalAlter("确认恢复已选择的所有活动？",'取消','确定',doRollBack,allCheckBox,null);
	
//	alert(JSON.stringify(rollbackList));
	
}

function doRollBack(allCheckBox){
	
	var rollbackList = new Array();
	$.each(allCheckBox,function(i,checkBoxObj){
		var checkId = checkBoxObj.id;
		var rCheckId = checkId.split('_');
		rollbackList.push(rCheckId[0]);
//		alert(rCheckId[0]);
	});
	initDataFunctionByJson("/camp/campRec/rollback.do",rollbackList,rollbackResult);
}

function rollbackResult(result){
	var param = new Object();
	param['hide_back_btn'] = true;
	createModalAlter("恢复成功",'取消','确定',null,param);
	showPageForSplit('/camp/campRec/listData.do',listData);
}

function deleteCamp(){
	var allCheckBox = $("input:checkbox:checked");//获取所有已选checkbox
	if(allCheckBox==null||allCheckBox.length==0){
		var param = new Object();
		param['hide_back_btn'] = true;
		createModalAlter("请选择要删除的活动",'取消','确定',null,param);
//		alert("请选择要删除的活动！");
		return false;
	}
	
	createModalAlter("确认彻底删除已选择的所有活动？",'取消','确定',doDeleteCamp,allCheckBox,null);
}

function doDeleteCamp(allCheckBox){
	var deleteCampList = new Array();
	$.each(allCheckBox,function(i,checkBoxObj){
		var checkId = checkBoxObj.id;
		var rCheckId = checkId.split('_');
		deleteCampList.push(rCheckId[0]);
	});
	initDataFunctionByJson("/camp/campRec/deleteCamp.do",deleteCampList,deleteCampResult);
}

function deleteCampResult(){
	var param = new Object();
	param['hide_back_btn'] = true;
	createModalAlter("彻底删除成功",'取消','确定',null,param);
//	alert("彻底删除成功！");
	showPageForSplit('/camp/campRec/listData.do',listData);
}