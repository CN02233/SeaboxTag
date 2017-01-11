package com.seabox.tagsys.persongroup.controller;

import com.seabox.tagsys.persongroup.service.PortraitServiceImp;
import com.seabox.tagsys.persongroup.utils.TagHelperBean;
import com.seabox.tagsys.persongroup.utils.TagHelperUtil;
import com.seabox.test.base.BaseTestClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by pc on 2016/12/19.
 */
public class PortraitControllerTest extends BaseTestClass{

    @Autowired
    private PortraitServiceImp portraitServiceImp;

    @Test
    public void getTop10Tags() throws Exception {
        String screeningId = "125";
        int topN = 10;

        List<TagHelperBean> allTagList = portraitServiceImp.getAllTags(TagHelperUtil.TagOrder.ORDER_DESC);

        List<Map<String, Object>> topN_tags = portraitServiceImp.getTopNTags(allTagList,topN);

        Map<String, List<Map<String, Object>>> screening_tags = portraitServiceImp.getTagsForScreeningOrder(screeningId, allTagList);

        System.out.println();
    }

}