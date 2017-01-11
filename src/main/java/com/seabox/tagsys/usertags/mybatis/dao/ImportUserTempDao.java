package com.seabox.tagsys.usertags.mybatis.dao;

import com.seabox.tagsys.usertags.entity.ImportFileUserTemp;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author Changhua, Wu
 *         Created on: 5/5/16,3:03 PM
 */
public interface ImportUserTempDao {


    @Update("truncate table h62_import_users_temp")
    void cleanup();


    // (mobile1), (mobile2), (mobile3) ...
    @Insert("insert into h62_import_users_temp (mobile) values  ${valueLists} ON DUPLICATE KEY UPDATE count=count+1")
    void addTempUserMobiles(@Param("valueLists") String valueLists);


    @One
    @Select("select count(1) from h62_import_users_temp")
    Long getTotalCount();

    @Many
    @Select("select * from h62_import_users_temp")
    List<ImportFileUserTemp> listAll();


}
