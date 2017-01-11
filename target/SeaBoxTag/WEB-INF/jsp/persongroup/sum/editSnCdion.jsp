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
<script src="${pageContext.request.contextPath}/seabox_js/persongroup/sum/editSnCdion.js"></script>
<script src="${pageContext.request.contextPath}/pageJs/base/modal-div.js"></script>


<input type="hidden" id="screeningId" value="${screeningId}"/>

<%--标签树,页面最左侧(类菜单导航方式显示)--%>
<div id="tag_tree" class="tag_tree" >
    <ul class="menu_tree_ul collapsed" >
        <li id="menu_area">
            <%--<div class="menu_tree_div menu_tree_first">--%>
                <%--<span class="glyphicon glyphicon-th-list" ></span> 社会特征--%>
            <%--</div>--%>
            <%--<ul class="menu_tree_ul collapse" >--%>
                <%--<li >--%>
                    <%--<div class="menu_tree_child have_bottom">--%>
                        <%--<div class="second_tree_before"></div>--%>
                        <%--工作特征--%>
                        <%--<label  class="sprite-img-six second_tree_after"></label>--%>
                    <%--</div>--%>
                    <%--<ul class="menu_tree_ul collapse">--%>
                        <%--<li >--%>
                            <%--<div class="menu_tree_child trd_tree_div">--%>
                                <%--<span></span> 工作状态--%>
                            <%--</div>--%>
                            <%--<ul class="menu_tree_ul collapse tag_info">--%>
                                <%--<li><input type="radio" name="iCheck1" />--%>
                                    <%--<span >在职</span> </li>--%>
                                <%--<li><input type="radio" name="iCheck2"/>--%>
                                    <%--<span >兼职</span> </li>--%>
                                <%--<li><input type="radio" name="iCheck3"/>--%>
                                    <%--<span >待业</span></li>--%>
                            <%--</ul>--%>
                        <%--</li>--%>
                    <%--</ul>--%>

                <%--</li>--%>
                <%--<li >--%>
                    <%--<div class="menu_tree_child have_bottom">--%>
                        <%--<div class="second_tree_before"></div>--%>
                        <%--工作特征--%>
                        <%--<label  class="sprite-img-six second_tree_after"></label>--%>
                    <%--</div>--%>
                    <%--<ul class="menu_tree_ul collapse">--%>
                        <%--<li >--%>
                            <%--<div class="menu_tree_child trd_tree_div">--%>
                                <%--<span></span> 工作状态--%>
                            <%--</div>--%>
                            <%--<ul class="menu_tree_ul collapse tag_info">--%>
                                <%--<li><input type="radio" name="iCheck1" />--%>
                                    <%--<span >在职</span> </li>--%>
                                <%--<li><input type="radio" name="iCheck2"/>--%>
                                    <%--<span >兼职</span> </li>--%>
                                <%--<li><input type="radio" name="iCheck3"/>--%>
                                    <%--<span >待业</span></li>--%>
                            <%--</ul>--%>
                        <%--</li>--%>
                    <%--</ul>--%>

                <%--</li>--%>
<%--</ul>--%>

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
        <%--<div class="tag_grp_split_line"></div>--%>
        <div class="op_place">
            <div>
                <nav>
                    <ul class="pager">
                        <li class="add_tag_grp">
                            <a href="#" id="add_stg_grp">
                                <span>
                                    <span class="glyphicon glyphicon-plus" ></span>添加标签组
                                </span>
                            </a>
                        </li>
                    </ul>
                </nav>
            </div>

            <div class="tag_grp_split_solid_line"></div>


            <div>
                <nav>
                    <ul class="pager">
                        <li class="add_tag_grp">
                            <button type="button" id="save_btn" class="btn btn-default btn_seabox">暂存</button>

                        </li>

                        <li  class="add_tag_grp">
                            <button type="button" id="submit_btn" class="btn btn-default btn_seabox">提交</button>

                        </li>
                    </ul>
                </nav>
            </div>

        </div>

        </div>


    </div>


</div>


</body>
</html>
