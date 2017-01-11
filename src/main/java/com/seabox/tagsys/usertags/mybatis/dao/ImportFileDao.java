package com.seabox.tagsys.usertags.mybatis.dao;

import com.seabox.tagsys.usertags.entity.ImportFile;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author Changhua, Wu
 *         Created on: 5/5/16,11:31 AM
 */
@CacheNamespace(readWrite = false)
public interface ImportFileDao {


    String SELECT_FROM_IMPORT_FILE = " F.fid as fid, " +
            " F.filename as filename," +
            " F.alias as alias," +
            " F.deleted as deleted," +
            " F.user_id as user_id," +
            " F.share as share," +
            " F.user_count as user_count," +
            " F.comments as comments," +
            " F.created_ts as created_ts," +
            " F.updated_ts as updated_ts ";



    @Update("update h62_import_files set alias = #{alias}, updated_ts=CURRENT_TIMESTAMP where fid= #{fid}")
    @Options(useCache = false, flushCache = true)
    void rename(@Param("fid") String fid, @Param("alias") String alias);

    @Insert("insert into h62_import_files (filename, share, user_id, alias, content) values (#{filename}, #{share}, #{user_id}, #{alias}, #{content})")
    @Options(useCache = false, flushCache = true, keyColumn = "fid")
    void  saveImportFile(@Param("user_id") int user_id,
                         @Param("share") int share,
                         @Param("filename") String filename,
                         @Param("alias") String alias,
                         @Param("content") byte[] content);



    @Update("update h62_import_files  set user_count = (select count(1) as user_count from h62_import_users where fid_${fid} = 1), updated_ts=CURRENT_TIMESTAMP where fid= #{fid}")
    @Options(useCache = false, flushCache = true)
    void updateImportUserCount(@Param("fid") String fid);



    @Update("update h62_import_files  set deleted = 1 , alias = concat(alias, '_deleted'), updated_ts=CURRENT_TIMESTAMP  where fid= #{fid} and deleted<>1")
    @Options(useCache = false, flushCache = true)
    void deleteFile(@Param("fid") String fid);

    @One
    @Select("select " + SELECT_FROM_IMPORT_FILE + " from h62_import_files F where fid=#{fid} and deleted<>1")
    ImportFile getById(@Param("fid") String fid);


    @One
    @Select("select " + SELECT_FROM_IMPORT_FILE + " from h62_import_files F where alias=#{alias} and deleted<>1")
    ImportFile getByAlias(@Param("alias") String alias);

    @One
    @Select("select fid from h62_import_files F where alias=#{alias} and deleted<>1")
    String getIdByAlias(@Param("alias") String alias);



    @Many
    @Select("select " + SELECT_FROM_IMPORT_FILE + " from h62_import_files F where deleted<>1 order by fid")
    List<ImportFile>  listAll();


    /**
     *
     * @return  this user created, or  Public imported files
     */
    @Many
    @Select("select " + SELECT_FROM_IMPORT_FILE
            + " , U.user_real_nm as owner,"
            + " case when F.user_id=#{user_id} then 1 else 0 end as myCreated "
            + " from h62_import_files F left join h62_sys_user U on F.user_id=U.user_id where F.deleted<>1 and (F.user_id = #{user_id} or F.share=1) order by F.fid")
    List<ImportFile>  listAllByUser(@Param("user_id") int user_id);


}
