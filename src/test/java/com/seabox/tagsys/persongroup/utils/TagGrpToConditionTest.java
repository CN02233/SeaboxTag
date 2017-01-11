package com.seabox.tagsys.persongroup.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seabox.tagsys.persongroup.entity.TagCtgyEntity;
import com.seabox.tagsys.persongroup.entity.TagGrpEntity;
import com.seabox.tagsys.usertags.logicquery.impl.TagConditionBase;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by pc on 2016/12/14.
 */
public class TagGrpToConditionTest {

    public static void main(String[] args) throws Exception {
        TagGrpToConditionTest test = new TagGrpToConditionTest();
        test.getCondition();
    }

    public void getCondition() throws Exception {

        List<String> tags1 = new ArrayList<>();
        List<String> tags2 = new ArrayList<>();
        List<String> tags3 = new ArrayList<>();

        tags1.add("100004");
        tags1.add("100005");

        tags2.add("100001");

        tags3.add("100012");
        tags3.add("100013");

        TagCtgyEntity tagCtgyEntity1 = new TagCtgyEntity();
        TagCtgyEntity tagCtgyEntity2 = new TagCtgyEntity();
        TagCtgyEntity tagCtgyEntity3 = new TagCtgyEntity();

        tagCtgyEntity1.setTagCtgyId("10203000");
        tagCtgyEntity1.setTagIds(tags1);

        tagCtgyEntity2.setTagCtgyId("10203001");
        tagCtgyEntity2.setTagIds(tags2);

        tagCtgyEntity3.setTagCtgyId("11203000");
        tagCtgyEntity3.setTagIds(tags3);

        List<TagCtgyEntity> tagCtgy = new ArrayList<>();
        tagCtgy.add(tagCtgyEntity1);
        tagCtgy.add(tagCtgyEntity2);
        tagCtgy.add(tagCtgyEntity3);

        List<TagGrpEntity> tagGrp = new ArrayList<>();
        TagGrpEntity tagGrpEntity1 = new TagGrpEntity();
        tagGrpEntity1.setGrpSeq("grp_0");
        tagGrpEntity1.setTagCtgys(tagCtgy);

        tagGrp.add(tagGrpEntity1);

        ObjectMapper mapper = new ObjectMapper();
        String resutStr = mapper.writeValueAsString(tagGrp);

        System.out.println(resutStr);


        TagConditionBase result = TagGrpToCondition.getCondition(tagGrp);
        System.out.println();
    }

}