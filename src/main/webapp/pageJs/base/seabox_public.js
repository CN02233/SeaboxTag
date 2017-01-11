/**
 * create by SongChaoqun
 */

var curWwwPath = window.document.location.href;
var pathName = window.document.location.pathname;
var pos = curWwwPath.indexOf(pathName);
var localhostPaht = curWwwPath.substring(0, pos);
var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
var postUrl = localhostPaht + projectName;

/**
 * 点击查询按钮后调用该方法
 * 搜集DIV标签searchDiv中所有type为input的输入框内容
 * url:请求后台数据时的入口地址(不包含IP和项目名称),如想要请求http://localhost:8080/TEST/com/testcontroller/test.do,url应为:/com/testcontroller/test.do
 * callbackFunction:向后台请求得到数据后回调方法，需要自己写逻辑处理得到的JSON报文MAP
 */
var isInitPage = true;

function searchDataFromBack(url,callbackFunction){
	
	var paramArray =  initSearchParam();
	if(paramArray==null)
		return;
	initDataFunction(url,paramArray,callbackFunction);
	
	//alert(allInputObjs);
}

/**
 * 获取带有分页信息的数据
 * @param url 请求数据URL
 * @param callbackFunction 请求完成数据后的回调函数
 * @param currPage 当前页码
 * @param pageSize 页面数据条数
 */
function showPageForSplit(url,callbackFunction,currPage,pageSize){
	var paramArray =  initSearchParam();
	if(paramArray==null)
		return;
	if(currPage==null)
		currPage = 1;
	if(pageSize==null)
		pageSize = 9;
	paramArray.push({id:"currPage",value:currPage});
	paramArray.push({id:"pageSize",value:pageSize});
	initPageData(url,paramArray,callbackFunction);
}

function initSearchParam(){
	var allInputObjs = $("#searchDiv input");
	var paramArray = new Array();
	
	var notNullErrorMsg = '';
	
	for(var i=0;i<allInputObjs.length;i++){
		var inputObj = allInputObjs[i];
		var inputClass = inputObj.className;
		if(inputClass.indexOf('notNull')>=0){
			if(inputObj.value==''||inputObj.value==null){
				if(notNullErrorMsg.length>0){
					notNullErrorMsg = notNullErrorMsg + "\n【" + inputObj.placeholder+"】";
				}else
					notNullErrorMsg = notNullErrorMsg+"【"+inputObj.placeholder+"】";
			}
				
		}
		if(inputObj.type=='text'||inputObj.type=='hidden'||inputObj.type=='selected'){
			paramArray.push(inputObj);
			//alert(inputObj.id+"|"+inputObj.value);
		}
		
	}
	
	var allSelectObjs = $("#searchDiv select");
	var selectObjs = new Array();
	for(var i=0;i<allSelectObjs.length;i++){
		var selectObj = allSelectObjs[i];
		var selectObjId = selectObj.id;
		var inputClass = selectObj.className;
		var hasSelect = false;
		if(inputClass.indexOf('notNull')<0){
			hasSelect = true;
		}
		var selectOptions = selectObj.options;
//		alert(selectOptions.length);
		var optionValues = '';
		
		for(var s=0;s<selectOptions.length;s++){
			var optionObj = selectOptions[s];
			if(optionObj.selected){
				if(optionValues!=''){
					optionValues = optionValues+','+optionObj.value;
				}
				else{
					optionValues = optionObj.value;
					hasSelect = true;
				}
			}
		}
		if(!hasSelect){
			if(notNullErrorMsg.length>0){
				notNullErrorMsg = notNullErrorMsg + "\n【" + selectObj.name+"】";
			}else
				notNullErrorMsg = notNullErrorMsg+"【"+selectObj.name+"】";
		}
		
		
		paramArray.push({id:selectObjId,
			value:optionValues});
		
	}	
	
	if(notNullErrorMsg.length>0){
		alert("请输入或选择查询条件:\n"+notNullErrorMsg+"");
		return ;
	}
//	alert(JSON.stringify(paramArray));
	return paramArray;
}

