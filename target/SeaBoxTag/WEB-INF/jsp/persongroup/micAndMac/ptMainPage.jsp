<%--
  Created by IntelliJ IDEA.
  User: SongChaoqun
  Date: 2016/11/11
  Time: 10:46
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>

    <!-- Bootstrap -->
    <link href="${pageContext.request.contextPath}/utils/bootstrap/css/bootstrap.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/seabox_css/seabox_public/menu_tree.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/seabox_css/main_page/main.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/utils/icheck/skins/square/square.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/seabox_css/persongroup/lstSnCdion.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/seabox_css/seabox_public/seabox_text.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/seabox_css/seabox_public/seabox_glyphicon.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/pageCss/tablist.css" rel="stylesheet" media="screen" />

</head>
<body style="background-color: #eff3f6;overflow-y:hidden;">
<script src="${pageContext.request.contextPath}/utils/jquery/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/utils/bootstrap/js/bootstrap.min.js"></script>
<script src="${pageContext.request.contextPath}/pageJs/base/seabox_public.js"></script>

<script src="${pageContext.request.contextPath}/seabox_js/seabox_public/menu_tree.js"></script>
<script src="${pageContext.request.contextPath}/seabox_js/seabox_public/seabox_glyphicon.js"></script>
<script src="${pageContext.request.contextPath}/utils/icheck/icheck.min.js"></script>
<script src="${pageContext.request.contextPath}/pageJs/base/extendPagination.js?v=${revision}"></script>

<c:if test="${pageType=='macroPt' }">
    <script src="${pageContext.request.contextPath}/seabox_js/persongroup/macroPtMain.js"></script>
</c:if>
<c:if test="${pageType=='microPt' }">
    <script src="${pageContext.request.contextPath}/seabox_js/persongroup/microPtMain.js"></script>
</c:if>


<form></form>

<input type="hidden" id="pageType" value="${pageType}">

<%--<div class="list_main title_div">--%>
    <%--<div >--%>
    <%--</div>--%>
    <%--<div style="line-height:40px;">--%>
        <%--<div style="float:right;">--%>
            <%--&lt;%&ndash;<input class="seabox_text text_simple" type="text" />&ndash;%&gt;--%>
            <%--<div class="seabox_area">--%>
                <%--<div class="seabox_glyphicon seabox_glyphicon_search" ></div>--%>
            <%--</div>--%>
            <%--<input class="seabox_text text_simple" placeholder="请输入查询条件" style="float: right;" type="text" />--%>
        <%--</div>--%>


    <%--</div>--%>
<%--</div>--%>

<div  class="list_main">

    <!-- Table -->
    <table class="table table-striped " style="text-align: center;">
        <%--<caption>条纹表格布局</caption>--%>
        <thead>
        <tr class="table_title">
            <th >序号</th>
            <th >名称</th>
            <th >状态</th>
            <th >创建人</th>
            <th >创建日期</th>
            <th >操作</th>
        </tr>
        </thead>
        <tbody class="tbody_boder">

        </tbody>
    </table>

</div>
<div id="paginationDiv" class="pageit_div"></div>

<script>
    showPageForSplit("/screen/sum/listMainData.do",listScreeningData,1,8);

    /**
     * @param resultData
     */
    function listScreeningData(resultData) {
        // alert(JSON.stringify(resultData));
        var dataList = resultData.list;

        var tbodyBorder = $(".tbody_boder");
        tbodyBorder.empty();
        $.each(dataList, function (i, screeningData) {
            var screeningId = screeningData.screeningId;
            var screeningNm = screeningData.screeningNm;
            var screeningDes = screeningData.screeningDes;
            var userNm = screeningData.userNm;
            var statsNm = screeningData.statsNm;
            var createTs = screeningData.createTs;
            var updateTs = screeningData.updateTs;
            var tr = $("<tr></tr>");
            tr.append("<td>" + screeningId + "</td>");
            tr.append("<td>" + screeningNm + "</td>");
            tr.append("<td>" + statsNm + "</td>");
            tr.append("<td>" + userNm + "</td>");
            tr.append("<td>" + createTs + "</td>");
            var optionTd = $("<td class='option_td'></td>");

            var viewHref = $('<label>查看</label>');
            viewHref.attr("screeningId", screeningId);
            viewHref.attr("screeningNm", screeningNm);


            var pageTypeVal = $("#pageType").val();

            if(pageTypeVal=='microPt' ){
                viewHref.click(function () {
                    viewPersonList(this);
                });
            }else if(pageTypeVal=='macroPt'){
                viewHref.click(function () {
                    viewMacDetail(this);
                });
            }

            optionTd.append(viewHref);
            tr.append(optionTd);
            // tr.append("<td>编辑|删除</td>");
            tbodyBorder.append(tr);
        });
    }

    /**
     * 显示当前筛选条件圈定的人群列表
     */
    function viewPersonList(screeningId){

        var param = new Array();
        var screeningIdVal = $(screeningId).attr("screeningId");
        var screeningNmVal = $(screeningId).attr("screeningNm");
        param.push({id:"screeningId",value:screeningIdVal});
        param.push({id:"screeningNm",value:screeningNmVal});
//        param.id = "screeningId";
//        param.value = screeningId;
        newPageByPost(param,"/person/personPage.do");
    }

</script>

</body>
</html>
