/**
 * create by SongChaoqun
 */

var hasCheckRadioIdTmp = new Array();
var newGrpSeqTmp = 1;

$(function(){
    var paramArray = new Array();
    paramArray.push({id:"screeningId",value:$("#screeningId").val()});

    initDataFunction('/screen/sum/listTag.do',null,initTagTree);

    initDataFunction('/screen/sum/getScreeningAndTags.do',paramArray,initScreeningAndTags);

    var tag_grp_all = $(".tag_grp");
    $.each(tag_grp_all,function(i,tag_grp){
        $(tag_grp).prev().css("padding-top",($(tag_grp).height()-17)/2);
    });

    clickListener();
});


function clickListener(){
    $("#add_stg_grp").click(function(){
        if($(".allow_edit").attr("class").indexOf("hidden_init")>0){
            $(".allow_edit").attr("class",$(".allow_edit").attr("class").replace("hidden_init",""));
            return ;
        }
        addTagGrp("new_grp_"+newGrpSeqTmp);
        newGrpSeqTmp++;
    });

    $("#save_btn").click(function(){
        saveSnCdion(this);
    });
}

function initScreeningAndTags(jsonData){
    var screeningObj = jsonData.screeningObj;
    var tagsObj = jsonData.tagsObj;

    $("#screeningNm").val(screeningObj.screeningNm);
    $.each(tagsObj,function(i,tagGrpObj){
        var grpSeq = tagGrpObj.grpSeq;
        $(".allow_edit").attr("id","grp_"+grpSeq);
        var tagList = tagGrpObj.tagList;
        $.each(tagList,function(i,tagObj){
            var tagId = tagObj.tagId;
            var tagNm = tagObj.tagNm;
            var tagCtgyId = tagObj.tagCtgyId;
            var tagCtgyNm = tagObj.tagCtgyNm;
            buildTagGrp(tagId,tagNm,tagCtgyId,tagCtgyNm);
        });

        addTagGrp();
    });

    $(".allow_edit").attr("class","allow_edit hidden_init");
    $(".allow_edit").attr("id","new_grp_"+newGrpSeqTmp);
    newGrpSeqTmp++;
}

function initTagTree(jsonData){
    var tagMarket = jsonData.tagMarket;

    // var rootTagNode = jsonData["root"]
    makeTagTree(tagMarket);
    makeSeaBoxMenuTree();

    initTagRadio();
}

/**
 * 初始化标签树：

 <div class="menu_tree_div menu_tree_first">
 <span class="glyphicon glyphicon-th-list" ></span> 社会特征
 </div>
 <ul class="menu_tree_ul collapse" >
 <li >
 <div class="menu_tree_child have_bottom">
 <div class="second_tree_before"></div>
 工作特征
 <label  class="sprite-img-six second_tree_after"></label>
 </div>
 <ul class="menu_tree_ul collapse">
 <li >
 <div class="menu_tree_child trd_tree_div">
 <span></span> 工作状态
 </div>
 <ul class="menu_tree_ul collapse tag_info">
 <li><input type="radio" name="iCheck1" />
 <span >在职</span> </li>
 <li><input type="radio" name="iCheck2"/>
 <span >兼职</span> </li>
 <li><input type="radio" name="iCheck3"/>
 <span >待业</span></li>
 </ul>
 </li>
 </ul>
 </li>
 </ul>

 * @param rootTagNodes
 */
function makeTagTree(tagMarket){

    var menu_area = $("#menu_area");
    menu_area.empty();

    //逻辑为:先取出跟节点的标签类别,之后向叶子节点取,初始化整个标签树
    var rootTagNodes = tagMarket.rootTag;
    $.each(rootTagNodes,function(i,tagNode){
        var rootNodeDiv =  $("<div></div>");
        rootNodeDiv.attr("class","menu_tree_div menu_tree_first");
        var rootNodeSpan = $("<span></span>");
        rootNodeSpan.attr("class","glyphicon glyphicon-th-list");

        rootNodeSpan.append(" "+tagNode.tagNm);
        rootNodeDiv.append(rootNodeSpan);
        menu_area.append(rootNodeDiv);

        makeSecondNode4Tree(tagNode.tagId,tagMarket);

    });

    // alert(menu_area.html());
}

