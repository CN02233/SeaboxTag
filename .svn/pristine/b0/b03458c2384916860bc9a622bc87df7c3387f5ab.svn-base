//获取主机地址，如： http://localhost:8083
var curWwwPath = window.document.location.href;
// 获取主机地址之后的目录，如： uimcardprj/share/meun.jsp
var pathName = window.document.location.pathname;
var pos = curWwwPath.indexOf(pathName);
// 获取主机地址，如： http://localhost:8083
var localhostPaht = curWwwPath.substring(0, pos);
var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
var postUrl = localhostPaht + projectName;
//获取主机地址结束

//定义ECHARTS模块路径start
require.config({
	paths : {
		echarts : '../../echarts/js'
	}
});
//定义ECHARTS模块路径end