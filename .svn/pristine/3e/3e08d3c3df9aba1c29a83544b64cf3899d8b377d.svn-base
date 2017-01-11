/**
 * Created by wuchh on 1/27/16.
 */





function drawTagCoveragePieChart( chartData ){

    var myChart = echarts.init(document.getElementById('tagCoverChartArea'));

    // 指定图表的配置项和数据
    var option = {
        /*
         title : {
         text: ' Title Text',
         subtext: ' sub title 2nd line',
         x:'center'
         }, */
        tooltip : {
            trigger: 'item',
            formatter: "{b}"
        },
        legend: {
            orient: 'vertical',
            left: '10px',
            top: '15px',
            show : true,
            radius: '25%',
            data: chartData.series_data
        },
        backgroundColor: '#f7f7f7',
        series : [
            {
                //  name: '访问来源',
                type: 'pie',
                radius: ['0', '60%'],
                center: ['65%', '50%'],

                label : {
                    normal: {
                        show : true,
                        //position : 'inner',
                        formatter: function(params) {
                            return params.data.short_name;
                        },
                    },
                    emphasis : {
                        show : false
                    }
                },
                data: chartData.series_data
            }
        ]
    };


    // 使用刚指定的配置项和数据显示图表。
    myChart.setOption(option);

}


/**
 * 弹出提示框方法
 * @param rowId   行号，用于定位到行，取到　dataStore
 * @param showMsg 需要显示到提示框中的文字
 * @param params 需要传递给callback的参数,可为空,此方法只做传递,不对其做任何处理
 * @param showTitle 弹出框title
 */
function createModalCoverageChart( rowId, showMsg, showTitle, params){

    //var trObj = $("#tag_row_idx_" + rowId);
    var chartData = tagChartDataStore[ rowId ];

    //创建modal div最外层
    var modalBodyDiv = $("<div></div>");
    modalBodyDiv.attr("id","tagCoverRateChartModal");
    modalBodyDiv.attr("tabindex","-1");
    modalBodyDiv.attr("role","dialog");
    modalBodyDiv.attr("aria-hidden","true");
    modalBodyDiv.attr("class","modal fade modal_div");


    var modalDialogDiv = $("<div></div>");
    modalDialogDiv.attr("class","modal-dialog");


    var modalContentDiv = $("<div></div>");
    modalContentDiv.attr("class","modal-content");
    modalContentDiv.css("width","660px");
    modalContentDiv.css("height","550px");/// needed


    if(showTitle==null)
        showTitle = "提&nbsp;&nbsp;示";
    var modalDivHeaderDiv = $("<div>"+showTitle+"<label class='close sprite-img-second' onclick='closeModalCoverageChart();'></label></div>");
    modalDivHeaderDiv.attr("class","modal_div_header");

    var modalDivContentDiv = $("<div id='tagCoverChartArea'></div>");
    modalDivContentDiv.attr("class","modal_div_content");
    modalDivContentDiv.css("width","100%");
    modalDivContentDiv.css("height","520px"); /// needed

    //modalDivContentDiv.append("<div id='tagCoverChartArea' class='modal_div_content_text'>"+showMsg+"</div>");


    modalContentDiv.append(modalDivHeaderDiv);
    modalContentDiv.append(modalDivContentDiv);
    modalDialogDiv.append(modalContentDiv);
    modalBodyDiv.append(modalDialogDiv);

    $("body").append(modalBodyDiv);

    $("#tagCoverRateChartModal").modal({backdrop:false,show:true});


    $('#tagCoverRateChartModal').on('shown.bs.modal', function (e) {
        drawTagCoveragePieChart( chartData );
    });

}

function closeModalCoverageChart(){
    $("#tagCoverRateChartModal").remove();
}
