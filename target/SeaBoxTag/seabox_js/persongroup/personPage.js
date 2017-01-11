

function listPerson(){
    var screeningId = $("#screeningId").val();
    var url = "/person/macroPtDetail/"+screeningId+".do";
    showPageForSplit(url,initTable,1,8);
}

function initTable(dataFromDb){
    var dataList = dataFromDb.list;
    // alert(JSON.stringify(dataList));
    var tbodyBorder = $(".tbody_boder");
    tbodyBorder.empty();
    $.each(dataList,function(i,data){
        var userNm = data.userNm;
        var userSex = data.userSex;
        var hasMarried = data.hasMarried;
        var userAge = data.userAge;
        var userGuid = data.userGuid;

        var tr = $("<tr></tr>");
        tr.append("<td>"+userNm+"</td>");
        tr.append("<td>"+userSex+"</td>");
        tr.append("<td>"+userAge+"</td>");
        tr.append("<td>汉族</td>");
        tr.append("<td>"+hasMarried+"</td>");
        var optionTd = $("<td class='option_td'></td>");
        var viewHref = $('<label>查看</label>');
        viewHref.attr("userGuid", userGuid);
        viewHref.attr("userNm", userNm);
        viewHref.click(function () {
            viewPersonDetail(this);
        });
        optionTd.append(viewHref);
        tr.append(optionTd);
        // tr.append("<td>编辑|删除</td>");
        tbodyBorder.append(tr);
    });

}

function viewPersonDetail(clickObj){
    // createModalAlter('密码已成功修改', '取消', '确认');
    // $("#templateModal").modal({backdrop:false,show:true});
    var userGuid = $(clickObj).attr("userGuid");
    var userNm = $(clickObj).attr("userNm");
    var sendData = {"userGuid":userGuid};

    submitData2Controller("/person/macroPtDetail/listAllTag.do",callBackFunction,sendData);

    function callBackFunction(data){
        // alert(JSON.stringify(data));

        createUserTagModal(data,userNm);

        $("#myModal").modal({backdrop:false,show:true});
    }
}

function createUserTagModal(dataList,userNm){
    $("#myModalLabel").empty();
    $("#myModalLabel").html(userNm+"--所属人群:"+$("#screeningNm").val());

    var tagPlace = $("#user_tag_tbl");
    tagPlace.empty();
    // alert(JSON.stringify(dataList));
    $.each(dataList,function(i,data){
        var tagId = data.tagId;
        var tagCtgyId = data.tagCtgyId;
        var tagNm = data.tagNm;
        var tagCtgyNm = data.tagCtgyNm;

        if($("#tr_"+tagCtgyId).length>0){
            var tagUl = $.find("ul[id='"+tagId+"']");
            $(tagUl).append("<li class='previous' ><span  href='#'>"+tagNm+"</span></li>");
        }else{
            var trObj =$("<tr id='tr_"+tagCtgyId+"' style='border-bottom:1px solid #AFB0E3;'></tr>");
            trObj.append("<td style='width:30%;'><nav><ul class='pager'><li class='previous' ><span style='border:0px' href='#'>"
                +tagCtgyNm+"</span></li></ul></nav></td>");
            trObj.append("<td><nav><ul id='"+tagCtgyId+"' class='pager'>"+
                "<li class='previous' ><span  href='#'>"+tagNm+"</span></li></ul></nav></td>");
            tagPlace.append(trObj);
        }
    });

// <tr style="border-bottom:1px solid #AFB0E3;">
}