package com.seabox.tagsys.persongroup.mapping.dao;

import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * Created by pc on 2016/12/7.
 */
@CacheNamespace(readWrite = false)
public interface PortraitDao {

    static final String TABLE_PUBLIC="h62_campaign_person_";

    @Many
    @Select("select " +
            "camp_id screeningId," +
            "user_nm userNm," +
            "user_sex userSex," +
            "user_age userAge," +
            "has_married hasMarried " +
            "from "+TABLE_PUBLIC+"#{screening_id}")
    List<Map<String,Object>> getDataByCampId(@Param("screening_id")String screeningId);

}
