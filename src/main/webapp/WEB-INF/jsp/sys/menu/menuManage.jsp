<!DOCTYPE html>
<%@ page language='java' pageEncoding='UTF-8' contentType='text/html;charset=UTF-8' %>
<%@ include file="/pageStatic/seaboxBase.jsp" %>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <jsp:include  page="/pageStatic/seaboxBase.jsp"/>

 	<script type="text/javascript">
 	 function changeMenuState(parent_menu_id,menu_id,isable,parent_menu_name,menu_ch_name){
		 var param='parent_menu_id='+parent_menu_id+'&menu_id='+menu_id+"&isable="+isable;
 		var confirmStr='您确定要';
 		if(isable==0){
 			 confirmStr+='启用';
 		 }else if(isable==1){
 			confirmStr+='禁用';
 		 }
 		 if(menu_ch_name==null |menu_ch_name==''){
 			confirmStr=confirmStr+parent_menu_name+'吗?,该菜单为一级菜单,您的操作将会对其所有子菜单生效。'
 		 }else{
 			confirmStr=confirmStr+menu_ch_name+'吗?';
 		 }
		 var ischg=confirm(confirmStr);
		 if(!ischg){
			 return;
		 }
 		 jQuery.ajax({
	  			type : 'get',
	  			contentType : 'application/json',
	  			url : 'updateMenuState.do',
	  			data:param,
	  			dateType:'json',
	  			success : function(data) {
	  				if(data.result=="success"){
	  					alert(data.msg);
	  					window.location="main.do";
	  				}else if(data.result=="error"){
	  					alert(msg);
	  				}
	  			},
	  		});
	   }
 	 
 	 	function init(currPage){
 	 		var pageSize=10;
 	 		jQuery.ajax({
 	 			type:"get",
 	 			contentType:"application/json",
 	 			url:"findMenuList.do",
 	 			data:"currPage="+currPage+"&pageSize="+pageSize,
 	 			dataType:"json",
 	 			success:function(data){
 	 				if(data.totalNum==0){return;}
 	 				createPageFoot(data,$("#pageFoot"));
 	 				dataInit(data,$("#dataTable"));
 	 			}
 	 		}); 
 	 	}
 	 	function createPageFoot(data,pagediv){
 	 		//当前页
 	 		var currPage=data.currPage;
 	 		//总记录数量
 	 		var totalNum=data.totalNum;
 	 		//每页展示数据条数 
 	 		var pageSize=data.pageSize;
 	 		//总分页
 	 		var totalPage=(totalNum%pageSize==0)?(totalNum/pageSize):(Math.ceil(totalNum/pageSize));
 	 		/* var maxPage=10; */
 	 		var beginPage=1;
 	 		var endPage=totalPage;
 	 		/* if(totalPage>10){
 	 			beginPage=Math.round(currPage/maxPage)+1;
 	 			if(currPage<(Math.round(totalPage/maxPage)+1)*maxPage){
	 	 			endPage=Math.round(currPage/maxPage)+maxPage;
 	 			}
 	 		} */
 	 		var pagefoot="";
 	 		if(currPage>1){
	 	 		pagefoot+="<span class='pageIndex' onclick=init("+(currPage-1)+")>&lt;</span>";
 	 		}else{
	 	 		pagefoot+="<span class='pageIndex'>&lt;</span>";
 	 		}
 	 		for(var i=0;i<totalPage;i++){
 	 			if(currPage==(i+1)){
	 	 			pagefoot+="<span class='currentPage' onclick=init("+(i+1)+")>"+(i+1)+"</span>";
 	 			}else{
	 	 			pagefoot+="<span class='pageIndex' onclick=init("+(i+1)+")>"+(i+1)+"</span>";
 	 			}
 	 		}
 	 		if(currPage<totalPage){
	 	 		pagefoot+="<span class='pageIndex' onclick=init("+(currPage+1)+")>&gt;</span>";
 	 		}else{
	 	 		pagefoot+="<span class='pageIndex'>&gt;</span>";

 	 		}
 	 		pagediv.empty();
 	 		pagediv.append(pagefoot);
 	 	}
 	 	function dataInit(data,tabObj){
 	 		$("table tbody tr td").html("&nbsp;");
 	 		$.each(data.list,function(i,menu){
 	 			var j=i+1;
 	 			$("table tr:eq("+j+") td:eq(0)").html(menu.menu_id);
 	 			$("table tr:eq("+j+") td:eq(1)").html(menu.parent_menu_name);
 	 			$("table tr:eq("+j+") td:eq(2)").html(menu.menu_ch_name);
 	 			
 	 			if(menu.isable==0){
	 	 			$("table tr:eq("+j+") td:eq(3)").html("已启用");
 		 			$("table tr:eq("+j+") td:eq(4)").html("<a onclick='javascript:;changeMenuState("+menu.parent_menu_id+","+menu.menu_id+","+1+",\""+menu.parent_menu_name+"\",\""+menu.menu_ch_name+"\")'>禁用</a>");
 	 			}else{
 	 				$("table tr:eq("+j+") td:eq(3)").html("已禁用");
 		 			$("table tr:eq("+j+") td:eq(4)").html("<a onclick='javascript:;changeMenuState("+menu.parent_menu_id+","+menu.menu_id+","+0+",\""+menu.parent_menu_name+"\",\""+menu.menu_ch_name+"\")'>启用</a>");
 	 			}
 	 			
 	 		});
 	 	}
  </script>
  </head>
  <body style="height:70%" onload="init(1)">
  	<div style="width:100%;height:70%">
    	<div class="body-title" style="height:40px;line-height:40px" id="titleDiv">用户管理</div>
    <div id="bodyDiv" style="width:100%;height:543px;background:#EFF3F6;float:left">
    		<table style="width:95%;height:460px;margin:0 auto;margin-top:16px;" id="dataTable">
    			<thead><tr><td>序号</td><td>模块名称</td><td>菜单名称</td><td>状态</td><td>操作</td></tr></thead>
				<tbody>
				<tr><td>1</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>
				<tr><td>2</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>
				<tr><td>3</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>
				<tr><td>4</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>
				<tr><td>5</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>
				<tr><td>6</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>
				<tr><td>7</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>
				<tr><td>8</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>
				<tr><td>9</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>
				<tr><td>10</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>
				</tbody>
			</table>
			<div style="width:100%;height:50px;padding:0px 28px;" id="pageFoot">
			<!-- <span class="pageIndex" style="float:right">&gt;</span>
			<span class="currentPage" style="float:right">1</span>
			<span class="pageIndex" style="float:right">&lt;</span> -->
			</div>
   	 </div>
    </div>	
  </body>
</html>