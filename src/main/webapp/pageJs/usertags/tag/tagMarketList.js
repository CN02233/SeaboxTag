




function addLevelOneTagCategoryToTable(data) {

    tagChartDataStore=new Array(); // re-initialize the data Store

    var enableLimitHideMoreL2AndL3  = false;
    var enableCoverageChartOnTheFly = false;

    var  index_l3_tag_cat = 0;

    var parentDiv = $("#tag-view-parent-div");

    for (var i = 0; i < data.length; ++i) {
        var tagCat1 = data[i];

        var tagCatTable = $("<table class='tag-view-table'></table>");
        parentDiv.append(tagCatTable);


        var tbl_thread = $("<thread></thread>");
        tagCatTable.append(tbl_thread);
        var tbl_level_1_head = $("<tr class='thread-tr'></tr>");
        tbl_thread.append(tbl_level_1_head);

//--------------------------------------------------
// add level 1 category
//--------------------------------------------------
        var  cat1Header =$("<th class='tag-view-l1-cat'>" + tagCat1.tag_ctgy_nm + "</th>");
        cat1Header.onmousedown = minusDiv;
        tbl_level_1_head.append(cat1Header);


        tbl_level_1_head.append("<th class='tag-view-l1-cat'></th>");

        if(tagCat1.children != null) {

            var num_of_level_2 = 0;

            for (var i2 = 0; i2 < tagCat1.children.length; ++i2) {

                var tagCat2 = tagCat1.children[i2];

                var tbl_level2_row = $("<tr></tr>");
                tbl_thread.append(tbl_level2_row);
                ++num_of_level_2;
                if(enableLimitHideMoreL2AndL3 && num_of_level_2 ==3) {
                    var link_tag_l1_more = "<a style=\"color: #fef9ff\"   onclick=\"tagViewByParentCategory("  + tagCat1.tag_ctgy_id + ");\">" + ">>.." + "</a>";
                    tbl_level2_row.append("<td valign='middle' ><div class='tag-view-l2-more-td'>" + link_tag_l1_more + "</div></td>");
                    tbl_level2_row.append("<td valign='middle'>" + "</td>");
                    console.log("break l2! >>..");
                    break;  //############ limit Level 2 presentation
                }


//--------------------------------------------------
// add level 2 category
//--------------------------------------------------
                if(tagCat1.sub_ctgy_depth != null
                    && tagCat1.sub_ctgy_depth == 2
                ) {
                    tagCat2 = tagCat1;
                }
                var link_tag_l2 = "<a style=\"color: #fef9ff\"   onclick=\"tagViewByParentCategory("  + tagCat2.tag_ctgy_id + ");\">" + tagCat2.tag_ctgy_nm + "</a>";
                // only add level-2 td in case of depth >=3
                tbl_level2_row.append("<td valign='middle' style='border-width: 15px; border-color: transparent'><div  class='tag-view-l2-cat" + (1+(i%4)) +"'>" + link_tag_l2 + "</div></td>");

                var td_level3 = $('<td class="tag-view-last-td"></td>'); // L3 category
                tbl_level2_row.append(td_level3);

                if(tagCat2.children != null) {
                    var div_3_in_row = null;

                    var num_of_level_3 = 0;
                    for(var i3=0; i3<tagCat2.children.length; ++i3) {

                        if(null==div_3_in_row) {
                            td_level3.append('<div  style="width:100%; border-radius: 10px; height:5px"> </div>');

                            div_3_in_row = $('<div align="center" style="width:100%;height:50px"></div>');
                            td_level3.append(div_3_in_row);
                        }

                        div_3_in_row.append('<div  class="tag-view-separate"> </div>');
                        var tagCat3 = tagCat2.children[i3];

                        if(enableCoverageChartOnTheFly) {
                            addTagsCoverageForLastLevelCategoryToDataStore( tagCat3 , index_l3_tag_cat );
                        }


//--------------------------------------------------
// add level 3 category
//--------------------------------------------------
                        //link to Coverage Chart
                        //var link_tag_cover = "<a style=\"color: #fef9ff\"  href=\"javascript:createModalCoverageChart(" + index_l3_tag_cat + ", '" + tagCat3.tag_ctgy_nm + "', '" + tagCat3.tag_ctgy_nm + "');\">" + tagCat3.tag_ctgy_nm + "</a>";

                        ++num_of_level_3;

                        var  MAX_NUM_OF_L3 = 6;
                        if(tagCat1.sub_ctgy_depth != null
                            && tagCat1.sub_ctgy_depth <= 2
                        ) {
                            MAX_NUM_OF_L3 = 8;
                        }
                        if(enableLimitHideMoreL2AndL3 && num_of_level_3 == MAX_NUM_OF_L3) {
                            var link_tag_l3_more;
                            if(tagCat1.sub_ctgy_depth != null
                                && tagCat1.sub_ctgy_depth <= 2
                            ) {
                                link_tag_l3_more = "<a style=\"color: #fef9ff\"   onclick=\"tagViewByParentCategory("  + tagCat1.tag_ctgy_id + ");\">" + ">>>..." + "</a>";
                            } else {
                                link_tag_l3_more = "<a style=\"color: #fef9ff\"   onclick=\"tagViewByParentCategory("  + tagCat2.tag_ctgy_id + ");\">" + ">>>..." + "</a>";
                            }
                            div_3_in_row.append('<div class="tag-view-l3-more-td">' + link_tag_l3_more + '</div>');

                            console.log("break l3! >>>...");
                            break; //############ limit Level 3 presentation

                        }

                        var link_tag_l3 = "<a style=\"color: #fef9ff\"   onclick=\"tagViewByParentCategory("  + tagCat2.tag_ctgy_id + "," + tagCat3.tag_ctgy_id + ");\">" + tagCat3.tag_ctgy_nm + "</a>";
                        div_3_in_row.append('<div class="tag-view-l3-cat' + (1+(i%4)) + '">' + link_tag_l3 + '</div>');

                        index_l3_tag_cat++;

                        if((i3 % 3)==2) {
                            div_3_in_row = null;
                        }


                    }
                }


                if(tagCat2 == tagCat1) {
                    break; // break for looping on L1 children
                }
            }
        }

    }


    parentDiv.masonry( );

}


