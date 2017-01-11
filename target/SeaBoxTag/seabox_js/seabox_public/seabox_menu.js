/**
 * create by SongChaoqun
 * 2016 11 11
 */

/**
 * 点击菜单后刷新显示区域的frame页面
 */
function refreshMainPage(clickObj){
    var action_url = $(clickObj).attr("action_url");
    // alert(action_url);
    $(".main_page_iframe").attr("src",action_url);

}