/**
 * searchDataFromBack方法搜集到查询框中数据内容后调用该方法，向后台请求AJAX请求
 * 注意:此方法调用JAVA时,java必须返回json报文格式的字符串或对象,如果返回的是普通的文本将无法解析。如果需要返回普通文本,使用initDataForTest方法
 * @param url
 * @param params
 * @param callbackFunction
 */
function initDataFunction(url,params,callbackFunction){
	if(params!=null){
		$.each(params,function(i,param){
			var paramId = param.id;
			var paramValue = param.value;
			if(i==0){
				url = url+"?"+paramId+"="+paramValue;
			}else{
				url = url+"&"+paramId+"="+paramValue;
			}
		});


	}
	
	jQuery.ajax({
		type : 'POST',
		contentType : 'application/json; charset=utf-8',
		url : postUrl + url,
		data : '',
		dataType : 'json',
		success : function(data) {
			// 生成报表
			if(callbackFunction!=null){
				callbackFunction(data);
			}
			//createDataZoom();
		},
		error : function(data) {
//			);
			alert("error");
		}
	});
}

/**
 * 与initDataFunction相同,差别仅是返回数据是否为JSON格式的差别
 * @param url
 * @param params
 * @param callbackFunction
 */
function initDataForText(url,params,callbackFunction){
	if(params!=null){
		for(var i=0;i<params.length;i++){
			var paramId = params[i].id;
			var paramValue = params[i].value;
			
			if(i==0){
				url = url+"?"+paramId+"="+paramValue;
			}else{
				url = url+"&"+paramId+"="+paramValue;
			}
		}
	}
	
	jQuery.ajax({
		type : 'POST',
		contentType: "application/x-www-form-urlencoded; charset=utf-8",
		url : postUrl + url,
		dataType:'text',
		success : function(data) {
			 
			// 生成报表
			if(callbackFunction!=null){
				callbackFunction(data);
			}
			//createDataZoom();
		},
		error : function(data) {
//			);
			alert("error");
		}
	});
}

/**
 * 初始化带有分页信息的页面
 */
function initPageData(url,params,callbackFunction){
	var oldUrl = url;
	for(var i=0;i<params.length;i++){
		var paramId = params[i].id;
		var paramValue = params[i].value;
		
		if(i==0){
			url = url+"?"+paramId+"="+paramValue;
		}else{
			url = url+"&"+paramId+"="+paramValue;
		}
	}
	jQuery.ajax({
		type : 'POST',
		contentType : 'application/json',
		url : postUrl + url,
		data : '',
		dataType : 'json',
		success : function(pageData) {
			// 生成报表
			if(callbackFunction!=null){
				var pageDataObj = pageData;
				var currPage= pageDataObj.currPage;//当前页号
				var pageSize= pageDataObj.pageSize;//当前页面显示条数
				var totalNum= pageDataObj.totalNum;//数据总数
				var dataList= pageDataObj.list;//当前页面元素集
				initPagination(oldUrl,pageDataObj,callbackFunction);
				callbackFunction(pageDataObj);
			}
			//createDataZoom();
		},
		error : function(data) {
			alert("error");
		}
	});
}