/*
   6 of Chinese chars vs  10 English(or number) chars.
   6:10 = 3:5
*/
function getStrPoints(str){
    var lengthPoint = 0;
    var len = str.length;
    var charCode = -1;
    for(var i = 0; i < len; i++){
        charCode = str.charCodeAt(i);
        if (charCode >= 0 && charCode <= 128) {
            lengthPoint += 3; // english give 3 points
        }else{
            // Chinese give 5 points
            lengthPoint += 5;
        }
    }
    return lengthPoint;
}


function addLevelOneTagCategoryToTable_DaMoStyle(data) {

    tagChartDataStore=new Array(); // re-initialize the data Store

    var enableLimitHideMoreL2AndL3  = false;
    var enableCoverageChartOnTheFly = false;

    var  index_l3_tag_cat = 0;
    var parentDiv = $("#tag-view-parent-div");


    for (var i = 0; i < data.length; ++i) {
        var tagCat1 = data[i];

        var l1_all_subs_div = $('<div class="grid-item div_grid_cell_div_0"  ></div>');

        var  spaceDiv =$('<div class="left_hide_margin_div_0_1">&nbsp;&nbsp;</div>');
        l1_all_subs_div.append(spaceDiv);


        var div_body = $('<div class="right_body_div_0_2"></div>');
        l1_all_subs_div.append( div_body );
//--------------------------------------------------
// add level 1 category
//--------------------------------------------------
        var  cat1Header =$('<div class="l1_icon_and_text_div_0_2_1" ></div>');
        var  tagIcon_eng_name;

        // TODO: this icon name mapping could move to DB or somewhere:
        if(tagCat1.tag_ctgy_nm.match("基本") || tagCat1.tag_ctgy_nm.match("基础") ) {
            tagIcon_eng_name = "basic";
        } else if(tagCat1.tag_ctgy_nm.match("社会") ) {
            tagIcon_eng_name = "social";
        } else if(tagCat1.tag_ctgy_nm.match("消费") ) {
            tagIcon_eng_name = "consume";
        } else if(tagCat1.tag_ctgy_nm.match("兴趣") ) {
            tagIcon_eng_name = "interest";
        } else if(tagCat1.tag_ctgy_nm.match("风险") ) {
            tagIcon_eng_name = "risk";
        } else if(tagCat1.tag_ctgy_nm.match("资产") ) {
            tagIcon_eng_name = "assets";
        } else {
            tagIcon_eng_name = "not_exists";
        }

        cat1Header.append('<div class="l1_icon_div_0_2_1_A"><label class="sprite-img-market_icon" style="background:url(/SeaBoxTag/images/usertags/market_icon_' + tagIcon_eng_name + '.svg) no-repeat;"></label></div>');
        cat1Header.append('<div class="l1_text_div_0_2_1_B">' + tagCat1.tag_ctgy_nm + '</div>');
        div_body.append(cat1Header);

        var right_div = $('<div class="l2_l3_text_div_0_2_2"></div>');
        div_body.append(right_div);

        var l2_l3_head_margin = $('<div class="one_line_l2_l3_top_margin_div_0_2_2_A" ></div>');
        right_div.append( l2_l3_head_margin );

        if(tagCat1.children != null) {

            var num_of_level_2 = 0;

            var  lines_of_level_2_3 = 0;
            for (var i2 = 0; i2 < tagCat1.children.length; ++i2) {

                var tagCat2 = tagCat1.children[i2];

                ++num_of_level_2;
                if(enableLimitHideMoreL2AndL3 && num_of_level_2 ==3) {
                    break;  //############ limit Level 2 presentation
                }

//--------------------------------------------------
// add level 2 category
//--------------------------------------------------
                if(tagCat1.sub_ctgy_depth != null
                    && tagCat1.sub_ctgy_depth == 2
                ) {
                    tagCat2 = tagCat1;
                }
                var link_tag_l2 = '<a class="l2_text"   onclick="tagViewByParentCategory('  + tagCat2.tag_ctgy_id + ');">' + tagCat2.tag_ctgy_nm + '</a>';
                // only add level-2 td in case of depth >=3

                var one_line_l2_and_l3 = $('<div class="one_line_l2_and_l3_div_0_2_2_B"></div>');

                var one_line_l2 = $('<div class="one_line_l2_part_div_0_2_2_B_1" >' + link_tag_l2 + '&nbsp;&nbsp;>' + '</div>');
                one_line_l2_and_l3.append( one_line_l2 );
                var one_line_l3 = $('<div class="one_line_l3_part_div_0_2_2_B_2" ></div>');
                one_line_l2_and_l3.append( one_line_l3 );
                right_div.append(one_line_l2_and_l3);

                ++lines_of_level_2_3;

                if(tagCat2.children != null) {

                    var need_add_new_line = false;
                    var l3StrPoints = 0;
                    var one_line_counter = 0;
                    for(var i3=0; i3<tagCat2.children.length; ++i3) {

                        var tagCat3 = tagCat2.children[i3];

                        if(enableCoverageChartOnTheFly) {
                            addTagsCoverageForLastLevelCategoryToDataStore( tagCat3 , index_l3_tag_cat );
                        }


//--------------------------------------------------
// add level 3 category
//--------------------------------------------------

                        if(need_add_new_line) {
                            one_line_l2_and_l3 = $('<div class="one_line_l2_and_l3_div_0_2_2_B"></div>');

                            one_line_l2 = $('<div class="one_line_l2_part_div_0_2_2_B_1" >' + '&nbsp;&nbsp;&nbsp;&nbsp;' + '</div>');
                            one_line_l2_and_l3.append( one_line_l2 );
                            one_line_l3 = $('<div class="one_line_l3_part_div_0_2_2_B_2" ></div>');
                            one_line_l2_and_l3.append( one_line_l3 );
                            right_div.append(one_line_l2_and_l3);
                            ++lines_of_level_2_3;
                            need_add_new_line = false;
                        }

                        l3StrPoints += getStrPoints( tagCat3.tag_ctgy_nm );
                        if( l3StrPoints > 120) { // points:120 ~= 24 Chinese
                            if(one_line_counter==0) {
                                var link_tag_l3 = '<a class="l3_text"   onclick="tagViewByParentCategory('  + tagCat2.tag_ctgy_id + "," + tagCat3.tag_ctgy_id + ');">&nbsp;' + tagCat3.tag_ctgy_nm + "</a>";
                                one_line_l3.append( link_tag_l3 + '&nbsp;&nbsp;|');
                            } else {
                                i3 -= 1; //move this to padding list back, let's handle it in next line.
                            }
                            need_add_new_line = true;
                            l3StrPoints = 0;
                            one_line_counter = 0;
                            continue;

                        } else {
                            ++one_line_counter;
                            var link_tag_l3 = '<a class="l3_text"   onclick="tagViewByParentCategory('  + tagCat2.tag_ctgy_id + "," + tagCat3.tag_ctgy_id + ');">&nbsp;' + tagCat3.tag_ctgy_nm + "</a>";
                            one_line_l3.append( link_tag_l3 + '&nbsp;&nbsp;|');

                            if(one_line_counter >= 4) {
                                need_add_new_line = true;
                                l3StrPoints = 0;
                                one_line_counter = 0;
                            }

                        }

                    }
                }


                if(tagCat2 == tagCat1) {
                    break; // break for looping on L1 children
                }
            }

            if(lines_of_level_2_3 == 1) {
                var add_more_head_margin = $('<div class="one_line_l2_l3_add_more_margin_div_0_2_2_A" ></div>');
                add_more_head_margin.insertBefore( l2_l3_head_margin );
            }

            var l2_l3_bottom_margin = $('<div class="one_line_l2_l3_bottom_margin_div_0_2_2_C" ></div>');
            right_div.append( l2_l3_bottom_margin );
        }


        parentDiv.masonry()
            .append( l1_all_subs_div )
            .masonry( 'appended', l1_all_subs_div )
            // layout
            .masonry( {
                transitionDuration: '0.2s',
            });

    }


}

//折叠或者显示层
function minusDiv(e) {
    e = e || event
    var nr = this.parentNode.parentNode.nextSibling;    //取得内容层
    nr.style.display = nr.style.display == "" ? "none" : "";
}

/**
 * 获取标签列表数据
 */
function listAllTagCategoriesByLevel(pageDataObj) {

    jQuery.ajax({
        type : 'GET',
        contentType : 'application/json',
        url : postUrl + '/usertags/tag/category/list-tops.do',
        data : '',
        dataType : 'json',
        success : function(data) {
            addLevelOneTagCategoryToTable_DaMoStyle(data);
        },
        error : function(data) {
            alert("获取标签列表数据失败, 请检查网络");
        }
    });

}


function tagViewByParentCategory(parent_ctgy_id, focus_ctgy_id) {
    var param = [
        {id:"parent_ctgy_id",value:parent_ctgy_id},
        {id:"focus_ctgy_id",value:focus_ctgy_id}
    ];
    newPageByPost(param,'/usertags/tag/tagViewByCat.do');
}

