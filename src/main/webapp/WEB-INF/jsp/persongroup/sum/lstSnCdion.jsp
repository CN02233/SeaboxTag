<%--
  Created by IntelliJ IDEA.
  User: SongChaoqun
  Date: 2016/11/11
  Time: 10:46
  To change this template use File | Settings | File Templates.
--%>
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
<script src="${pageContext.request.contextPath}/seabox_js/persongroup/lstSnCdion.js"></script>
<script src="${pageContext.request.contextPath}/pageJs/base/extendPagination.js?v=${revision}"></script>

<form></form>

<div class="list_main title_div">
    <div >
        <button type="button" id="crt_screening" class="btn btn-primary">新建筛选条件</button>
    </div>
    <div style="line-height:40px;">
        <%--<div style="float:right;">--%>
            <%--&lt;%&ndash;<input class="seabox_text text_simple" type="text" />&ndash;%&gt;--%>
                <%--<div class="seabox_area">--%>
                    <%--<div class="seabox_glyphicon seabox_glyphicon_search" ></div>--%>
                <%--</div>--%>
                <%--<input class="seabox_text text_simple" placeholder="请输入查询条件" style="float: right;" type="text" />--%>
        <%--</div>--%>


    </div>
</div>

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
</body>
</html>
