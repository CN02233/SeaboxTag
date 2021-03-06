<!DOCTYPE html>
<%@ page language='java' pageEncoding='UTF-8' contentType='text/html;charset=UTF-8' %>
<html lang="en">
<head>
	<title>标签市场</title>
	<jsp:include  page="/pageStatic/seaboxBase.jsp"/>
	<script src="${localProjectName}/utils/echarts3.0.2/echarts.min.js?v=${revision}"></script>
</head>
<body>
<script src="${localProjectName}/utils/jquery/plugin/jquery.sparkline.min.js?v=${revision}"></script>


<script type='text/javascript' charset='utf-8' src="${localProjectName}/pageJs/usertags/tag/tagViewList.js?v=${revision}"></script>
<script type='text/javascript' charset='utf-8' src="${localProjectName}/pageJs/usertags/tag/tagCoverage.js?v=${revision}"></script>
<script type='text/javascript' charset='utf-8' src="${localProjectName}/pageJs/usertags/tag/tagMarketList.js?v=${revision}"></script>



<!-- 弹出提示框 start -->
<link href="${localProjectName}/pageCss/modal-div.css?v=${revision}" rel="stylesheet">
<link href="${localProjectName}/pageCss/usertags/tag/userTagsView.css?v=${revision}" rel="stylesheet">
<link href="${localProjectName}/pageCss/usertags/tag/dataTable.css?v=${revision}" rel="stylesheet">
<script src="${localProjectName}/pageJs/base/modal-div.js?v=${revision}"></script>
<!-- 弹出提示框 end -->
<script>
	$(document).ready(function(){
		var currPage = $("#currPage").val();
		listAllTagCategoriesByLevel(currPage);
	});

	var needConfirmRemoveFavor = false;


	/*
	function  refreshByLoadingData(currPage) {
		showPageForSplit('/usertags/tag/category/list-tops.do', listData, currPage);
	}
	*/

</script>


<!--

<style type="text/css" id="colored-blocks">
	.tag-view-separate{
		width:2%;
		height:100%;
		float:left;
	}

	.input_lable{
		background: url(/SeaBoxTag/images/searchImg.png) no-repeat;
		width: 15px;
	    height: 15px;
		cursor: pointer;
		position: absolute;
		margin-top:32px;
		margin-left:22%;
		top: 0;
	}

	.tag-view-l2-cat1 {
		width: 160px;height:75px;color: #f7f6ff; background-color: #284e91;
		text-align: center;
		padding:25px; border-radius: 10px;
	}


	.tag-view-l2-cat2 {
		width: 160px;height:75px;color: #f7f6ff; background-color: #E1BF00;
		align:"center";
		padding:25px; border-radius: 10px;
	}


	.tag-view-l2-cat3 {
		width: 160px;height:75px;color: #f7f6ff; background-color: #17CE6D;
		text-align: center;
		padding:25px; border-radius: 10px;
	}


	.tag-view-l2-cat4 {
		width: 160px;height:75px;color: #f7f6ff; background-color: #E6175F;
		align:"center";
		padding:25px; border-radius: 10px;
	}


	.tag-view-l1-cat {
		text-align:left;
		color: #0d0503;
	}

	.tag-view-l3-cat1 {
		width:30%;
		height:100%;
		float:left;
		color: #fef9ff;
		background-color: #56A1DB;
		 border-radius: 10px;
	}

	.tag-view-l3-cat2 {
		width:30%;
		height:100%;
		float:left;
		color: #fef9ff;
		background-color: #E9D045;
		 border-radius: 10px;
	}

	.tag-view-l3-cat3 {
		width:30%;
		height:100%;
		float:left;
		color: #fef9ff;
		background-color: #56DB95;
		 border-radius: 10px;
	}

	.tag-view-l3-cat4 {
		width:30%;
		height:100%;
		float:left;
		color: #fef9ff;
		background-color: #ED568A;
		 border-radius: 10px;
	}

	.tag-view-table {
		display:inline;
		float: left;
		color: #fef9ff;
		width: 49%;
	}

	.tag-view-last-td {
		color: #fef9ff;
		width: 100%;
	}

	.tag-view-l2-more-td {
		color: #fef9ff;
		width: auto;
		background-color: #3394ec;
	}

	.tag-view-l3-more-td {
		color: #fef9ff;
		width:30%;
		float:left;
		background-color: #3394ec;
	}


</style> id="new-damo-style"
-->


