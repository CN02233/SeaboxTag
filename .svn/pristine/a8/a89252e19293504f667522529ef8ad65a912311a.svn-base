
/**
 * 弹出提示框方法
 * @param showMsg 需要显示到提示框中的文字
 * @param backBtnNm 上一步按钮的名称可能不同,如上一步 返回 关闭等,所以此次做成可配置
 * @param nextButtonNm 下一步按钮的名称可能不同,如下一步 通过 确定等,所以此次做成可配置
 * @param callback 提示框弹出后点击确定时的回调方法,在该回调方法中实现后续裸机
 * @param params 需要传递给callback的参数,可为空,此方法只做传递,不对其做任何处理
 * @param showTitle 弹出框title
 */
function createModalAlter(showMsg,backBtnNm,nextButtonNm,callback,params,showTitle){
	//创建modal div最外层
	var modalBodyDiv = $("<div></div>");
	modalBodyDiv.attr("id","modal_alter_div");
	modalBodyDiv.attr("tabindex","-1");
	modalBodyDiv.attr("role","dialog");
	modalBodyDiv.attr("aria-hidden","true");
	modalBodyDiv.attr("class","modal fade modal_div");
	
	var modalDialogDiv = $("<div></div>");
	modalDialogDiv.attr("class","modal-dialog");
	
	var modalContentDiv = $("<div></div>");
	modalContentDiv.attr("class","modal-content");
	
	if(showTitle==null)
		showTitle = "提&nbsp;&nbsp;示";
	var modalDivHeaderDiv = $("<div>"+showTitle+"<label class='close sprite-img-second' onclick='closeModal();'></label></div>");
	modalDivHeaderDiv.attr("class","modal_div_header");
	
	var modalDivContentDiv = $("<div></div>");
	modalDivContentDiv.attr("class","modal_div_content");
	
	var buttonsDiv = $("<div></div>");//按钮组DIV
	buttonsDiv.attr("class","modal_div_content_buttons");
	
	if(params!=null&&params.hide_back_btn != true){
		var backButton = $("<button id='returnButton' type='button'>"+backBtnNm+"</button>");//返回按钮
		backButton.attr("class","btn btn-active");
		backButton.attr("style","width:30%;height:100%;background-color:white;color:#6CABF6;border:1px solid #6CABF6;font-weight:bold;");
		backButton.attr("data-dismiss","modal");
		backButton.attr("data-toggle","button");
		backButton.click(function(){
			closeModal(params);
		});
	}
	
	var nextPreButton = $("<button id='nextButton' type='button'>"+nextButtonNm+"</button>");//下一步按钮
	nextPreButton.attr("class","btn btn-active");
//	console.log("!params.hide_back_btn2:" + !params.hide_back_btn);
	if(params!=null&&params.hide_back_btn != true){
		nextPreButton.attr("style","width:30%;height:100%;background-color:#6CABF6;color:white;margin-left:30px;");
	} else {
		nextPreButton.attr("style","width:30%;height:100%;background-color:#6CABF6;color:white;margin-left:9px;");
	}
	nextPreButton.attr("data-toggle","button");
	nextPreButton.click(function(){
		nextPre(callback,params);
	});
	
	buttonsDiv.append(backButton);
	buttonsDiv.append(nextPreButton);
	
	modalDivContentDiv.append("<div class='modal_div_content_text'>"+showMsg+"</div>");
	modalDivContentDiv.append(buttonsDiv);
	
	modalContentDiv.append(modalDivHeaderDiv);
	modalContentDiv.append(modalDivContentDiv);
	modalDialogDiv.append(modalContentDiv);
	modalBodyDiv.append(modalDialogDiv);
		
	$("body").append(modalBodyDiv);
	
	$("#modal_alter_div").modal({backdrop:false,show:true});
}

function closeModal(){
	$("#modal_alter_div").remove();
}

function nextPre(callback,params){
	$("#modal_alter_div").remove();//如果把这句放到callback之后,会导致:如果callback里面又调用createModalAlter建了个新提示框,会被这句清掉,无法显示,所以先清旧模态框,再调用回调方法
	if(callback!=null){
		callback(params);
	}
	
}