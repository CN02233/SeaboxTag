
$(document).ready(function(){
    makeSeaBoxMenuTree();
});

/**
 * 因为菜单数据通常需要调用后台请求得到数据后才初始化
 * 而如果采用$(document).ready的方式会导致 请求完成菜单数据后没有渲染成HTML就已经运行了 $(".menu_tree_div,.menu_tree_child").click方法
 * 此时HTML没有渲染完成,就会造成click时间绑定失效。所以，此处采用手工调用方式，也就是渲染完成Menu的HTML后手动调用makeSeaBoxMenuTree进行菜单的时间绑定动作
 */
function makeSeaBoxMenuTree(){
    // alert("running");
    $(".menu_tree_div,.menu_tree_child").click(function(){
        // alert("here is click");
        var allChildUlObj = $(this).next("ul");

        var nowClickClass = $(this).attr("class");
        // alert("SS");
        if(nowClickClass.indexOf("menu_tree_first")>0&&nowClickClass.indexOf("first_shadowm")>0){
            // alert("YY");
            $(this).attr("class",nowClickClass.replace(" first_shadowm",""));
            $(this).css("margin-bottom","0px");
        }else if(nowClickClass.indexOf("menu_tree_first")>0&&nowClickClass.indexOf("first_shadowm")<0){
            $(this).attr("class",nowClickClass+" first_shadowm");
            $(this).css("margin-bottom","5px");

            // alert("NN");

        }

        $.each(allChildUlObj,function(e,childUlObj){
            $(childUlObj).slideToggle();
            // $(childUlObj).fadeToggle();
            // $(childUlObj).toggle();
        });
    });
}

