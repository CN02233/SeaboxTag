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
    <link href="${pageContext.request.contextPath}/pageCss/modal-div.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/seabox_css/persongroup/micro_person.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/pageCss/tablist.css" rel="stylesheet" media="screen" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/utils/jquery/plugin/jquery.mCustomScrollbar.min.css?v=${revision}">

</head>
<body style="background-color: #eff3f6;overflow-y:hidden;">
<script src="${pageContext.request.contextPath}/utils/jquery/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/utils/bootstrap/js/bootstrap.min.js"></script>
<script src="${pageContext.request.contextPath}/pageJs/base/seabox_public.js"></script>
<script src="${pageContext.request.contextPath}/pageJs/base/modal-div.js"></script>

<script src="${pageContext.request.contextPath}/seabox_js/seabox_public/menu_tree.js"></script>
<script src="${pageContext.request.contextPath}/seabox_js/seabox_public/seabox_glyphicon.js"></script>
<script src="${pageContext.request.contextPath}/utils/icheck/icheck.min.js"></script>
<script src="${pageContext.request.contextPath}/utils/jquery/plugin/jquery.mCustomScrollbar.concat.min.js?v=${revision}"></script>
<script src="${pageContext.request.contextPath}/seabox_js/persongroup/personPage.js"></script>
<script src="${pageContext.request.contextPath}/pageJs/base/extendPagination.js?v=${revision}"></script>


<form></form>
<input type="hidden" id="pageType" value="${pageType}">
<input type="hidden" id="screeningId" value="${screeningId}"/>
<input type="hidden" id="screeningNm" value="${screeningNm}"/>

<%--<div class="list_main title_div">--%>
    <%--<div >--%>
        <%--&lt;%&ndash;<button type="button" id="crt_screening" class="btn btn-primary">新建筛选条件</button>&ndash;%&gt;--%>
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
            <th >姓名</th>
            <th >性别</th>
            <th >年龄</th>
            <th >民族</th>
            <th >是否已婚</th>
            <th >操作</th>
        </tr>
        </thead>
        <tbody class="tbody_boder">

        </tbody>
    </table>
    <div id="paginationDiv" class="pageit_div"></div>

    <!-- 模态框（Modal） -->
    <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content" style="box-shadow: #666 0px 5px 10px 5px;">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                        &times;
                    </button>
                    <h3 class="modal-title" id="myModalLabel">
                        张三--------所属人群:大龄未婚男青年
                    </h3>
                </div>
                <div class="modal-body">
                    <table id="user_tag_tbl">
                        <tr style="border-bottom:1px solid #AFB0E3;">
                            <td style="width:30%;">
                                <nav >
                                    <ul class="pager">
                                        <li class="previous" ><span style="border:0px" href="#">性别:男</span></li>
                                    </ul>
                                </nav>
                            </td>
                            <td>
                                <nav >
                                    <ul class="pager">
                                        <li class="previous" ><span  href="#">性别:男</span></li>
                                        <li class="previous" ><span  href="#">年龄>30</span></li>
                                        <li class="previous"><span  href="#">民族:汉</span></li>
                                        <li class="previous"><span   href="#">学历:本科</span></li>
                                        <li class="previous"><span   href="#">学历:本科</span></li>
                                        <li class="previous"><span   href="#">学历:本科</span></li>
                                        <li class="previous"><span   href="#">学历:本科</span></li>
                                        <li class="previous"><span   href="#">学历:本科</span></li>
                                        <li class="previous"><span   href="#">学历:本科</span></li>
                                        <li class="previous"><span   href="#">学历:本科</span></li>
                                    </ul>
                                </nav>
                            </td>
                        </tr>
                        <tr  style="border-bottom:1px solid #AFB0E3;">
                            <td style="width:30%;">
                                <nav >
                                    <ul class="pager">
                                        <li class="previous" ><span style="border:0px" href="#">性别:男</span></li>
                                    </ul>
                                </nav>
                            </td>
                            <td>
                                <nav >
                                    <ul class="pager">
                                        <li class="previous" ><span  href="#">性别:男</span></li>
                                        <li class="previous" ><span  href="#">年龄>30</span></li>
                                        <li class="previous"><span  href="#">民族:汉</span></li>
                                        <li class="previous"><span   href="#">学历:本科</span></li>
                                        <li class="previous"><span   href="#">学历:本科</span></li>
                                        <li class="previous"><span   href="#">学历:本科</span></li>
                                        <li class="previous"><span   href="#">学历:本科</span></li>
                                        <li class="previous"><span   href="#">学历:本科</span></li>
                                        <li class="previous"><span   href="#">学历:本科</span></li>
                                        <li class="previous"><span   href="#">学历:本科</span></li>
                                    </ul>
                                </nav>
                            </td>
                        </tr>
                        <tr  style="border-bottom:1px solid #AFB0E3;">
                            <td style="width:30%;">
                                <nav >
                                    <ul class="pager">
                                        <li class="previous" ><span style="border:0px" href="#">性别:男</span></li>
                                    </ul>
                                </nav>
                            </td>
                            <td>
                                <nav >
                                    <ul class="pager">
                                        <li class="previous" ><span  href="#">性别:男</span></li>
                                        <li class="previous" ><span  href="#">年龄>30</span></li>
                                        <li class="previous"><span  href="#">民族:汉</span></li>
                                        <li class="previous"><span   href="#">学历:本科</span></li>
                                        <li class="previous"><span   href="#">学历:本科</span></li>
                                        <li class="previous"><span   href="#">学历:本科</span></li>
                                        <li class="previous"><span   href="#">学历:本科</span></li>
                                        <li class="previous"><span   href="#">学历:本科</span></li>
                                        <li class="previous"><span   href="#">学历:本科</span></li>
                                        <li class="previous"><span   href="#">学历:本科</span></li>
                                    </ul>
                                </nav>
                            </td>
                        </tr>
                        <tr  style="border-bottom:1px solid #AFB0E3;">
                            <td style="width:30%;">
                                <nav >
                                    <ul class="pager">
                                        <li class="previous" ><span style="border:0px" href="#">性别:男</span></li>
                                    </ul>
                                </nav>
                            </td>
                            <td>
                                <nav >
                                    <ul class="pager">
                                        <li class="previous" ><span  href="#">性别:男</span></li>
                                        <li class="previous" ><span  href="#">年龄>30</span></li>
                                        <li class="previous"><span  href="#">民族:汉</span></li>
                                        <li class="previous"><span   href="#">学历:本科</span></li>
                                        <li class="previous"><span   href="#">学历:本科</span></li>
                                        <li class="previous"><span   href="#">学历:本科</span></li>
                                        <li class="previous"><span   href="#">学历:本科</span></li>
                                        <li class="previous"><span   href="#">学历:本科</span></li>
                                        <li class="previous"><span   href="#">学历:本科</span></li>
                                        <li class="previous"><span   href="#">学历:本科</span></li>
                                    </ul>
                                </nav>
                            </td>
                        </tr>
                    </table>
                </div>

            </div><!-- /.modal-content -->
        </div><!-- /.modal -->
    </div>


</div>


<script>
    listPerson();
</script>

</body>
</html>
