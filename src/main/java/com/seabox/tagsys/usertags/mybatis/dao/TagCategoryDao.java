package com.seabox.tagsys.usertags.mybatis.dao;

import com.seabox.tagsys.usertags.entity.TagCategoryBase;
import com.seabox.tagsys.usertags.entity.TagCategoryHighLevel;
import com.seabox.tagsys.usertags.entity.TagCategoryLastLevel;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author Changhua, Wu
 *         Created on: 1/23/16,10:37 AM
 */

@CacheNamespace(readWrite = false)
public interface TagCategoryDao extends TagChangeAuditDao {


    String  SELECT_SQL_FOR_CATEGORY = "select  " +
            "c.tag_ctgy_id   as tag_ctgy_id, " +
            "c.tag_ctgy_nm   as tag_ctgy_nm, " +
            "c.tag_desc   as tag_desc, " +
            "c.tag_type_cd   as tag_type_cd, " +
            "case c.up_tag_ctgy_id  when '' then null end as up_tag_ctgy_id, " +
            "case c.have_tag_ind   when '' then 0  when null then 0  else c.have_tag_ind  end as have_tag_ind, " +
            "c.enabled_dt   as enabled_dt, " +
            "c.disabled_dt   as disabled_dt " // +
           // "c.created_ts   as created_ts, " +
           // "c.updated_ts   as updated_ts  "
            ;


    String TABLE_AND_ALIAS = " h50_tag_category_info c ";
    String FROM_TABLE = " from " + TABLE_AND_ALIAS;

    String EXCLUDE_CONDITION_SQL =
            "(" +
            " c.tag_ctgy_status=2 " +
            " and (c.enabled_dt is null or TIMESTAMPDIFF(MINUTE, now(), c.enabled_dt) <0) " +
            " and (c.disabled_dt is null or TIMESTAMPDIFF(MINUTE, now(), c.disabled_dt) >0) " +
            ")";

    @Many
    @Select({SELECT_SQL_FOR_CATEGORY,
            FROM_TABLE,
            "where ",
            EXCLUDE_CONDITION_SQL
    })
    @TypeDiscriminator(column="have_tag_ind", cases = {
            @Case(value = "0", type = TagCategoryHighLevel.class),
            @Case(value = "1", type = TagCategoryLastLevel.class)
    })
    List<TagCategoryBase>     listAll();



    @One
    @Select({SELECT_SQL_FOR_CATEGORY,
            FROM_TABLE,
            "where ",
            EXCLUDE_CONDITION_SQL,
            " and c.tag_ctgy_id=#{id}"
    } )
    @TypeDiscriminator(column="have_tag_ind", cases = {
            @Case(value = "0", type = TagCategoryHighLevel.class),
            @Case(value = "1", type = TagCategoryLastLevel.class)
    })
    TagCategoryBase getById(int id);


    @Many
    @Select({SELECT_SQL_FOR_CATEGORY,
            FROM_TABLE,
            "where ",
            EXCLUDE_CONDITION_SQL,
            " and c.tag_ctgy_nm like #{nameLike}"
    })
    @TypeDiscriminator(column="have_tag_ind", cases = {
            @Case(value = "0", type = TagCategoryHighLevel.class),
            @Case(value = "1", type = TagCategoryLastLevel.class)
    })
    List<TagCategoryBase>        findAllByName(String nameLike);





    @Many
    @Select({SELECT_SQL_FOR_CATEGORY,
            FROM_TABLE,
            "where ",
            EXCLUDE_CONDITION_SQL,
            " and c.up_tag_ctgy_id = #{categoryId} and c.tag_ctgy_id <> #{categoryId} "
    })
    @TypeDiscriminator(column="have_tag_ind", cases = {
            @Case(value = "0", type = TagCategoryHighLevel.class),
            @Case(value = "1", type = TagCategoryLastLevel.class)
    })
    List<TagCategoryBase>     findAllSubGroupsByCategory(int categoryId);


    @Many
    @Select({SELECT_SQL_FOR_CATEGORY,
            FROM_TABLE,
            "where ",
            EXCLUDE_CONDITION_SQL,
            " and c.up_tag_ctgy_id = #{categoryId} and c.tag_ctgy_id <> #{categoryId} ",
            " and ( c.tag_ctgy_id like #{searchFilter} or c.tag_ctgy_nm like #{searchFilter} or c.tag_desc like #{searchFilter} ) "
    })
    @TypeDiscriminator(column="have_tag_ind", cases = {
            @Case(value = "0", type = TagCategoryHighLevel.class),
            @Case(value = "1", type = TagCategoryLastLevel.class)
    })
    List<TagCategoryBase>     findAllSubGroupsByCategoryWithSearchFilter(@Param("categoryId") int categoryId, @Param("searchFilter") String searchFilter);


    @Many
    @Select({SELECT_SQL_FOR_CATEGORY,
            FROM_TABLE,
            "where ",
            EXCLUDE_CONDITION_SQL,
            " and c.up_tag_ctgy_id is null or c.up_tag_ctgy_id=''"
    })
    List<TagCategoryHighLevel> findAllLevelOneTagCategory();



}