function makeSecondNode4Tree(rootTagId,tagNodes){
    var menu_area = $("#menu_area");
    var sonNodes = tagNodes[rootTagId];
    // alert(JSON.stringify(sonNodes));
    var secondAreaUl = $("<ul class='menu_tree_ul collapse'></ul>");

    if(sonNodes!=null){
        $.each(sonNodes,function(i,secondNode){
            var tagNm = secondNode.tagNm;

            var secondAreaLi = $("<li ></li>");
            var secondAreaDiv = $("<div class='menu_tree_child have_bottom'></div>");
            secondAreaDiv.append("<div class='second_tree_before'></div>"+tagNm+"<label  class='sprite-img-six second_tree_after'></label>");
            secondAreaLi.append(secondAreaDiv);
            makeThrNode4Tree(secondNode.tagId,tagNodes,secondAreaLi);
            secondAreaUl.append(secondAreaLi);

            menu_area.append(secondAreaUl);


            // alert(secondArea.html());
        });
    }
}

function makeThrNode4Tree(parentTagId,tagNodes,secondAreaLi){
    var thrdNodes = tagNodes[parentTagId];
    if(thrdNodes!=null){
        var trdAreaUl = $("<ul class='menu_tree_ul collapse menu_trd'></ul>");
        $.each(thrdNodes,function(i,trdNode){
            var tagNm = trdNode.tagNm;

            var trdAreaLi = $("<li class='trd_li'></li>");
            var trdAreaDiv = $("<div class='menu_tree_child trd_tree_div'></div>");

            trdAreaDiv.append("<span></span>"+tagNm);
            trdAreaLi.append(trdAreaDiv);
            makeLstNode4Tree(trdNode.tagId,trdNode.tagNm,tagNodes,trdAreaLi);
            trdAreaUl.append(trdAreaLi);

            secondAreaLi.append(trdAreaUl);
        });
    }

}

function makeLstNode4Tree(parentTagId,parentTagNm,tagNodes,trdAreaLi){
    var lstNodes = tagNodes[parentTagId];
    // alert(JSON.stringify(lstNodes));
    if(lstNodes!=null){
        var lstAreaUl = $("<ul class='menu_tree_ul collapse'></ul>");
        // alert(JSON.stringify(lstNodes));
        $.each(lstNodes,function(i,trdNode){
            var tagNm = trdNode.tagNm;
            var tagId = trdNode.tagId;

            var lstAreaLi = $("<li ></li>");

            var radioObj = $("<input type='radio' class='icheck_radio'></input><span style='margin-left:5px;'>"+tagNm+"</span>");
            radioObj.attr("id",tagId);
            radioObj.attr("tagNm",tagNm);
            radioObj.attr("tagCtgyId",parentTagId);
            radioObj.attr("tagCtgyNm",parentTagNm);

            // lstAreaLi.append("<input type='radio' class='icheck_radio' id='"+tagId+"' tagNm='"+tagNm+"'/>"+tagNm+"<span></span>");
            lstAreaLi.append(radioObj);
            lstAreaUl.append(lstAreaLi);

            trdAreaLi.append(lstAreaUl);
        });
    }
}

/**
 * 因为标签数量很多,初始化树完成后一次性的对标签的radio进行CSS渲染非常耗时
 * 所以采用当点击上一级菜单的时候才进行渲染
 */
function initTagRadio(){
    $(".trd_tree_div").click(function(){
        var radioLst = $(this).next('ul').find('input[type=radio]');

        $(radioLst).iCheck({
            radioClass: 'iradio_square',
            increaseArea: '20%' // optional
        });
        $(radioLst).on('ifClicked', function(event){
            if(event.target.checked){
                $(this).iCheck('uncheck');
                removeTagFromGrp(this);//将页面右边的标签移除
            }else{
                addTagToGrp(this);//将选中的标签加入到页面右边
                show_grp_body();
                sumTagGrpHeight();
            }
        });
    });
}



