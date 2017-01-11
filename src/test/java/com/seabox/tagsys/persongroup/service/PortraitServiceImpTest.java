package com.seabox.tagsys.persongroup.service;

import com.seabox.test.base.BaseTestClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by pc on 2016/12/16.
 */
public class PortraitServiceImpTest extends BaseTestClass{

    @Autowired
    private PortraitServiceImp portraitServiceImp;

    @Test
    public void getUserAllTag() throws Exception {
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("userGuid","675");
        portraitServiceImp.getUserAllTag("675");
    }

}