package com.seabox.tagsys.usertags.mybatis.dao;

import com.seabox.tagsys.usertags.entity.TagInfo;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author Changhua, Wu
 *         Created on: 1/23/16,10:36 AM
 */

@CacheNamespace(readWrite = false)
public interface TagInfoDao extends TagChangeAuditDao {

    String  SELECT_SQL_FOR_TAG_INFO = "select  " +
            "t.tag_id         as tag_id, " +
            "t.tag_nm         as tag_nm, " +
            "t.tag_desc       as tag_desc, " +
            "t.tag_type_cd    as tag_type_cd, " +
            "t.tag_ctgy_id    as tag_ctgy_id, " +
            "t.enabled_dt     as enabled_dt, " +
            "t.disabled_dt    as disabled_dt, " +
            "t.active_ind     as active_ind, " +
            "t.unknown_ind    as unknown_ind" //+
            //"t.created_ts     as created_ts, " +
            //"t.updated_ts     as updated_ts  "
            ;

    String TABLE_AND_ALIAS = " h50_tag_info t ";
    String FROM_TABLE = " from " + TABLE_AND_ALIAS;

    String EXCLUDE_CONDITION_SQL =
            " (" +
            " t.active_ind=1 " +
            " and (t.enabled_dt is null or TIMESTAMPDIFF(MINUTE, now(), t.enabled_dt) <0) " +
            " and (t.disabled_dt is null or TIMESTAMPDIFF(MINUTE, now(), t.disabled_dt) >0) " +
            ") ";

    @Many
    @Select({SELECT_SQL_FOR_TAG_INFO,
            FROM_TABLE,
            " where",
            EXCLUDE_CONDITION_SQL
            })
    List<TagInfo>     listAll();



    @One
    @Select({SELECT_SQL_FOR_TAG_INFO,
            FROM_TABLE,
            " where",
            EXCLUDE_CONDITION_SQL,
            " and t.tag_id = #{id}"
            } )
    TagInfo       getById(int id);


    @Many
    @Select({SELECT_SQL_FOR_TAG_INFO,
            FROM_TABLE,
            " where",
            EXCLUDE_CONDITION_SQL,
            " and t.tag_nm like #{nameLike}"
    } )
    List<TagInfo>        findAllByName(String nameLike);


    @One
    @Select({SELECT_SQL_FOR_TAG_INFO,
            FROM_TABLE,
            " where",
            EXCLUDE_CONDITION_SQL,
            " and t.tag_ctgy_id = #{categoryId} and t.unknown_ind = 1"
    })
    TagInfo     findUnknownTagByCategory(int categoryId);



    @Many
    @Select({SELECT_SQL_FOR_TAG_INFO,
            FROM_TABLE,
            " where",
            EXCLUDE_CONDITION_SQL,
            " and  t.tag_ctgy_id = #{categoryId}"
    } )
    List<TagInfo>     findAllTagsByCategory(int categoryId);




}