function initPagination(url,pageData,callbackFunction){
	var currPage= pageData.currPage;//当前页号
	var pageSize= pageData.pageSize;//当前页面显示条数
	var totalNum= pageData.totalNum;//数据总数
	var pageTotal = totalNum/pageSize;
	var pageTotalResult = Math.ceil(pageTotal);

	var paginationDivObj = $("#paginationDiv");
	if(paginationDivObj==null)
		return null;
	if(pageTotal<=1 && totalNum<=pageSize && currPage<=1) {
		paginationDivObj.css("display", "none");
	} else {
		paginationDivObj.css("display", "inline-block");
	}

	/*paginationDivObj.empty();
	var beforePageObj = $("<span class='pageIndex'>&lt;</span>");
	beforePageObj.click(function(){
		var activeValue = $("#paginationDiv span.active").text();
		if(activeValue==1){
			return;
		}
		if(activeValue == '') {
		    activeValue = 2; // next goes to page '1'
		}
		showPageForSplit(url,callbackFunction,activeValue-1,pageSize);
	});
	paginationDivObj.append(beforePageObj);
	var maxPageNum = 0;
	for(var i=0;i<pageTotalResult;i++){
		var spanNum = i+1;
		var idValue = 'span_'+spanNum;
		var spanObj = $("<span>"+spanNum+"</span>");
		if(currPage==(i+1)){
			spanObj.attr("class","active currentPage");
		}
		else{
			spanObj.attr("class","currentPage");
		}
		paginationDivObj.append(spanObj);
		spanObj.click(function(){
			showPageForSplit(url,callbackFunction,$(this).text(),pageSize);
		});
			
		maxPageNum++;
	}
	
	var afterPageObj = $("<span class='pageIndex'>&gt;</span>");
	afterPageObj.click(function(){
		var activeValue = $("#paginationDiv  span.active").text();
		if(activeValue==maxPageNum){
			return;
		}
		if(activeValue == '') {
		    activeValue = 0; // next goes to page '1'
		}
		showPageForSplit(url,callbackFunction,parseInt(activeValue)+1,pageSize);
	});
	paginationDivObj.append(afterPageObj);*/
	if(isInitPage){
		$('#paginationDiv').extendPagination({
			currPage: currPage,
	        totalCount: totalNum,
	        showCount: 10,
	        limit: pageSize,
	        callback: function (currPage, limit, totalCount) {
	        	showPageForSplit(url, callbackFunction, currPage, pageSize);
	        }
	    });
		isInitPage = false;
	}
	
}

/**
 * post方式提交表单，跳转页面 需要在源页面有<form></form>
 * @param param 请求后台controller时带过去的参数,类型为Array{id:paramName,value:paramValue} 如:param = new Array(); param[0]={id:'testParam',value:'123456'}
 * @param url 后台Controller地址。如想要请求http://localhost:8080/TEST/com/testcontroller/test.do,url应为:/com/testcontroller/test.do
 */
function newPageByPost(param,url){
	var formObj = $("form");
	if(param!=null){
		for(var i=0;i<param.length;i++){
			var paramId = param[i].id;
			var paramValue = param[i].value;
			var hiddenObj = $("<input type='hidden' name='"+paramId+"' id='"+paramId+"' value='"+paramValue+"'>");
			formObj.append(hiddenObj);
		}
	}

	// var params = $("form").serialize();
	// params = decodeURIComponent(params,true);
	// params = encodeURI(params);
	// $.post(postUrl + url,encodeURI(params),getApply,'json');

	formObj.attr("method","post");
	formObj.attr("action",postUrl + url);
	formObj.submit();

}
/**
 * 获取Min~Max区间的随机数
 * @param Min
 * @param Max
 * @returns
 */
function randomNum(Min,Max){
	var Range = Max - Min;
	var Rand = Math.random();   
	var num = Min + Math.round(Rand * Range);
	return num;
	}

/**
*以JSON作为请求参数请求后台数据
*/
function initDataFunctionByJson(url,params,callbackFunction){
	jQuery.ajax({
		type : 'POST',
		contentType : 'application/json',
		url : postUrl + url,
		data : JSON.stringify(params),
		dataType : 'json',
		success : function(data) {
			if(callbackFunction!=null)
				callbackFunction(data);
		},
		error : function(data) {
			alert("error");
		}
	});
}

/**
 * 当提交到controller的数据比较多,希望以json字符串而不是对象提交到后台的时候调用
 * @param url
 * @param callbackFunction
 * @param data
 */
function submitJsonStr2Controller(url,callbackFunction,data) {

	jQuery.ajax({
		type : 'POST',
		contentType : 'application/json',
		url : postUrl + url,
		data : JSON.stringify(data),
		dataType : 'json',
		success : function(data) {
			callbackFunction(data);
		}

	});
}

/**
 *
 * @param url 后台地址:/XX/SS/www.do
 * @param callbackFunction 后台处理成功后的回调函数
 * @param data 向后退发送的数据,js对象
 */
function submitData2Controller(url,callbackFunction,data){

	jQuery.ajax({
		type : 'POST',
		url : postUrl + url,
		data : data,
		// dataType: 'application/json',
		success : function(data) {
			callbackFunction(data);
		},
		error : function(){
			alert("you get a mesSage.qlease Call manager. ");
		}
	});
}


/**
 * 检查非空选项是否为空
 */
