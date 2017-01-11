<!DOCTYPE html>
<%@ page language='java' pageEncoding='UTF-8' contentType='text/html;charset=UTF-8' %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="ctx" value="${pageContext.request.contextPath }"/>
<html lang="zh-cn">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>东方金信标签管理系统</title>
	<link href="${pageContext.request.contextPath}/utils/bootstrap/css/bootstrap.css" rel="stylesheet">
	<link href="${pageContext.request.contextPath}/seabox_css/main_page/main.css" rel="stylesheet">
</head>
<body>



<!--[if lt IE 9]>
<script src="${pageContext.request.contextPath}/utils/bootstrap/js/html5shiv.min.js"></script>
<script src="${pageContext.request.contextPath}/utils/bootstrap/js/respond.min.js"></script>
<![endif]-->
<script src="${pageContext.request.contextPath}/pageJs/base/seabox_public.js"></script>
<script src="${pageContext.request.contextPath}/utils/jquery/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/utils/bootstrap/js/bootstrap.min.js"></script>
<script src="${pageContext.request.contextPath}/seabox_js/main_page/main.js"></script>
<script src="${pageContext.request.contextPath}/seabox_js/seabox_public/seabox_menu.js"></script>




<div id="first_line" class="first_line">
	<%--LOGO位--%>
	<div class="logo_div">
		<img  src="${pageContext.request.contextPath}/images/main/logo-white.jpg">
	</div>

	<%--title位 与logo在同一行--%>
	<div id="help_div" class="help_div">
		<ol class="breadcrumb" >
			<%--<li ><font>消息</font></li>--%>
			<%--<li ><font >帮助</font></li>--%>
			<li ><font >欢迎：<font style="color:#6BA3F2;">Alex</font></font></li>
			<li ><font class="logout_btn" id="logout_btn">退出</font></li>
		</ol>

	</div>
</div>

<%--菜单 位于界面第二行--%>
<nav id="menu_nav" class="navbar navbar-default menu_nav"  role="navigation">
	<div class="container-fluid-seabox">
		<!-- Brand and toggle get grouped for better mobile display -->

		<!-- Collect the nav links, forms, and other content for toggling -->
		<div class="collapse navbar-collapse" id="menu-nav-div">
			<ul id="menu-ul" class="nav navbar-nav ">
                    <%--<input type="hidden" id="menu_list" value="${menuList}"/>--%>
					<c:forEach items="${menuList}" var="menu" varStatus="status">
						<c:if test="${menu.menu_level==1 }">
							<li style="text-align: center;border:0px solid red;" class="dropdown hover-color">
								<a href="#" class="dropdown-toggle active" data-toggle="dropdown">
									<font class="menu_font"><c:out value="${menu.menu_ch_name }"/></font>
								</a>
								<ul class="dropdown-menu" role="menu">
									<c:set var="cnt_nmb" value="1"/>
									<c:forEach items="${menuList }" var="submenu">
										<c:if test="${submenu.parent_menu_id==menu.menu_id }">
											<c:if test="${cnt_nmb!=1 }">
												<li class="divider"></li>
											</c:if>

											<li>
												<a href="#" class="menu_font" onclick="refreshMainPage(this)"
												   action_url="${pageContext.request.contextPath}${submenu.menu_url}">
													<c:out value="${submenu.menu_ch_name }"/>
												</a>
											</li>
											<c:set var="cnt_nmb" value="${cnt_nmb+1}"/>
										</c:if>
									</c:forEach>
								</ul>
							</li>
						</c:if>
					</c:forEach>



                 <%--<li style="text-align: center;border:0px solid red;" class="dropdown hover-color">--%>
                    <%--<a href="#" class="dropdown-toggle active" data-toggle="dropdown">--%>
						<%--<font class="menu_font">标签工厂</font>--%>
					<%--</a>--%>
					<%--<ul class="dropdown-menu" role="menu">--%>
						<%--<li>--%>
							<%--<a href="#" class="menu_font" onclick="refreshMainPage(this)"--%>
							   <%--action_url="${pageContext.request.contextPath}/usertags/tag/main.do">--%>
								<%--标签市场--%>
							<%--</a>--%>

						<%--</li>--%>
						<%--<li class="divider"></li>--%>
						<%--<li >--%>
							<%--<a href="#" class="menu_font">--%>
								<%--标签创建--%>
							<%--</a>--%>
						<%--</li>--%>
						<%--<li class="divider"></li>--%>
						<%--<li ><a href="#">--%>
							<%--<a href="#" class="menu_font">--%>
								<%--标签规则--%>
							<%--</a></li>--%>

					<%--</ul>--%>
				<%--</li>--%>
				<%--<li class="hover-color" style="text-align: center;border:0px solid red;"><a href="#">--%>
					<%--<font class="menu_font">标签市场</font>--%>
				<%--</a></li>--%>
				<%--<li class="hover-color" style="text-align: center;border:0px solid red;"><a href="#">--%>
					<%--<font class="menu_font">用户管理</font>--%>
				<%--</a></li>--%>
				<%--<li style="text-align: center;border:0px solid red;" class="dropdown hover-color">--%>
					<%--<a href="#" class="dropdown-toggle" data-toggle="dropdown">--%>
						<%--<font class="menu_font">用户群管理</font>--%>
					<%--</a>--%>
					<%--<ul class="dropdown-menu" role="menu">--%>
						<%--<li>--%>
						<%--<li ><a href="#">--%>
							<%--<a href="#" class="menu_font" onclick="refreshMainPage(this)"--%>
							   <%--action_url="${pageContext.request.contextPath}/screen/sum/lstSnCdion.do">--%>
								<%--用户群筛选--%>
							<%--</a>--%>
						<%--</li>--%>
						<%--<li class="divider"></li>--%>
						<%--<li ><a href="#">--%>
							<%--<a href="#" class="menu_font" onclick="refreshMainPage(this)"--%>
							   <%--action_url="${pageContext.request.contextPath}/person/microPt/microPtMain.do">--%>
								<%--群内微观画像--%>
							<%--</a></li>--%>
						<%--<li class="divider"></li>--%>
						<%--<li ><a href="#"><a href="#" class="menu_font" onclick="refreshMainPage(this)"--%>
								<%--action_url="${pageContext.request.contextPath}/person/macroPt/macroPtMain.do">--%>
								<%--群内宏观画像--%>
						<%--</a></li>--%>

					<%--</ul>--%>

				<%--</li>--%>


                        <li id="person_center" class="person_center" class="dropdown hover-color">
                            <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                                <span class="glyphicon glyphicon-user"> </span>
                                个人中心
                                <span class="glyphicon glyphicon-circle-arrow-down"></span>

                            </a>
                            <ul id="per_cent_ul" style="width:10px;" class="dropdown-menu" role="menu">
                                <li style="width:10px;">
                                    <a href="#"  class="menu_font" onclick="refreshMainPage(this)"
                                       action_url="${pageContext.request.contextPath}/sys/goModifyPassword.do">
                                        <c:out value="修改密码"/>
                                    </a>
                                </li>
                            </ul>
                        </li>

			</ul>

			<%--<ul id="menu-ul" class="nav navbar-nav">--%>

			<%--</ul>--%>

			<form role="search">
			</form>

		</div><!-- /.navbar-collapse -->
	</div><!-- /.container-fluid -->


</nav>
<%--菜单结束--%>

<%--显示内容区域--%>
<div class="main_page_div">
	<iframe id="mainPageIfame" frameborder="0" class="main_page_iframe">

	</iframe>


</div>

</body>
</html>
