package com.seabox.tagsys.persongroup.service;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.seabox.tagsys.usertags.hbase.entity.TUserInfo;
import com.seabox.test.base.BaseTestClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by pc on 2016/12/15.
 */
public class PersonListServiceTest extends BaseTestClass{
    @Autowired
    private PersonListService personListService;

    @Test
    public void createPersonListFromHbase() throws Exception {
        List<TUserInfo> result = personListService.createPersonListFromHbase("148", 2,8);
        System.out.println();
    }


    public static void main(String[] args){
        PersonListServiceTest test = new PersonListServiceTest();
        test.mai111n();
    }

    @Test
    public void mai111n(){
        BitSet bitSet = new BitSet();
        bitSet.set(2);
        bitSet.set(5);
        bitSet.set(10);
        bitSet.set(20);
        String bitSetStr = bitSet.toString();
        bitSetStr = bitSetStr.replaceAll("\\{","[");
        bitSetStr = bitSetStr.replaceAll("}","]");

        ObjectMapper mapper = new ObjectMapper();

        JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, String.class);

        try {
            List<String> list = mapper.readValue(bitSetStr, List.class);
            System.out.println();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}