package com.seabox.tagsys.usertags.mybatis.dao;

import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

/**
 * @author Changhua, Wu
 *         Created on: 2/2/16,11:13 AM
 */
public interface TagChangeAuditDao {

    @One
    @Select("select  " +
            "max(version)   as version " +
            " from h62_tag_change_audit " )
    @Options(useCache = false)
    Long       getAuditVersion();


    @One
    @Select("select 1")
    @Options(useCache = false, flushCache = true)
    Integer       forceFlushCache();


}
