/**
 * create by SongChaoqun
 * 2016 11 11
 */

/**
 * 页面加载后 根据页面的相关设置调整一些组件的参数
 */
$(document).ready(function() {
    init_large();
    menu_hover();
    logout_click();
});

/**
 * 设置相关内容的高度和宽度
 */
function init_large(){
    $(".navbar-collapse").css("padding-left","0");
    $(".navbar-collapse").css("padding-right","0");
    var allMenuObj = $("#menu-ul").children("li[id!='person_center']");
    var allLiLength = allMenuObj.length;
    //减180是为了去掉"个人中心的宽度"。个人中心实际宽度是160px,但是如果这个减去的是160px的话，会导致在firefox浏览器下按alt键后个人中心换行到下一行
    //所以为了显示避免出现这个BUG，在这里减去180px
    var menuNavDivWid = $("#menu-nav-div").width()-180;
    var echLiWid = menuNavDivWid/allLiLength;

    if(allLiLength<4&&echLiWid>300){
        var newAllWid = 300*allLiLength;
        if((menuNavDivWid-newAllWid)<300){

        }else{
            echLiWid =300;
            // alert(menuNavDivWid-newAllWid);
            $("#person_center").css("margin-left",menuNavDivWid-newAllWid);
        }
    }

    //设置菜单的宽度
    allMenuObj.css("width",echLiWid);
    //设置菜单展开后的2级菜单宽度
    var dropDownMenuObjLst = $(".dropdown-menu[id!='per_cent_ul']");
    dropDownMenuObjLst.css("width",echLiWid);

    $("#menu_nav").css("margin-bottom","0");

    var pageHeight = $(window).height();
    var first_line_height = $("#help_div").height();
    var menu_nav_height = $("#menu_nav").height();

    var main_iframe_height = pageHeight-first_line_height-menu_nav_height-10;

    $(".main_page_iframe").css("height",main_iframe_height+"px");
}

/**
 * 鼠标在菜单上悬停时修改菜单文字的颜色为白色
 */
function menu_hover(){
    $(".hover-color").mouseover(function(){
        // alert("get");
        $(this).find("font").css("color","white");
    });

    $(".hover-color").mouseout(function(){
        // alert("get");
        $(this).find("font").css("color","black");
    });
}

function logout_click(){
    $("#logout_btn").click(function () {
        newPageByPost(null,"/sys/logout.do");
    });
}