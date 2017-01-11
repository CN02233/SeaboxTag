var isOpen = false;//后方操作按钮栏是否已打开,true打开
var operClick = false;

var isOpenCampId = '';
/**
 * 鼠标滑过table中tr时方法
 * @param evtId 活动ID
 */
$(document).ready(function(){
	
	refreshPage();
	
	$("#seachLabel").click(function(){
		refreshPage();
	});
	
	
	var date = new Date();
	var daysInMonth = new Array([0],[31],[28],[31],[30],[31],[30],[31],[31],[30],[31],[30],[31]);   
	var strYear = date.getFullYear();     
	var strDay = date.getDate();     
	var strMonth = date.getMonth()+1;  
	var nowStrMonth = date.getMonth()+1;  
	if(strYear%4 == 0 && strYear%100 != 0){   
		daysInMonth[2] = 29;   
	}   
	if(strMonth - 1 == 0)   
	{   
		strYear -= 1;   
		strMonth = 12;   
	}   
	else  
	{   
		strMonth -= 1;   
	}   
	strDay = daysInMonth[strMonth] >= strDay ? strDay : daysInMonth[strMonth];   
	if(strMonth<10)     
	{     
		strMonth="0"+strMonth;     
	} 
	if(strMonth<10)     
	{     
		strMonth="0"+strMonth;     
	}  
	if(nowStrMonth<10)     
	{     
		nowStrMonth="0"+nowStrMonth;     
	}   
	datastr = strYear+"-"+strMonth+"-"+strDay+" 00:00"; 
	var nowDateStr = strYear+"-"+nowStrMonth+"-"+strDay; 
	$("#startDateLabel").attr("data-date",datastr);
	$("#startDateLabel").datetimepicker({
		format: "yyyy-MM-dd hh:ii",
        autoclose: true,//参数在设置changeDate事件后不好使,需要手动关闭$('#startDateLabel').datetimepicker('hide');
        language : 'zh-CN',
        minView : 0,
        todayBtn: true,
        todayHighlight : 1
	}).on('changeDate', function(ev){
		$('#startDateLabel').datetimepicker('hide');
		var selectMonth = ev.date.getMonth()+1;
		if(selectMonth<10)
			selectMonth = "0"+selectMonth;
		var selectDay = ev.date.getDate();
		if(selectDay<10)
			selectDay = "0"+selectDay;
		
		var selectHour = ev.date.getUTCHours();
		var selectMill = ev.date.getMinutes();
		if(selectHour<10)
			selectHour = "0"+selectHour;
		if(selectMill<10)
			selectMill = "0"+selectMill;
		
		var selectDate = ev.date.getFullYear()+"-"+selectMonth+"-"+selectDay+" "+selectHour+":"+selectMill;
		$("#start_date").val(selectDate);
		
		var nowEndDate = $("#end_date").val();
		var endDate= new Date(nowEndDate);
		var startDate = new Date(selectDay);
		if(nowEndDate!=null&&endDate<startDate){
			var param = new Object();
			param['hide_back_btn'] = true;
			createModalAlter("开始日期大于结束日期,当前结束日期为:"+nowEndDate,'取消','确定',null,param);
			return;
		}
		refreshPage();
	});
	
	$("#endDateLabel").attr("data-date",nowDateStr);
	$("#endDateLabel").datetimepicker({
        format: "yyyy-MM-dd hh:ii",
        autoclose: true,
        language : 'zh-CN',
        minView : 2,
        todayBtn: true,
        todayHighlight : 1
    }).on('changeDate', function(ev){
    	
		$('#endDateLabel').datetimepicker('hide');
		var selectMonth = ev.date.getMonth()+1;
		if(selectMonth<10)
			selectMonth = "0"+selectMonth;
		var selectDay = ev.date.getDate();
		if(selectDay<10)
			selectDay = "0"+selectDay;
		
		var selectHour = ev.date.getUTCHours();
		var selectMill = ev.date.getMinutes();
		if(selectHour<10)
			selectHour = "0"+selectHour;
		if(selectMill<10)
			selectMill = "0"+selectMill;
		
		var selectDate = ev.date.getFullYear()+"-"+selectMonth+"-"+selectDay+" "+selectHour+":"+selectMill;
		
		$("#end_date").val(selectDate);
		var nowStartDate = $("#start_date").val();
		var startDate = new Date(nowStartDate);
		var endDate = new Date(selectDay);
		if(nowStartDate!=null&&endDate<startDate){
			var param = new Object();
			param['hide_back_btn'] = true;
			createModalAlter("开始日期大于结束日期,当前结束日期为:"+nowEndDate,'取消','确定',null,param);
			return;
		}
		
		refreshPage();
	});
	
	$("body").click(function(e){
//		alert("body click "+operClick+" "+isOpen);
		if(!operClick&&isOpen){
			$('#operationDiv').remove();
			isOpen = false;
			isOpenCampId = '';
//			operClick = false;
		}
		operClick=false;
	});
});

