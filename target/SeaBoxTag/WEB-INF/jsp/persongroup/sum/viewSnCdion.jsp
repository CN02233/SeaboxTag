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
    <link href="${pageContext.request.contextPath}/pageCss/modal-div.css" rel="stylesheet">


</head>
<body style="background-color: #eff3f6;overflow-y:hidden;">
<script src="${pageContext.request.contextPath}/utils/jquery/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/utils/bootstrap/js/bootstrap.min.js"></script>
<script src="${pageContext.request.contextPath}/pageJs/base/seabox_public.js"></script>

<script src="${pageContext.request.contextPath}/seabox_js/seabox_public/menu_tree.js"></script>
<script src="${pageContext.request.contextPath}/utils/icheck/icheck.min.js"></script>
<script src="${pageContext.request.contextPath}/seabox_js/persongroup/sum/viewSnCdion.js"></script>
<script src="${pageContext.request.contextPath}/pageJs/base/modal-div.js"></script>


<input type="hidden" id="screeningId" value="${screeningId}"/>

<%--标签树,页面最左侧(类菜单导航方式显示)--%>
<div id="tag_tree" class="tag_tree" >
    <ul class="menu_tree_ul collapsed" >
        <li id="menu_area">

        </li>

    </ul>



</div>
<%--标签树 结束--%>



<div id="main_div" class="tag_main">

    <div>
        <div class="sum_place">
            <div >
                <table class="title_table" >
                    <tr >
                        <td >
                            <h4 style="float:left;">群组名称：</h4>
                            <div><input type="text" id="screeningNm" name="群组名称" class="form-control nm_text" readonly/></h4></div>


                        </td>
                        <td style="text-align: right;"><h4 >人群数量：<font>102110</font></h4></td>
                    </tr>
                </table>

            </div>
            <div style="width:97%;height:1px;margin-top:10px;margin-left:1.5%;margin-right:1.5%;border-top:1px solid #AFB0E3;overflow:hidden;"></div>
        </div>


        <div class="tag_group_place" >

            <div class="allow_edit">
                <div class="grp_name">
                    标签组
                </div>
                <div class="tag_grp">
                    <nav>
                        <ul class="pager">

                            <%--<li id="rm_tg_grp_li" class="previous"><a href="#" onclick="removeTagGrp('tag_grp_seq_2')">删除此标签组</a></li>--%>
                        </ul>
                    </nav>
                </div>
                <div class="tag_grp_split_line"></div>
            </div>

        </div>

        </div>


    </div>


</div>


</body>
</html>
