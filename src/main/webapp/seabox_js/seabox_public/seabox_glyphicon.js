/**
 * create by SongChaoqun
 * 2016-11-22
 */

$(function(){
    initGlyphicon_search();
});

/**
 * 检索图标
 */
function initGlyphicon_search(){
    var allSearch = $(".seabox_glyphicon_search");

    $.each(allSearch,function(i,searchObj){
        var canvasWidth = $(searchObj).width();
        var canvasHeight = $(searchObj).height();
        if(canvasWidth==0)
            canvasWidth = 30;
        if(canvasHeight==0)
            canvasHeight = 30;
        var canvansCenterX = canvasWidth/2.4;//canvas对象的中心位置X坐标
        var canvansCenterY = canvasHeight/2.4;//canvas对象的中心位置Y坐标

        //当前标签下增加CANVAS
        var newCanvasObj = $("<canvas class='' ></canvas>");

        newCanvasObj.attr("width",canvasWidth);
        newCanvasObj.attr("height",canvasHeight);
        newCanvasObj.attr("class","seabox_glyphicon seabox_glyphicon_search");

        $(searchObj).append(newCanvasObj);
        // alert("aaa");
        var searchCanvas = newCanvasObj[0];

        var ctx=searchCanvas.getContext("2d");
        ctx.lineWidth = 2;
        ctx.strokeStyle = 'white';

        ctx.beginPath();
        // ctx.arc(圆心X坐标,圆心Y坐标,圆半径,0,2*Math.PI);\
        var arcR = canvansCenterX*2/3;

        ctx.arc(canvansCenterX,canvansCenterY,arcR,0,2*Math.PI);

        var hudu = (2*Math.PI / 360) * 135;
        var X = canvansCenterX + Math.sin(hudu) * arcR;
        var Y = canvansCenterY - Math.cos(hudu) * arcR;
        var X1 = canvansCenterX + Math.sin(hudu) * canvasWidth/2*1.1;
        var Y2 = canvansCenterY - Math.cos(hudu) * canvasWidth/2*1.1;

        ctx.moveTo(X,Y);
        ctx.lineTo(X1,Y2);
        ctx.stroke();

    });
}
