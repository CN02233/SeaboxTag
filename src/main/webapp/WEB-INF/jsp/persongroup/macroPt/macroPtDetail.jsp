<%--
  Created by IntelliJ IDEA.
  User: SongChaoqun
  Date: 2016/11/11
  Time: 10:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<head>
    <meta charset="utf-8">
    <title>ECharts</title>
    <style>
        html,body{
            width:100%;
            height:100%;
            overflow: hidden;
        }


        .previous>span{
            margin-right:10px;
            margin-top:3px;
            background-color:red;
        }

        .top_10_title {
            font-size: 20px;
            color:blue;
            border:0px;
        }

        .grp_top_name {
            width:20%;
            float:left;
            border:0px solid red;
        }
        .grp_tag_body{
            width:80%;
            float:left;
            border:0px solid red;

        }
        .left_body{
            float:left;
            width:50%;
            height:100%;
            border-right:1px solid black;
        }
    </style>
    <link href="${pageContext.request.contextPath}/utils/bootstrap/css/bootstrap.css" rel="stylesheet">

</head>
<input type="hidden" id="screeningId" value="${screeningId}"/>

<body style="border:0px solid red;margin:0px; height:100%;">
<script src="${pageContext.request.contextPath}/utils/jquery/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/utils/bootstrap/js/bootstrap.min.js"></script>
<script src="${pageContext.request.contextPath}/pageJs/base/seabox_public.js"></script>

<div id="left_body" class="left_body">
    <!-- 为ECharts准备一个具备大小（宽高）的Dom -->
    <div id="top10_main" style="width:100%;height:70%;"></div>
    <div id="top10_count" style="height:40%;width:100%;padding-left:20px;padding-right:20px;border-top:0px solid black;">
        <nav >
            <ul class="pager">
                <li class="previous"><span class="top_10_title" style="border:0px;" href="#">TOP10 标签选择试算</span></li>
                <li class="next"><span class="top_10_title" style="border:0px;" href="#">人群数量：<span id="top10_sum_count">0</span></span></li>
            </ul>
            <ul  class="pager">
                <li class="previous" id="top10_count_li">
                </li>
            </ul>
        </nav>
    </div>
</div>

<div id="right_body" style="float:left;width:50%;height:100%;border:0px solid black;overflow: auto;">
    <div id="grp_top10_main" style="width:100%;height:70%;"></div>
    <div id="screening_div" style="height:40%;width:100%;padding-left:20px;padding-right:20px;border-top:0px solid black;">

    </div>
</div>


<!-- ECharts单文件引入 -->
<script src="${pageContext.request.contextPath}/utils/echarts/js/echarts.js"></script>
<script type="text/javascript">
    var fullPath = "${pageContext.request.contextPath}";
</script>
<script src="${pageContext.request.contextPath}/seabox_js/persongroup/macroPtDetail.js"></script>

</body>