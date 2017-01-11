package com.seabox.tagsys.persongroup.utils;

import com.seabox.test.base.BaseTestClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;
import com.seabox.tagsys.persongroup.utils.TagHelperUtil;

import java.util.List;

/**
 * Created by pc on 2016/12/15.
 */
public class TagHelperUtilTest extends BaseTestClass{
    @Test
    public void checkUserAllTag() throws Exception {

        TagHelperUtil tagHelper = TagHelperUtil.getInstance(jedisPool);

        List<TagHelperBean> result = tagHelper.checkUserAllTag(new Long(675));

        for(TagHelperBean tagBean :result){
            System.out.println(tagBean.getTagId()+"#"+tagBean.getUserCount());
        }
        tagHelper.sortTagByUsers(result,TagHelperUtil.TagOrder.ORDER_ASC);
        System.out.println("开始排序..........");
        for(TagHelperBean tagBean :result){
            System.out.println(tagBean.getTagId()+"#"+tagBean.getUserCount());
        }
    }

    @Autowired
    private JedisPool jedisPool;

    @Test
    public void getAllTagFromRedis() throws Exception {
        TagHelperUtil tagHelperUtil = TagHelperUtil.getInstance(this.jedisPool);

//        TagHelperUtil TagHelperUtil = TagHelperUtil.getInstance(jedisPool);
        tagHelperUtil.getAllTagFromRedis(TagHelperUtil.ByteToBitSet.NOT_NEED);
    }

}