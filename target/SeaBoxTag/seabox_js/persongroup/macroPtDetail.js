var screeningIdVal = $("#screeningId").val();

var sendData = {"screeningId":screeningIdVal,"topN":"10"};

submitData2Controller("/person/macroPtDetail/top10Tags.do",callBackFunction,sendData);


function callBackFunction(resultData){
    var topN_tags = resultData.topN_tags;
    var screening_tags =  resultData.screening_tags;
    initTop10Body(topN_tags);
    initScreeningTagBar(screening_tags);
    initTagGrp(screening_tags);
    // $("#top10_count").hide();
}

/**
 * 获取整个系统的标签,将排名靠前的10个显示出来
 * @param screening_tags
 */
function initTop10Body(topN_tags){
    var topNTagLength = topN_tags.length;
    var topNTagNmArray = new Array(topNTagLength);
    var topNTagCountArray = new Array(topNTagLength);
    $.each(topN_tags,function(i,topNTag){
        var tagId = topNTag.tagId;
        var tagNm = topNTag.tagNm;
        var tagCtgyNm = topNTag.tagCtgyNm;
        var tagCtgyId = topNTag.tagCtgyId;

        var userCount = topNTag.userCount;

        topNTagNmArray[topNTagLength-(i+1)] = tagNm;
        topNTagCountArray[topNTagLength-(i+1)] = {value:userCount,tag_id:tagId,tag_nm:tagNm,tag_ctgy_id:tagCtgyId,tag_ctgy_nm:tagCtgyNm};
    });


    require.config({
        paths: {
            echarts : fullPath+'/utils/echarts/js'
        }
    });



    require(
        [
            'echarts',
            'echarts/chart/bar' // 使用柱状图就加载bar模块，按需加载
            ,'echarts/config'
        ],
        function (ec) {
            // 基于准备好的dom，初始化echarts图表
            var top10_main_chart = ec.init(document.getElementById('top10_main'));

            var ecConfig_top10 = require('echarts/config');

            top10_main_chart.on(ecConfig_top10.EVENT.CLICK, top10tag_click);


            var option = {
                title : {
                    text: '标签系统内所有标签'
                },
                calculable : false,
                xAxis : [
                    {type : 'value'}
                ],
                yAxis : [

                    {type : 'category',data : topNTagNmArray}
                ],
                series : [
                    {
                        type:'bar',
                        color: '#6495ed',
                        itemStyle : { normal: {label : {show: true, position: 'insideRight'},color:'#30B8B8'}},
                        data:topNTagCountArray
                    }
                ]
            };
            // 为echarts对象加载数据
            top10_main_chart.setOption(option);
        }
    );
}

/**
 * 获取当前筛选条件中的标签,将标签组成柱状图
 * @param screening_tags
 */
function initScreeningTagBar(screening_tags){
    var topN_tags = screening_tags.orderList;

    var topNTagLength = topN_tags.length;
    var topNTagNmArray = new Array(topNTagLength);
    var topNTagCountArray = new Array(topNTagLength);

    $.each(topN_tags,function(i,topNTag){
        var tagId = topNTag.tagId;
        var tagNm = topNTag.tagNm;
        var tagCtgyNm = topNTag.tagCtgyNm;

        var userCount = topNTag.userCount;

        topNTagNmArray[topNTagLength-(i+1)] = tagNm;
        topNTagCountArray[topNTagLength-(i+1)] = userCount;
    });

    require.config({
        paths: {
            echarts : fullPath+'/utils/echarts/js'
        }
    });
    // 使用
    require(
        [
            'echarts',
            'echarts/chart/bar' // 使用柱状图就加载bar模块，按需加载
        ],
        function (ec) {
            // 基于准备好的dom，初始化echarts图表
            var myChart = ec.init(document.getElementById('grp_top10_main'));

            var option = {
                title : {
                    text: '筛选条件内所包含标签'
                },
                calculable : false,
                xAxis : [
                    {
                        type : 'value'
                    }
                ],
                yAxis : [
                    {
                        type : 'category',
                        data : topNTagNmArray
                    }
                ],
                series : [
                    {
                        name:'直接访问',
                        type:'bar',
                        stack: '总量',
                        itemStyle : { normal: {label : {show: true, position: 'insideRight'},color:'#30B8B8'}},
                        data:topNTagCountArray
                    }
                ]
            };
            // 为echarts对象加载数据
            myChart.setOption(option);
        }
    );
}