function checkNotNull(){
	var allNotNullObjs = $(".not-null");
	var nullObjNames = "";
	$.each(allNotNullObjs,function(i,notNullObj){
		if($(notNullObj).val()!=null&&$(notNullObj).val()!=''){//do nothing}
//			alert(notNullObj.name+"---"+$(notNullObj).val());
		}
		else{
			nullObjNames = nullObjNames+"<span style='color:red;font-size:17px;'>"+$(notNullObj).attr("name")+"</span><br/>";
		}
		
	});
	
	var param = new Object();
	param['hide_back_btn'] = true;
	if(nullObjNames!=""){
		nullObjNames = nullObjNames+"<br/>"+"不允许为空！";
		var msgText = "<div style='width:90%;height:90%;margin-left:5%;margin-right:5%;margin-top:5%;line-height:20px;text-align:center;'>"+nullObjNames+"</div>";
		createModalAlter(msgText,'取消','确定',null,param);
		return false;
	}
	return true;
}

/**
 * 除下划线外不允许有任何特殊字符
 * @param s
 * @returns
 */
function containSpecial(s) { 
	var containSpecial = RegExp(/[(\ )(\~)(\!)(\@)(\#) (\$)(\%)(\^)(\&)(\*)(\()(\))(\+)(\=) (\[)(\])(\{)(\})(\|)(\\)(\;)(\:)(\')(\")(\,)(\.)(\/) (\<)(\>)(\?)(\)]+/); 
	return ( containSpecial.test(s) ); 
}


/**
 * 对列表可排序字段点击时, 更新排序图标
 *  需要请求排序的页面存在有 id="searchDiv" 的 组件
 *    比如:  <div id="searchDiv" ... >
 *
 * @param orderColumn
 */
function update_Table_With_orderByColumn_Icon(orderColumn){

	var required_parent_div = $("#searchDiv");
	if(!required_parent_div.length) {
		console.log("error on  update_Table_With_orderByColumn_Icon(), No parent div with  class table-responsive defined!");
		return;
	}

	var addOrUpdate_orderLabel_icon = function (orderColumn, orderType) {
		var nextObj = $(orderColumn).next();

		if(nextObj.length==0) {
			var parentTd = $(orderColumn).parent();
			nextObj = $("<label></label>");
			parentTd.append(nextObj);
		}

		var icon_class = "sprite-img-six ";
		if(orderType == "down") {
            icon_class += "icon_06_04_down";
		} else {
		    icon_class += "icon_06_03_up";
		}

		$(nextObj).attr("id","orderLabel");
		$(nextObj).attr("class", icon_class);
		$(nextObj).css("vertical-align", "middle");

	};


	var orderColumNm = $(orderColumn).attr("id");
	var orderObj = $("#order_id");

	var allorderLabelComp = $("#orderLabel");
	$.each(allorderLabelComp,function(i,orderLabelComp){
		$(orderLabelComp).remove();
	});
	if(allorderLabelComp.length) {
		allorderLabelComp.remove();
	}

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


	if($("#orderLabel").length>0){
//		alert("found");
		$("#orderLabel").removeAttr("id");
		$("#orderLabel").removeAttr("class");
	}
	if(orderObj==null){
		required_parent_div.append("<input type='hidden' id='order_id' value="+orderColumNm+">");
		required_parent_div.append("<input type='hidden' id='order_type' value='desc'>");
		addOrUpdate_orderLabel_icon(orderColumn, "down");
	}
	else{
		var orderColumOld = orderObj.val();
		if(orderColumOld==orderColumNm){
			var orderTypeOld = $("#order_type").val();
			if(orderTypeOld=='desc'){
				$("#order_type").val("asc");
				addOrUpdate_orderLabel_icon(orderColumn, "up");
			}else{
				$("#order_type").val("desc");
				addOrUpdate_orderLabel_icon(orderColumn, "down");
			}
		}else{
			$("#order_id").remove();
			$("#order_type").remove();
			required_parent_div.append("<input type='hidden' id='order_id' value="+orderColumNm+">");
			required_parent_div.append("<input type='hidden' id='order_type' value='desc'>");
			addOrUpdate_orderLabel_icon(orderColumn, "down");
		}
	}


}