/**
 * 获取活动总览列表数据
 */
function listData(jsonObjPage){
	var jsonObj = jsonObjPage.list;
	var currPageNum = jsonObjPage.currPage;
	var isMa = jsonObjPage.isMa;//是否管理员
	var userId = jsonObjPage.userId;
	$("#isMa").val(isMa);
	$("#userId").val(userId);
	$("#currPageNum").remove();
	$("#searchDiv").append("<input type='hidden' id='currPageNum' value="+currPageNum+">");
	var tbody = $("#tbody");
//	var tbody = $("<tbody></tbody>");
	tbody.empty();
	if(jsonObj!=null&&jsonObj.length>0){
//		tableObj.append(tbody);
	}else{
		return;
	}
//	alert(JSON.stringify(jsonObj));
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
		trObj.append("<td class='tbody-td td_camp_id' style='cursor:pointer;height-line:40px; padding-top:14px;' title='"+campId+"' onclick='showCampDetail(this)'>"+campId+"</td>");
//		trObj.append("<td><p class='tbody-td' data-toggle='tooltip' data-animation='background:red' data-placement='bottom' title='"+campDesc+"'>"+campDesc+"</p></td>");
		trObj.append("<td class='tbody-td td_camp_name' style='cursor:pointer;padding-top:14px;' title='"+campNm+"' campId='"+campId+"' onclick='showCampDetail(this)'>"+campNm+"</td>");
		trObj.append("<td class='tbody-td td_camp_date_and_time' style='padding-top:14px;' title='"+startDate+"'>"+startDate+"</td>");
		trObj.append("<td class='tbody-td td_camp_date_and_time' style='padding-top:14px;' title='"+endDate+"'>"+endDate+"</td>");
		trObj.append("<td class='tbody-td td_camp_condition_desc' style='padding-top:14px;' title='"+ppDesc+"'>"+ppDesc+"</td>");
		trObj.append("<td class='tbody-td td_camp_action_way' style='padding-top:14px;'>"+campTypeNm+"</td>");
		trObj.append("<td class='tbody-td td_camp_user_count' style='padding-top:14px;'>"+hitCount+"</td>");
		trObj.append("<td class='tbody-td td_camp_status' style='padding-top:14px;'>"+statusNm+"</td>");
		trObj.append("<td class='tbody-td td_camp_creator' style='padding-top:14px;' title='"+createUserNm+"'>"+createUserNm+"</td>");
		trObj.append("<td class='tbody-td td_camp_operation' style='padding-bottom:0px;padding-top:14px;'>" +//style中设置放到CSS中无效,待查原因
				"<label class='sprite-img-first' style='background-position:-107px -2px;' id='"+campId+"' status='"+status+"' user='"+createUser+"' onclick='showOperation(this,event)'></label></td>");
//		alert(trObj.html());
		tbody.append(trObj);
//		$("[data-toggle='tooltip']").tooltip();
	 }
	
	
	
}

function orderList(orderColumn){

	update_Table_With_orderByColumn_Icon(orderColumn);
	if($(orderColumn).parent().find("#startDateLabel").length>0||$(orderColumn).parent().find("#endDateLabel").length>0){
		$(orderColumn).parent().find("#orderLabel").css("margin-top","10px");
		$(orderColumn).parent().find("#orderLabel").css("margin-left","5px");
		$(orderColumn).parent().find("#orderLabel").css("float","left");
	}
	
	showPageForSplit('/camp/campView/listMainData.do',listData);
}

function showOperation(opObj,event){
//	alert("showOperation before "+operClick+"  "+isOpen);
	
	var camp_Id = opObj.id;
	var status = $(opObj).attr("status");
	var creatUser = $(opObj).attr("user");//创建活动用户
	
	$('#operationDiv').remove();
	if(isOpenCampId!=''&&isOpenCampId==camp_Id){//二次点击,关闭
		isOpenCampId = '';
		isOpen = false;
		operClick = false;
		return ;
	}else{
		isOpen = true;
		operClick = true;
	}
	
//	if(isOpen){
//		isOpen = false;
//		operClick = false;
//	}else{
//		isOpen = true;
//		operClick = true;
//	}
	
//	alert("showOperation after "+operClick+"  "+isOpen);
	
	isOpenCampId = camp_Id;
	
	
//	if(isOpen){
//		$('#operationDiv').remove();
//		isOpen = false;
//	}else{
		var showColumNum = 1;
//		var parentObj = $(opObj).parent();
		var parentObj = $("body");
		var operationDiv = $("<div id='operationDiv' class='oprtDiv'></div>");
		operationDiv.css("margin-left",'89%');
		operationDiv.css("margin-top",event.pageY+15);
		operationDiv.append("<div id='"+camp_Id+"' operType='copy' onclick='doOperation(this)'>复制</p></div>");
		
		var userId = $("#userId").val();
		if(userId==creatUser){//非创建用户除了复制和审批,无其他权限
			if(status=='01'||status=='10'){//只有未提交&已驳回的活动可编辑
				var userId = $("#userId").val();
				if(userId==creatUser){//非创建用户不允许修改
					operationDiv.append("<div id='"+camp_Id+"' operType='edit' onclick='doOperation(this)'>编辑</div>");
					showColumNum++;
				}
			}
			if(status=='02'||status=='03'){//只有审批中(提交审批)&待发布(已审批) 显示终止
				operationDiv.append("<div id='"+camp_Id+"' operType='stop' onclick='doOperation(this)'>终止</div>");
				showColumNum++;
			}
			
			if(status=='01'||status=='10'){//只有未提交&驳回可删除
				operationDiv.append("<div id='"+camp_Id+"' operType='delete' onclick='doOperation(this)'>删除</div>");
				showColumNum++;
			}
		}
		
		operationDiv.css("height",showColumNum*40+'px');
		parentObj.append(operationDiv);
//		isOpen = true;
//	}
	
}

