package com.seabox.tagsys.persongroup.service;

import com.seabox.test.base.BaseTestClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pc on 2016/12/7.
 */

public class PortraitServiceTest extends BaseTestClass{

    @Autowired
    private PortraitServiceImp portraitService1;

    @Test
    public void listUser4Screening() throws Exception {
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("screeningId","012");
        paramMap.put("currPage","1");
        paramMap.put("pageSize","10");
        portraitService1.listUser4Screening(paramMap);
    }

    @Test
    public void enumTest(){
        String mr_str = "MR";
        String sex_mr_str = "SEX_MR";
        PersonEnum adsas = PersonEnum.valueOf("Y");

        String name123 = PersonEnum.SEX_MR.name;
        String val = PersonEnum.SEX_MR.val;

        System.out.println(PersonEnum.SEX_MR);
        if("1".equals(PersonEnum.SEX_MR.toString())){
            System.out.println(PersonEnum.SEX_MR.getVal(PersonEnum.SEX_MR));
        }
    }


    enum PersonEnum{

        SEX_MR("MR","1") ,SEX_MS("MS","0"),MARRIED("Y","0"),NO_MARRIED("N","1");

        private String val;
        private String name;

        private PersonEnum(String name ,String val){
            this.name = name;
            this.val = val;
        }

        public String getVal (PersonEnum personEnum){
            return personEnum.name;
        }

//        @Override
        public String toString(){  //覆盖了父类Enum的toString()
            return val;
        }
    }

}