package com.seabox.tagsys.persongroup.service;

import com.seabox.test.base.BaseTestClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pc on 2016/11/17.
 */
public class PersonScreenServiceImpTest extends BaseTestClass{
    @Test
    public void delScreeningData() throws Exception {
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("screeningId","126");
        personScreenServiceImp.delScreeningData(paramMap);
    }

    @Test
    public void getScreeningAndTags() throws Exception {
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("screeningId","473");
        System.out.println(personScreenServiceImp.getScreeningAndTags(paramMap));
    }

    @Test
    public void saveScreening() throws Exception {
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("screeningNm","TTT");
        paramMap.put("user_id","6");

        Map<String,Map<String,List<String>>> tagGrpMap = new HashMap<>();

        Map<String,List<String>> tagCtgyMap = new HashMap();
        Map<String,List<String>> tagCtgyMap1 = new HashMap();

        List<String> tagList1 = new ArrayList<>();
        List<String> tagList2 = new ArrayList<>();
        List<String> tagList3 = new ArrayList<>();
        List<String> tagList4 = new ArrayList<>();
        tagList1.add("100004");
        tagList1.add("100005");
        tagList2.add("100001");
        tagList3.add("100002");
        tagList4.add("100012");

        tagCtgyMap.put("10203000",tagList1);
        tagCtgyMap.put("10203001",tagList2);

        tagCtgyMap1.put("10203001",tagList3);
        tagCtgyMap1.put("11203000",tagList4);

        tagGrpMap.put("grp_0",tagCtgyMap);
        tagGrpMap.put("grp_1",tagCtgyMap1);


        paramMap.put("tagData",tagGrpMap);
        personScreenServiceImp.saveScreening(paramMap);
    }

    @Autowired
    private PersonScreenServiceImp personScreenServiceImp;

    @Test
    public void listTagDataTest(){
        System.out.println(personScreenServiceImp.listTagData(null));
    }

    @Test
    public void listMainDataTest(){
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("currPage","1");
        paramMap.put("pageSize","10");
        personScreenServiceImp.listMainData(paramMap);
    }

}