/**
 * 将本次筛选条件对应的标签显示到页面
 *
 * <nav>
 <ul class="pager grp_top_name">
 <li class="previous"><span style="border:0;" href="#">SHIT</span></li>
 </ul>
 <ul class="pager grp_tag_body">
 <li class="previous"><span href="#">性别:男</span></li>
 <li class="previous"><span  href="#">年龄>30</span></li>
 <li class="previous"><span  href="#">民族:汉</span></li>
 <li class="previous"><span   href="#">学历:本科</span></li>
 <li class="previous"><span   href="#">学历:本科</span></li>
 <li class="previous"><span   href="#">学历:本科</span></li>
 <li class="previous"><span   href="#">学历:本科</span></li>
 <li class="previous"><span   href="#">学历:本科</span></li>
 <li class="previous"><span   href="#">学历:本科</span></li>
 <li class="previous"><span   href="#">学历:本科</span></li>
 </ul>
 </nav>
 * @param screening_tags
 */
function initTagGrp(screening_tags) {
    var tagGrpList = screening_tags.tagList;
    // alert(JSON.stringify(tagGrpList));
    $.each(tagGrpList,function(i,tagInfo){
        var tagId = tagInfo.tagId;
        var tagNm = tagInfo.tagNm;
        var tagCtgyId = tagInfo.tagCtgyId;
        var tagCtgyNm = tagInfo.tagCtgyNm;

        var tagShowHtml = "<span href='#'>"+tagCtgyNm+":"+tagNm+"</span>";

        var grpSeq = tagInfo.grpSeq;
        var show_grp_seq = "grp_seq_"+grpSeq;
        if($("#"+show_grp_seq).length>0){
            var tagCtgyHtml = $("#"+show_grp_seq).find(".grp_tag_body").find("li[id='tag_ctgy_"+tagCtgyId+"']");
            if(tagCtgyHtml.length>0){
                $(tagCtgyHtml).find("span").append(","+tagNm);
            }else{
                $("#"+show_grp_seq).find(".grp_tag_body").append("<li id='tag_ctgy_"+tagCtgyId+"' class='previous'>"+tagShowHtml+"</li>");
            }
        }else{
            var nav_obj = $("<nav id='"+show_grp_seq+"'></nav>");
            nav_obj.append("<ul class='pager grp_top_name'><li class='previous'><span style='border:0;' href='#'>标签组-"+grpSeq+"</span></li></ul>");
            nav_obj.append("<ul class='pager grp_tag_body'><li id='tag_ctgy_"+tagCtgyId+"' class='previous'>"+tagShowHtml+"</li></ul>");
            $("#screening_div").append(nav_obj);
        }

    });

    // alert($("#screening_div").html());
}


function top10tag_click(param){
    var tagId = param.data.tag_id;
    var tagNm = param.data.tag_nm;
    var tagCtgyId = param.data.tag_ctgy_id;
    var tagCtgyNm = param.data.tag_ctgy_nm;
    var spanTagObj = $("span[tagId='"+tagId+"']");

    var isRemove = false;

    if(spanTagObj.length>0){
        var parentSpan = spanTagObj.parent("span").children("span");
        if(parentSpan.length<=1){
            spanTagObj.parent("span").remove();
            isRemove = true;
        }else{
            $(spanTagObj).remove();
            isRemove = true;
        }
    }

    if(!isRemove){
        var ctgySpan = $("#top10_count_li").find("span[id='tagCtgyId_"+tagCtgyId+"']");
        if(ctgySpan.length>0){
            ctgySpan.append("<span tagId='"+tagId+"'>,"+tagNm+"</span>");
        }else{
            $("#top10_count_li").append("<span id='tagCtgyId_"+tagCtgyId+"' tagCtgyId='"+tagCtgyId+"'><span tagId='"+tagId+"'>"+tagCtgyNm+":"+tagNm+"</span></span></span>");
        }
    }
    sumTop10Tag();
    // alert(JSON.stringify(param));
}

function sumTop10Tag(){
    var tagCtgyArray = $("#top10_count_li").children("span");
    if(tagCtgyArray.length>0){
        var tagCtgyArrayObj = new Object();
        $.each(tagCtgyArray,function(i,tagCtgyObj){
            var tagCtgyId = $(tagCtgyObj).attr("tagCtgyId");
            var tagArrays = $(tagCtgyObj).find("span");
            var tagArrayObj = new Array();
            if(tagArrays.length>0){
                $.each(tagArrays,function(i,tagObj){
                    var tagId = $(tagObj).attr("tagId");
                    tagArrayObj.push(tagId);
                });
            }
            tagCtgyArrayObj[tagCtgyId] = tagArrayObj;
        });

    }
    var resultTagGrp = new Object();
    resultTagGrp['grp_1'] = tagCtgyArrayObj;

    submitJsonStr2Controller("/screen/sum/sumPCnt.do",saveBackFunction,resultTagGrp);
    function saveBackFunction(data){
        $("#top10_sum_count").html(data);
    }

}