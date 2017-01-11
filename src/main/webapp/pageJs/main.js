window.onload = function() {
	// 通过当前页面计算内容DIV的高度
	var pageHeight = $(document).height();// 整个BODY高度
	var logoDivHeight = $("#logoDiv").height();
	// var logoDivHeight = $("#logoDiv").height;
	var bodyHeadingDivHeight = 30;
	// alert(pageHeight+"|"+logoDivHeight+"|"+bodyHeadingDivHeight);
	// $("#mainBodyDiv").css("height",pageHeight-logoDivHeight-bodyHeadingDivHeight);
	var titleDiv = $("#titleDiv").height();
	$("#titleDiv").css("line-height", titleDiv + "px");
	$('#main-nav').on('click', 'li', function() {
		$(this).addClass('active').siblings().removeClass('active');
	});
	var picHeight = pageHeight - 842;// 页面可显示区域大于842的时候背景显示图片,系统显示区域最高842
	if (picHeight > 0)
		$("#rootDiv").css("padding-top", picHeight / 2);

}

function onclickSubMenu(sub_menu_id) {
	$("#" + sub_menu_id).parents().siblings().children().find("li").removeClass('active');
	$("#" + sub_menu_id).addClass('active').siblings().removeClass('active');
}

function openPageByUrl(url) {
	var realUrl = postUrl + url;
	$("#mainPageIfame").attr("src", realUrl);
}

/**
 * 
 * 点击一级菜单,隐藏一级菜单,将一级菜单加到二级菜单中显示
 */
function firstMenuClick(secId, name, logoUrl) {
	var beforeStr = "<li id=\"" + secId + "F\">" + 
						"<a href=\"#\" class=\"nav-header collapsed\" onclick=\"showFirstMenu('" + secId + "');\" data-toggle=\"collapse\">" + 
							"<label class=\"" + logoUrl + "\"></label>" +
							"<i>" + name + "</i>" + 
							"<span style=\"float:right\">" +
								"<img src=\"" + baseHostUrl + "/images/menuUp.png\">" +
							"</span>" + 
						"</a>" + 
					"</li>";
	var clickObj = $("#" + secId);
	var clickChildren = $("#" + secId + " li");
	$("#" + secId).find("li").remove();
	for (var i = 0; i < clickChildren.length + 1; i++) {
		if (i == 0) {
			clickObj.append(beforeStr);
		} else {
			clickObj.append(clickChildren[i - 1]);
		}
	}
	$("#" + secId + "A").hide();
}

/**
 * 点击已安置在二级菜单中的一级菜单名,将实际一级菜单显示,并将在二级中显示的一级菜单删除
 */
function showFirstMenu(frtId) {
	$("#" + frtId + "F").remove();
	var aObj = $("#" + frtId + "A");
	aObj.show();
	$('#' + frtId).collapse('hide');
	$("#" + frtId + "LI").attr("class", "");
}