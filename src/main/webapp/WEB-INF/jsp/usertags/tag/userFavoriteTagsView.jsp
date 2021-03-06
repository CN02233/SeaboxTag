<!DOCTYPE html>
<%@ page language='java' pageEncoding='UTF-8' contentType='text/html;charset=UTF-8' %>
<html lang="en">
<head>
    <title>标签收藏夹</title>
    <jsp:include  page="/pageStatic/seaboxBase.jsp"/>
    <script src="${localProjectName}/utils/echarts3.0.2/echarts.min.js?v=${revision}"></script>
</head>
<body>
<script src="${localProjectName}/utils/jquery/plugin/jquery.sparkline.min.js?v=${revision}"></script>

<script type='text/javascript' charset='utf-8' src="${localProjectName}/pageJs/usertags/tag/tagViewList.js?v=${revision}"></script>
<script type='text/javascript' charset='utf-8' src="${localProjectName}/pageJs/usertags/tag/tagCoverage.js?v=${revision}"></script>


<!-- 弹出提示框 start -->
<link href="${localProjectName}/pageCss/modal-div.css?v=${revision}" rel="stylesheet">
<link href="${localProjectName}/pageCss/usertags/tag/userTagsView.css?v=${revision}" rel="stylesheet">
<link href="${localProjectName}/pageCss/usertags/tag/dataTable.css?v=${revision}" rel="stylesheet">
<script src="${localProjectName}/pageJs/base/modal-div.js?v=${revision}"></script>
<!-- 弹出提示框 end -->
<script>
    $(document).ready(function(){
        var currPage = $("#currPage").val();
        refreshByLoadingData(currPage);
    });

    var needConfirmRemoveFavor = true;

    function  refreshByLoadingData(currPage) {
        showPageForSplit('/usertags/tag/favors/list.do', listData, currPage);
    }


    function doSearchFilter(e) {
        if(e ==13|| e ==32)
        {
            var currPage = $("#currPage").val();
            refreshByLoadingData(currPage);
        }
    }

</script>
<input type="hidden" id="currPage" name="currPage" value="${currPage}" />

<div id="titleDiv" class="body-title">标签收藏夹
    <div id="searchDiv" class="title-search-div">
        <input id="search_context" onkeydown="doSearchFilter(event.keyCode||event.which);" class="form-control contextInput" style="background-color:#F7F7F7" type="text" value="" placeholder="按照 标签类别名,ID,或描述 查找" >
        <label id="seachLabel" class="input_lable"  onclick="refreshByLoadingData( $('#currPage').val() );"></label><!-- CSS在evtView.css中定义 -->
    </div>
</div>


<div id="splitDiv" class="split_title"><!-- title和内容页的分割线START-----splitDiv -->
</div>	<!-- title和内容页的分割线END-----splitDiv -->
<div id="operationDiv" class="no_operation_div">
</div><!-- operationDiv end 操作按钮DIV -->
<div class="table-responsive data-list-table-div">
    <table class="seabox-table"> <!-- table size and style are aligned in "seabox-table" -->
        <tbody>
        <tr class="thread-tr">
            <th hidden="hidden" class='title_td_css td_col_0_tag_id_hide'>id</th>

            <th class="title_td_css td_col_A_tag_name"  style='text-align:left;padding-left:40px;'>
                <span id="tag_ctgy_nm" onclick="orderList(this)" >标签类别名</span>
            </th>
            <th class='title_td_css td_col_B_tag_desc'>
                <span id="tag_desc" onclick="orderList(this)" >描述</span>
            </th>
            <th class='title_td_css td_col_C_tag_valid'>
                <span id="disabled_dt" onclick="orderList(this)" >有效期</span>
            </th>
            <th class='title_td_css td_col_D_tag_cover' style='text-align:right;padding-right:30px;'>
                <span id="covered_rate" onclick="orderList(this)" >覆盖率(%)</span>
            </th>
            <th class='title_td_css td_col_E_tag_op' style="padding-bottom:0px;">
                <!-- 收藏里面,所有列出的标签类别,都是被收藏的, 所以对收藏排序 没意义 -->
                <span id="tagFavo"  style="cursor:default;">操作</span>
            </th>

        </tr>
        </tbody>
    </table>

    <table class="table seabox-table"><!-- table size and style are aligned in "seabox-table" -->
        <tbody id="fill-list-data-async"></tbody>
    </table>

    <div id="paginationDiv" class="pageit_div" style="float:right;"></div>


</div>
<jsp:include  page="/pageStatic/seaboxBaseOnload.jsp"/>

<!--
<div id="tagCoverRateChartModal_for_test" class="modal fade modal_div template_modal"  role="dialog"  aria-labelledby="myModalLabel" aria-hidden="true" >

    <div class="modal-dialog" style="width: 450px; height: 550px;">
        <div class="modal-content" style="width: 450px; height: 550px;">
            <div class="modal_div_header">年龄<label class="close sprite-img-second" ></label></div>
            <div class="modal_div_content" id="tagCoverChartArea_for_test" style="width: 450px; height: 550px;">
            </div>
        </div>
    </div>

</div>
-->

</body>
</html>
