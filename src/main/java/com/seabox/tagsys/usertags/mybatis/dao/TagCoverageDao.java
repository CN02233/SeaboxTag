package com.seabox.tagsys.usertags.mybatis.dao;

import com.seabox.tagsys.usertags.entity.TagCoverage;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author Changhua, Wu
 *         Created on: 1/27/16,1:53 PM
 */
@CacheNamespace(readWrite = false)
public interface TagCoverageDao  extends TagChangeAuditDao {

    String  SELECT_SQL_FOR_COVERAGE = "select  " +
            "s.tag_id        as tag_id, " +
            "s.tag_ctgy_id   as tag_ctgy_id, " +
            "s.indv_sum      as indv_sum, " +
            "s.indv_pcnt     as indv_pcnt " // +
           // "s.created_ts    as created_ts, " +
           // "s.updated_ts    as updated_ts  "
            ;


    @Many
    @Select(SELECT_SQL_FOR_COVERAGE +
            " from h62_tag_indv_sum s" )
    List<TagCoverage> listAll();


    @Many
    @Select(SELECT_SQL_FOR_COVERAGE +
            " from h62_tag_indv_sum s where s.tag_ctgy_id = #{categoryId}" )
    List<TagCoverage> getCoveragesByCategory(int categoryId);


    @One
    @Select(SELECT_SQL_FOR_COVERAGE +
            " from h62_tag_indv_sum s where s.tag_id = #{tagId}" )
    TagCoverage getCoverageByTag(int tagId);

    //TODO:  ensure/restrict DB side: For a Category, always have one(only one) Tag with unknown_ind= true.
    @One
    @Select(SELECT_SQL_FOR_COVERAGE +
            " from h62_tag_indv_sum s, h50_tag_info t where  t.tag_ctgy_id= #{categoryId} and t.unknown_ind='1' and s.tag_id = t.tag_id" )
    TagCoverage getUnKnownCoverageByCategory(int categoryId);

}
