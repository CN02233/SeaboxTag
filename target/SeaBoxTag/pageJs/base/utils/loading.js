
function createLoading(){
	var loadingDiv = $("<div></div>");
	var dialogDiv = $("<div></div>");
	var contentDiv = $("<div></div>");
	var bodyDiv = $("<div></div>");
	
	bodyDiv.attr("class","modal-body");
	bodyDiv.append(" 处理中.......");
	contentDiv.attr("class","modal-content");
	contentDiv.append(bodyDiv);
	dialogDiv.attr("class","modal-dialog");
	dialogDiv.append(contentDiv);
	loadingDiv.attr("class","modal fade");
	loadingDiv.attr("id","loadingDiv");
	loadingDiv.attr("tabindex","-1");
	loadingDiv.attr("role","dialog");
	loadingDiv.attr("aria-hidden","true");
	loadingDiv.append(dialogDiv);
	
//	var closeBtn = $("<div></div>");
//	closeBtn.attr("display","none");
//	closeBtn.append("<button id='closeBtn' type='button' class='btn btn-default'  data-dismiss='modal'>关闭 </button>");
//	loadingDiv.append(closeBtn);
	
	$("body").append(loadingDiv);
	
	
	
	$("#loadingDiv").modal({backdrop:false,show:true});
//	$("#loadingDiv").modal({backdrop:"static",show:true});
}

function removeLoading(){
//	$("#closeBtn").click();
	$("#loadingDiv").remove();
//	$("#closeBtn").remove();
}

function checkLoading(){
	if($("#loadingDiv").length>0){
		return true;
	}
	return false;
}

function showLoading(){
	if($("#loadingDiv").length>0){
		$("#loadingDiv").modal({show:true});
	}else{
		createLoading();
	}
	
}

function hideLoading(){
	$("#loadingDiv").modal({show:false});
}

//<div class="modal fade" id="myModal" tabindex="-1" role="dialog" 
//   aria-labelledby="myModalLabel" aria-hidden="true">
//   <div class="modal-dialog">
//      <div class="modal-content">
//         <div class="modal-body">
//            处理中.......
//         </div>
//      </div><!-- /.modal-content -->
//</div><!-- /.modal -->