<style type="text/css">



	.sprite-img-market_icon {
		width: 52px;
		height: 52px;
		display: inline-block;
	}


	.grid-item {
		width: 49%;
		margin-bottom: 20px;
	}

	.gutter-sizer {
		width: 10px;
	}

	.grid {
		max-width: 1264px;
		min-width: 1250px;
	}

	div>a:hover {
	    background-color: #6CABF6;
	}

	.div_grid_cell_div_0 {
		width:auto;
		height:auto;
		float:left;
		border-width: 1px; border-color: #1b6d85;		border-style:none;  /* for debug */
	}

	.left_hide_margin_div_0_1 {
		width: 20px;  /*  left space length = 40 */
		height:100%;
		float: left;
		border-width: 1px; border-color: #1b6d85;		border-style:none;  /* for debug */
	}

	.right_body_div_0_2 {
		float:left;
		width: 590px;   /*  right part body = div_grid_cell_div_0 - space_div */
		border-width: 1px; border-color: #E4E4E4;   border-style:solid;  /* this border is required for product */
	    border-radius: 0px;
	}

	.l1_icon_and_text_div_0_2_1 {
		width: 103px;   /*  body.l1_text_icon = 103px */
		height:100%;
		float:left;
		text-align: center;
		border-width: 1px; border-color: #1b6d85;		border-style:none;  /* for debug */
	}

	.l1_icon_div_0_2_1_A {
		width: 100%;
		height: auto;
		padding-left: 20px;
		padding-right: 30px;
		padding-top: 20px;
	}

	.l1_text_div_0_2_1_B {
		width: 100%;
		height: auto;
		padding-bottom: 20px;
		font-weight: bold;
		font-size: 14px;
		color: #132851;
		text-align: left;
		padding-left: 20px;
	}

	.l2_l3_text_div_0_2_2 {
		width: 484px;  /*  body.l2_l3 = right_body_div_0_2 - l1_icon_and_text_div_0_2_1 -2px(border) = 486px */
		height:100%;
		border-radius: 0px;
		float: right ;
		padding-top: 0px; margin-top: 0px;
		border-width: 1px; border-color: #1b6d85;		border-style:none;  /* for debug */
	}


	.one_line_l2_l3_top_margin_div_0_2_2_A {
		width: 100%;
		height:25px;
		float:left ;
		padding-top: 0px; margin-top: 0px;
		border-width: 1px; border-color: #0fe8f5;		border-style:none;  /* for debug */
	}


	.one_line_l2_l3_add_more_margin_div_0_2_2_A {
		width: 100%;
		height:0px;  /* no need add more margin when only 1 line of L2_l3 exists */
		float:left ;
		padding-top: 0px; margin-top: 0px;
		border-width: 1px; border-color: #0fe8f5;		border-style:none;  /* for debug */
	}

	.one_line_l2_and_l3_div_0_2_2_B {
		width: 100%;
		height: auto;
		float:left ;
		padding-top: 0px; margin-top: 0px;
		border-width: 1px; border-color: #00bf00;		border-style:none;  /* for debug */
	}

	.one_line_l2_part_div_0_2_2_B_1 {
		width: 80px; height:auto;  /* right.l2 80px */
		float:left ;
		padding-top: 5px;
		padding-bottom: 5px;
		border-width: 1px; border-color: #00bf00;		border-style:none;  /* for debug */
	}

	.one_line_l3_part_div_0_2_2_B_2 {
		width: 400px; height:auto;  /* right.l3 =  l2_l3_text_div_0_2_2 - one_line_l2_part_div_0_2_2_B_1 - 2px(border) = 400px */
		float:left;
		text-align: left;
		padding-top: 5px;
		padding-bottom: 5px;
		border-width: 1px; border-color: #002a80;		border-style:none;  /* for debug */
	}

    .one_line_l2_l3_bottom_margin_div_0_2_2_C {
		width: 100%;
		height:25px;
		float:left ;
		border-width: 1px; border-color: #0fe8f5;		border-style:none;  /* for debug */
    }

	.l2_text {
		font-weight: bold;
		font-size: 14px;
		color: #333333;
	}

	.l3_text {
		font-weight: normal;
		font-size: 14px;
		color: #333333;
	}

	.virtual_hide_table_cell {
		border-color: transparent; border-width: 0px; border-style:none;
	}

</style>

	<div id="titleDiv" class="body-title" style="display: inline-block;">标签市场</div>

<div id="splitDiv" class="split_title" style="display: inline-block;"><!-- title和内容页的分割线START-----splitDiv -->
</div>


<table  class="virtual_hide_table_cell" style="width: 100%; display: inline-block; padding-left: 20px; padding-right: 40px;">

	<!-- title和内容页的分割线END-----splitDiv -->
	<tbody>
		<tr class="virtual_hide_table_cell">
			<th class="virtual_hide_table_cell" style="height:40px;" ></th>
		</tr>
	</tbody>

	<tbody>
		<tr class="virtual_hide_table_cell" >
			<td class="virtual_hide_table_cell" >
				<div id="tag-view-parent-div" style="display: inline-block; width: 100%;  height: 100%; float: left; border-width: 0px; border-style:none;"  class="grid" />
			</td>
		</tr>
	</tbody>

</table>










<form>
	<input type="hidden" id="dir-to-cat-subs"/>
</form>
</body>
</html>