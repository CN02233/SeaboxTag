package com.seabox.tagsys.camp.service.imp;

import com.seabox.tagsys.camp.service.CampManagerService;
import com.seabox.test.base.BaseTestClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by pc on 2016/12/14.
 */
public class CampViewServiceImpTest extends BaseTestClass{

    @Autowired
    private CampViewServiceImp enventViewService;

    @Test
    public void listTagValiData() throws Exception {

        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("campId","125");

        String result = enventViewService.listTagValiData(paramMap);

        System.out.println(result);
    }

}