function addTagToGrp(radioObj){
    if($(".allow_edit").attr("class").indexOf("hidden_init")>0){
        $(".allow_edit").attr("class",$(".allow_edit").attr("class").replace("hidden_init",""));
    }

    var tagId = $(radioObj).attr("id");
    hasCheckRadioIdTmp.push(tagId);//缓存已勾选的标签ID,用于释放勾选状态(所有的已勾选变为非勾选)

    var tagNm = $(radioObj).attr("tagNm");
    var tagCtgyId =  $(radioObj).attr("tagCtgyId");
    var tagCtgyNm = $(radioObj).attr("tagCtgyNm");
    buildTagGrp(tagId,tagNm,tagCtgyId,tagCtgyNm);
}

function buildTagGrp(tagId,tagNm,tagCtgyId,tagCtgyNm){
    var tagGrps = $(".allow_edit").find(".pager");
    var tagCtgyGrp = tagGrps.find("li[id='"+tagCtgyId+"']");
    if(tagCtgyGrp!=null&&tagCtgyGrp.length>0){
        tagCtgyGrp.children("span").append("<span id='"+tagId+"'>"+","+tagNm+"</span>");
    }else{
        var newTagCtgyGrp = $("<li id='"+tagCtgyId+"' class='previous' ><span>"+tagCtgyNm+"： "+"<span id='"+tagId+"'>"+tagNm+"</span>"+"</span></li>");
        tagGrps.append(newTagCtgyGrp);
        // tagGrps.find("li[id='rm_tg_grp_li']").before(newTagCtgyGrp);
    }
    sumTagGrpHeight();
}

function show_grp_body(){
    var disabledVal = $("#prompt_msg_body").css("disabled");
    if(disabledVal==null||disabledVal==''){//隐藏提示信息
        $("#prompt_msg_body").css("display","none");
        $.each($(".hidden_init"),function(i,hidden_obj){
            $(hidden_obj).attr("class",$(hidden_obj).attr("class").replace("hidden_init",""));
        });
        $(".btn_seabox").attr("class",$(".btn_seabox").attr("class").replace("disabled",""));
    }
}

function removeTagFromGrp(radioObj){
    var tagId = $(radioObj).attr("id");
    var tagNm = $(radioObj).attr("tagNm");
    var tagCtgyId =  $(radioObj).attr("tagCtgyId");
    var tagCtgyNm = $(radioObj).attr("tagCtgyNm");
    var tagGrps = $(".allow_edit").find(".pager");
    var tagCtgyGrp = tagGrps.find("li[id='"+tagCtgyId+"']");
    if(tagCtgyGrp!=null&&tagCtgyGrp.length>0){
        var tagObj = tagCtgyGrp.find("span[id='"+tagId+"']");
        if(tagObj!=null&&tagObj.length>0){
            tagObj.remove();
        }
        var tagCnt = tagCtgyGrp.children("span").find("span").length;
        if(tagCnt<1){
            tagCtgyGrp.remove();
        }
    }
    sumTagGrpHeight();
}

/**
 * 删除标签组
 */
function removeTagGrp(tag_grp_seq){
    // alert(tag_grp_seq);
    var rmObj = $("#"+tag_grp_seq);
    var rmObjClass = rmObj.attr("class");

    if(rmObjClass!=null&&rmObjClass=='allow_edit'){
        rmObj.find("ul[class='pager']").children("li[id!='rm_tg_grp_li']").remove();
    }else{
        rmObj.remove();
    }
    sumTagGrpHeight();
    // $("#"+tag_grp_seq).remove();
}

