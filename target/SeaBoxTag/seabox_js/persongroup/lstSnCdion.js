/**
 * create by SongChaoqun
 */

$(function(){

    $("#crt_screening").click(function(){
        newPageByPost(null,"/screen/sum/crtSnCdion.do");
    });

    showPageForSplit("/screen/sum/listMainData.do",listScreeningData,1,8);

    // function showPageForSplit(url,callbackFunction,currPage,pageSize){


});

/**
 * @param resultData
 */
function listScreeningData(resultData){
    // alert(JSON.stringify(resultData));
    var dataList = resultData.list;

    var tbodyBorder = $(".tbody_boder");
    tbodyBorder.empty();
    $.each(dataList,function(i,screeningData){
        var screeningId = screeningData.screeningId;
        var screeningNm = screeningData.screeningNm;
        var screeningDes = screeningData.screeningDes;
        var userNm = screeningData.userNm;
        var statsNm = screeningData.statsNm;
        var createTs = screeningData.createTs;
        var updateTs = screeningData.updateTs;
        var tr = $("<tr></tr>");
        tr.append("<td>"+screeningId+"</td>");
        tr.append("<td>"+screeningNm+"</td>");
        tr.append("<td>"+statsNm+"</td>");
        tr.append("<td>"+userNm+"</td>");
        tr.append("<td>"+createTs+"</td>");
        var optionTd = $("<td class='option_td'></td>");

        var viewHref = $('<label>查看</label>');
        viewHref.attr("screeningId",screeningId);
        viewHref.click(function(){
            viewScreening(this);
        });

        var editHref = $('<label>编辑</label>');
        editHref.attr("screeningId",screeningId);
        editHref.click(function(){
            editScreening(this);
        });


        var delHref = $('<label>删除</label>');
        delHref.attr("screeningId",screeningId);
        delHref.click(function(){
            delScreening(this);
        });

        optionTd.append(viewHref);
        optionTd.append("|");
        optionTd.append(editHref);
        optionTd.append("|");
        optionTd.append(delHref);
        tr.append(optionTd);
        // tr.append("<td>编辑|删除</td>");
        tbodyBorder.append(tr);
    });
}

function editScreening(editObj) {
    // alert($(editObj).attr("screeningId"));
    var param = new Array();

    param[0]= {id:"screeningId",value:$(editObj).attr("screeningId")};
    newPageByPost(param,"/screen/sum/editSnCdion.do");
}

function delScreening(editObj) {
    var param = {"screeningId":$(editObj).attr("screeningId")};

    submitJsonStr2Controller("/screen/sum/delScreeningData.do",saveBackFunction,param);

    function saveBackFunction(){
        showPageForSplit("/screen/sum/listMainData.do",listScreeningData,1,8);
    }
}

function viewScreening(editObj) {
    var param = new Array();

    param[0]= {id:"screeningId",value:$(editObj).attr("screeningId")};
    newPageByPost(param,"/screen/sum/viewSnCdion.do");
}



