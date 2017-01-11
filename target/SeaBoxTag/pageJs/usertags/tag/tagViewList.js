var currPage=1;

var  tagChartDataStore=new Array();

$(document).ready(function() {

});

function showCoverageDialog() {
    window.open("loading Urling....");
}


function  addTagsCoverageForLastLevelCategoryToDataStore (lastLevelTagCategory, dsIndex){

    if(null == lastLevelTagCategory) {
        return;
    }

    var have_tag_ind = lastLevelTagCategory.have_tag_ind;
    var childTags = lastLevelTagCategory.children;

    var miniChartDatas = new Array();
    var bigChartDatas = new Array();


    if(have_tag_ind && childTags!=null && childTags.length>0) {

        for(var t=0; t<childTags.length; ++t) {

            miniChartDatas[t] = childTags[t].covered_count ;
            bigChartDatas[t] = new Object();
            if(childTags[t].covered_count== null) {
                bigChartDatas[t].value = 0; // use Fake Zero for this Tag.
                bigChartDatas[t].name  = childTags[t].tag_nm + "  : " + "暂无数据";
            } else {
                bigChartDatas[t].value = childTags[t].covered_count ;
                bigChartDatas[t].name  = childTags[t].tag_nm + "  : " + childTags[t].covered_count + "  (" + childTags[t].covered_rate + "%)";
            }


            bigChartDatas[t].short_name  = childTags[t].tag_nm ;
        }
    }
    tagChartDataStore[dsIndex] = new Object();
    tagChartDataStore[dsIndex].mini_data = miniChartDatas;
    tagChartDataStore[dsIndex].series_data =   bigChartDatas; // keep dataStore

}


/**
 * 获取用户列表数据
 */
function listData(pageDataObj) {
    var tbody = $("#fill-list-data-async");
    var webAppRoot = "/" + window.location.pathname.substr(1).split('/')[0];
    currPage= pageDataObj.currPage;//当前页号

    $("#currPage").val(currPage);
    var pageSize= pageDataObj.pageSize;//当前页面显示条数
    var totalNum= pageDataObj.totalNum;//数据总数
    var dataList= pageDataObj.list;//当前页面元素集
    tbody.empty();
    if (dataList != null && dataList.length > 0) {
    } else {
        return;
    }
    for (var i = 0; i < dataList.length; i++) {
        //标签名	描述	有效期	覆盖率	操作
        var tag_ctgy_id = dataList[i].tag_ctgy_id;
        var tag_ctgy_nm = dataList[i].tag_ctgy_nm;
        var tag_desc = dataList[i].tag_desc;
        var valid_date = (dataList[i].disabled_dt != null) ? dataList[i].disabled_dt : "";
        if(valid_date == '') {
            valid_date = "长期";
        }
        var covered_rate = dataList[i].covered_rate;
        if(covered_rate==null ||  covered_rate == "null") {
            covered_rate = "暂无数据";
        } else {
            covered_rate = covered_rate.toFixed(2);
        }


        var favorite_by_user = dataList[i].favorite_by_user;

        var childTags = dataList[i].children;
        var have_tag_ind = dataList[i].have_tag_ind;



        var trObj = $("<tr id='tag_row_idx_" + i + "'></tr>");  // keep the id

        if (i % 2 != 0) {
            trObj.attr("class", "active");
        }

        addTagsCoverageForLastLevelCategoryToDataStore( dataList[i] , i);

        trObj.append("<td hidden='hidden' class='td_col_0_tag_id_hide'>" + tag_ctgy_id + "</td>");

        trObj.append("<td class='tbody-td td_col_A_tag_name' style='text-align:left;padding-left:40px;'>" + tag_ctgy_nm + "</td>");
        trObj.append("<td class='tbody-td td_col_B_tag_desc' style='text-align:center;'>" + tag_desc + "</td>");
        trObj.append("<td class='tbody-td td_col_C_tag_valid' style='text-align:center;'>" + valid_date + "</td>");
        trObj.append("<td class='tbody-td td_col_D_tag_cover' style='text-align:right;padding-right:35px;'>" + covered_rate + "</td>");

        var td_tag_op = $("<td class='tbody-td td_col_E_tag_op'  style='padding-left: 35px;'></td>");
        var div_ops_all = $("<div align='center' valign='middle' style='float: left; width:100%'></div>");
        td_tag_op.append(div_ops_all);

        var div_cover = $("<div class='div_tag_cover_mini_chart' ></div>" );
        div_cover.attr("id", "mini_cover_view_" + i);
        div_cover.attr("onclick", 'createModalCoverageChart(' + i + ',"' + tag_ctgy_nm + '","' + tag_ctgy_nm + '")');

        div_ops_all.append(div_cover);
        div_cover.append("<label class='sprite-img-first tag-icon-coverage'></label>");

        var event_func_name;
        var icon_label;

        var div_favo = $("<div valign='middle' class='div_tag_favo_icon' ></div>");
        div_ops_all.append(div_favo);

        if(favorite_by_user == true) {
            event_func_name = "clickRemoveFavoriteTag";
            icon_label = $("<label class='sprite-img-first tag-icon-favo-yes'></label>");
        } else {
            event_func_name = "clickAddFavoriteTag";
            icon_label = $("<label class='sprite-img-first tag-icon-favo-no'></label>");
        }

        div_favo.append( icon_label );
        icon_label.attr("onclick", event_func_name + "(" + i + "," + tag_ctgy_id + ', "' + tag_ctgy_nm + '")');

        trObj.append(td_tag_op);

        tbody.append(trObj);

    }

}