/**
 * 点击页面其他地方关闭操作栏
 */


//$(document).bind('click',function(e){ 
//	var clickClass = $(e.target).attr('class');
//	if(clickClass=='tdOperClass'){
//		return ;
//	}else{
//		$('#operationDiv').remove();
//		isOpen = false;
//	}
//}); 

function showCampDetail(thisObj){
	var campId = $(thisObj).attr("campId");
	if(campId!=null){
		
	}else{
		campId = $(thisObj).text();
	}
	
	var param = new Array();
	param.push({id:"camp_id",value:campId});
	param.push({id:"currPageNum",value:$("#currPageNum").val()});
	param.push({id:"isMa",value:$("#isMa").val()});
	newPageByPost(param,'/camp/campView/detailPage.do');
}

function doOperation(campObj){
	var operationName = $(campObj).attr("operType");
	if(operationName=='copy'){
		copyEvt(campObj);
	}
	if(operationName=='edit'){
		editEvt(campObj);
	}
	if(operationName=='stop'){
		stopEvt(campObj);
	}
	if(operationName=='delete'){
		delEvt(campObj);
	}
	isOpenCampId = '';
	$("#operationDiv").remove();
}

function copyEvt(evtObj){
	//执行复制操作
	var campId = evtObj.id;
	jQuery.ajax({
		type : 'POST',
		contentType : 'application/json',
		url : postUrl + '/camp/campView/copyEvtData.do?camp_id='+campId,
		data : '',
		//dataType : 'json',
		success : function(data) {
			var param = new Object();
			param['hide_back_btn'] = true;
			createModalAlter("复制成功",'取消','确定',null,param);
			refreshPage();
		},
		error : function(data) {
			alert("error");
		}
	});
	//复制完成后刷新列表
}

function editEvt(evtObj){
	var campId = evtObj.id;
	var param = new Array();
	param.push({id:"camp_id",value:campId});
	newPageByPost(param,'/camp/campView/evtEdit.do');
}

function stopEvt(evtObj){
	var campId = evtObj.id;
	jQuery.ajax({
		type : 'POST',
		contentType : 'application/json',
		url : postUrl + '/camp/campView/stopEvt.do?camp_id='+campId,
		data : '',
		//dataType : 'json',
		success : function(data) {
			var param = new Object();
			param['hide_back_btn'] = true;
			createModalAlter("终止成功",'取消','确定',null,param);
			refreshPage();
		},
		error : function(data) {
			alert("error");
		}
	});
	
//	var campId = evtObj.id;
//	var param = new Array();
//	param.push({id:"camp_id",value:campId});
//	newPageByPost(param,'/camp/campView/stopEvt.do');
}

function submitDel(thisObj){
	var oldCampId = thisObj.id;
	createModalAlter('确定要删除此次活动吗？','取消','确认',delEvt,oldCampId);
}

function delEvt(thisObj){
	var oldCampId = thisObj.id;
	jQuery.ajax({
		type : 'POST',
		contentType : 'application/json',
		url : postUrl + '/camp/campView/moveToDelTb.do?camp_id='+oldCampId,
		data : '',
		//dataType : 'json',
		success : function(data) {
			var param = new Object();
			param['hide_back_btn'] = true;
			createModalAlter("删除成功",'取消','确定',null,param);
			refreshPage();
		},
		error : function(data) {
			alert("error");
		}
	});
}

function refreshPage(){
	var allOrderLabelDown = $(".glyphicon-arrow-down");
	$.each(allOrderLabelDown,function(i,orderLabel){
		$(orderLabel).removeAttr("id");
		$(orderLabel).removeAttr("class");
	});
	var allOrderLabelUp = $(".glyphicon-arrow-up");
	$.each(allOrderLabelUp,function(i,orderLabel){
		$(orderLabel).removeAttr("id");
		$(orderLabel).removeAttr("class");
	});
	
	showPageForSplit('/camp/campView/listMainData.do',listData);
}