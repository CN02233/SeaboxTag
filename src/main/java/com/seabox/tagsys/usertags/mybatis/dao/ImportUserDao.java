package com.seabox.tagsys.usertags.mybatis.dao;

import com.seabox.tagsys.usertags.entity.ImportUser;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author Changhua, Wu
 *         Created on: 5/5/16,1:32 PM
 */
@CacheNamespace(readWrite = false)
public interface ImportUserDao {


/*
alter table  hdmp.h62_import_users add fid_1 tinyint;
alter table  hdmp.h62_import_users add index fid_1_idx (fid_1);
 */
    @Update("alter table  h62_import_users add fid_${fid} tinyint default 0")
    void createColumnForImportedFile(@Param("fid") String fid);

    @Update("alter table  h62_import_users add index idx_fid_${fid} ( fid_${fid} )")
    void createIndexForImportedFile(@Param("fid") String fid);

    @One
    @Select("select uid from h62_import_users where mobile = #{mobile}")
    Long getUserIdByMobile(@Param("mobile") String mobile);

    @One
    @Select("select count(1) from h62_import_users where uid = #{uid}")
    Integer existsUserId(@Param("uid") long uid);



    /*

insert into h62_import_users (mobile)

select FU.mobile as mobile from h62_import_users_temp FU
 where FU.fid=#{fid} and not exists (select mobile from h62_import_users U where FU.mobile= U.mobile);

-- or

select FU.mobile as mobile from h62_import_users_temp FU
left join h62_import_users U on FU.mobile= U.mobile where U.mobile is NULL;

     */

    @Insert("insert into h62_import_users (mobile) \n" +
            "  select FU.mobile as mobile from h62_import_users_temp FU \n" +
            "  where not exists (select mobile from h62_import_users U where FU.mobile= U.mobile)")
    void addAllUsersFromTempImport();



    @Update("update h62_import_users U set U.fid_${fid} = 1, U.updated_ts=CURRENT_TIMESTAMP where exists(select mobile from h62_import_users_temp FU  where FU.mobile = U.mobile ) ")
    @Options(useCache = false, flushCache = true)
    void  updateUsersToImportFile(@Param("fid") String fid);


    @Many
    @Select("select * from h62_import_users order by uid")
    List<ImportUser> listAll();



    @Many
    @Select("select mobile from h62_import_users where fid_${fid} = 1 ")
    List<String>  getMobilesByOneImportFile(@Param("fid") String fid);


    @Many
    @Select("select uid from h62_import_users where fid_${fid} = 1 ")
    List<String>  getUserIdsByOneImportFile(@Param("fid") String fid);


    @Many
    @Select("select mobile from h62_import_users where $fileConditions ")
    List<String>  getMobilesByFileConditions(@Param("fileConditions") String fileConditions);


    @Many
    @Select("select uid from h62_import_users where $fileConditions ")
    List<String>  getUserIdsByFileConditions(@Param("fileConditions") String fileConditions);

}