function orderList(orderColumn){

    update_Table_With_orderByColumn_Icon(orderColumn);

    var currPage = 1;
    refreshByLoadingData(currPage);

}


function clickAddFavoriteTag(tag_row_idx, tag_ctgy_id, tag_ctgy_nm){
    var addPara = new Object();
    addPara.tag_row_idx = tag_row_idx;
    addPara.tag_ctgy_id = tag_ctgy_id;
    addPara.tag_ctgy_nm = tag_ctgy_nm;
    doAddFavoriteTag(addPara);
}

function clickRemoveFavoriteTag(tag_row_idx, tag_ctgy_id, tag_ctgy_nm){
    var removePara = new Object();
    removePara.tag_row_idx = tag_row_idx;
    removePara.tag_ctgy_id = tag_ctgy_id;
    removePara.tag_ctgy_nm = tag_ctgy_nm;
    if(needConfirmRemoveFavor == true) {
        createModalAlter('确定要将 "'+ tag_ctgy_nm + '" 移出收藏夹吗？','取消','确认',doRemoveFavoriteTag, removePara);
    } else {
        doRemoveFavoriteTag (removePara);
    }

}

function doRemoveFavoriteTag( removePara ){
    var tag_ctgy_id = removePara.tag_ctgy_id;
    jQuery.ajax({
        type : 'DELETE',
        contentType : 'application/json',
        url : postUrl + '/usertags/tag/favors/remove/' + tag_ctgy_id +'.do',
        data : '',
        //dataType : 'json',
        success : function(data) {
            //window.location.reload();
            var currPage = $("#currPage").val();
            refreshByLoadingData(currPage);
        },
        error : function(data) {
            alert("移出收藏夹失败");
        }
    });
}


function  doAddFavoriteTag( addPara ){
    var tag_ctgy_id = addPara.tag_ctgy_id;
    jQuery.ajax({
        type : 'POST',
        contentType : 'application/json',
        url : postUrl + '/usertags/tag/favors/include/' + tag_ctgy_id +'.do',
        data : '',
        //dataType : 'json',
        success : function(data) {
            //window.location.reload();
            var currPage = $("#currPage").val();
            refreshByLoadingData(currPage);
        },
        error : function(data) {
            alert("添加到收藏夹失败");
        }
    });
}