function addTagGrp(grpId){
    var oldEditPlace = $(".allow_edit");
    var oldEditPlaceId = oldEditPlace.attr("id");
    var tagsLength = oldEditPlace.find("ul[class='pager']").children().length;
    if(tagsLength<1){
        alert("当前可编辑标签组为空，请在当前标签组中进行编辑！");
        return;
    }


    oldEditPlace.find("ul[class='pager']").append(
        "<li id='rm_tg_grp_li' class='previous'><a href='#' onclick=removeTagGrp('"+oldEditPlaceId+"')>删除此标签组</a></li>");

    var newTagGrp = $("<div class='allow_edit'></div>");
    if(grpId!=null&&grpId!=''){
        newTagGrp.attr("id",grpId);
    }
    var newGrpNm = $("<div class='grp_name'>标签组</div>");
    var tagGrpPlace = $("<div class='tag_grp'><nav><ul class='pager'></ul></nav></div>");
    var splitLine = $("<div class='tag_grp_split_line'></div>");

    newTagGrp.append(newGrpNm);
    newTagGrp.append(tagGrpPlace);
    newTagGrp.append(splitLine);
    // alert(newTagGrp.html());

    oldEditPlace.after(newTagGrp);
    oldEditPlace.attr("class","has_done_grp");
    // oldEditPlace.removeAttr("class");
    // oldEditPlace.attr("class","");
    // alert(oldEditPlace.html());

    freeAllCheckTag();

    sumTagGrpHeight();
}

function freeAllCheckTag(){
    // $(".icheck_radio").iCheck('uncheck');//太慢,后续优化
    if(hasCheckRadioIdTmp!=null&&hasCheckRadioIdTmp.length>0){
        $.each(hasCheckRadioIdTmp,function(i,radioObj){
            $(".icheck_radio[id='"+radioObj+"']").iCheck('uncheck');
        });
    }
}

/**
 * 动态计算当前标签组所属DIV高度,之后重定向标签组名的位置(否则可能造成垂直方向不居中)
 */
function sumTagGrpHeight(){
    var now_heg = $(".allow_edit").find(".tag_grp").height();
    $(".allow_edit").find(".grp_name").css("padding-top",(now_heg-20)/2);
    // alert($(".allow_edit").attr("id"));

}

/**
 * 将标签按组和类别整理到对象中
 * 结果格式:
 * {
 *  "grp_0":
 *      {
 *          "标签类别ID":["标签ID","标签ID"],
 *          "标签类别ID":["标签ID"]
 *      },
 *  "grp_1":
 *      {
 *          "标签类别ID":["标签ID"],
 *          "标签类别ID":["标签ID"]
 *      }
 * }
 */
function sumTagToObj(){
    var hasDoneGrpArray = $(".has_done_grp");
    var allowEditObj = $(".allow_edit");
    hasDoneGrpArray.push(allowEditObj);
    var resultTagGrp = new Object();
    $.each(hasDoneGrpArray,function(i,hasDoneGrpObj){//取所有标签组,循环
        var allTagCtgy = $(hasDoneGrpObj).find(".tag_grp").find(".pager").find("li[id!='rm_tg_grp_li']");//取标签组下标签类别,循环
        // alert($(hasDoneGrpObj).find(".tag_grp").html());
        var tagCtgyMap = new Object();
        $.each(allTagCtgy,function(i,tagCtgyObj){
            var tagCtgyId = $(tagCtgyObj).attr("id");
            var tagList = $(tagCtgyObj).find("span");//取标签类别下标签,循环
            var tagArray = new Array();
            $.each(tagList,function(i,tagObj){
                var tagId = $(tagObj).attr("id");
                if(tagId!=null&&tagId!='')
                    tagArray.push(tagId);
            });
            tagCtgyMap[tagCtgyId] = tagArray;
        });
        resultTagGrp['grp_'+i] = tagCtgyMap;
    });
    return resultTagGrp;
}

/**
 * 保存筛选条件
 */
function saveSnCdion(saveBtn) {
    if($(saveBtn).attr("class").indexOf("disabled")>0){
        return;
    }
    // checkNotNull();
    var tagData = sumTagToObj();
    var sendData = new Object();
    sendData["tagData"] = tagData;
    sendData["screeningId"] = $("#screeningId").val();
    submitJsonStr2Controller("/screen/sum/updateScreening.do",saveBackFunction,sendData);

    function saveBackFunction(){
        alert("done");
    }
}

