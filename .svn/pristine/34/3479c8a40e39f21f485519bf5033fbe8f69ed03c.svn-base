<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.util.ResourceBundle"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link href="${localProjectName}/utils/bootstrap/css/bootstrap-combobox.css?v=${revision}" rel="stylesheet" media="screen">
	<link href="${localProjectName}/utils/bootstrap/css/bootstrap-datetimepicker.css?v=${revision}" rel="stylesheet" media="screen">
	<script type="text/javascript" src="${localProjectName}/utils/bootstrap/js/bootstrap-combobox.js?v=${revision}" charset="UTF-8"></script>
	<script type="text/javascript" src="${localProjectName}/utils/bootstrap/js/bootstrap-datetimepicker.js?v=${revision}" charset="UTF-8"></script>
	<script type="text/javascript" src="${localProjectName}/utils/bootstrap/js/locales/bootstrap-datetimepicker.zh-CN.js?v=${revision}" charset="UTF-8"></script>
	<script type="text/javascript" src="${localProjectName}/utils/bootstrap/js/bootstrap-multiselect.js?v=${revision}" charset="UTF-8"></script>
	<script type="text/javascript" src="${localProjectName}/pageJs/base/evergrande_css.js?v=${revision}" charset="UTF-8"></script>
	<script src="${localProjectName}/pageJs/checkBox/checkBox.js?v=${revision}"></script>
	<title></title>
	<script>
		$(function() {
			/** 
			 *初始化日期控件
			 */
			var dateInputs = $('.dateInput');
			var dd = new Date();

		    dd.setDate(dd.getDate()+1);//获取AddDayCount天后的日期
		
		    var y = dd.getFullYear();
		
		    var strMonth = dd.getMonth()+1;//获取当前月份的日期
		
		    var strDay = dd.getDay();
		
			if(strMonth<10)     
			{     
				strMonth="0"+strMonth;     
			}   
			if(strDay<10)     
			{     
				strDay="0"+strDay;     
			}   
		
		    var tommrow = y+"-"+strMonth+"-"+strDay;
			for (var i = 0; i < dateInputs.length; i++) {
				var dateInputId = dateInputs[i].id;
				$(dateInputs).attr("data-date",tommrow);
				$(dateInputs).datetimepicker({
					format : 'yyyy-mm-dd',
					language : 'zh-CN',
					startDate:tommrow,
					weekStart : 1,
					todayBtn : 1,
					autoclose : 1,
					startView : 2,
					minView : 2,
					forceParse : 0,
					pickerPosition : "bottom-right"
				});
	
			}
		});
		setTimeout(function() {
			initCheckBox();
		}, 200); //因为部分checkbox是在JS请求AJAX数据之后才初始化完成的，所以checkbox绑定CLICK事件时需要延时执行。
	</script>
	</head>
	<body>
	</body>
</html>
