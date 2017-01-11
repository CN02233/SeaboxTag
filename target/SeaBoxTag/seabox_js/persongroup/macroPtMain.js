
function viewMacDetail(thisObj){
    // alert("aaa");
    var screeningId = $(thisObj).attr("screeningId");

    var param = new Array();

    param[0]= {id:"screeningId",value:screeningId};

    newPageByPost(param,"/person/macroPt/macroPtDetail.do");
}

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
            viewMacDetail(this);
        });

        optionTd.append(viewHref);
        tr.append(optionTd);
        // tr.append("<td>编辑|删除</td>");
        tbodyBorder.append(tr);
    });
}