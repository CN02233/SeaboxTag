package com.seabox.tagsys.usertags.mybatis.dao;


import com.seabox.tagsys.usertags.entity.TagCategoryLastLevel;
import com.seabox.tagsys.usertags.entity.UserTagFavorite;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author Changhua, Wu
 *         Created on: 1/23/16,10:37 AM
 */

@CacheNamespace(readWrite = false)
public interface TagFavoriteDao extends TagChangeAuditDao {

    String  SELECT_SQL_FOR_TAG_FAVORITE = "select  " +
            "f.user_id        as user_id, " +
            "f.tag_ctgy_id    as tag_ctgy_id " // +
            //"f.created_ts     as created_ts, " +
            //"f.updated_ts     as updated_ts  "
            ;


    String TABLE_AND_ALIAS = " h62_user_tag_favorite f ";
    String FROM_TABLE = " from " + TABLE_AND_ALIAS;


    @Many
    @Select({
            SELECT_SQL_FOR_TAG_FAVORITE,
            FROM_TABLE, ",", TagCategoryDao.TABLE_AND_ALIAS,
            " where ",
            TagCategoryDao.EXCLUDE_CONDITION_SQL,
            " and f.tag_ctgy_id = c.tag_ctgy_id ",
    })
    List<UserTagFavorite>     listAll();


    /**
     *
     * @param userId
     * @return  list of Int, ID list of user's Favorite's tagCategory
     */
    @Many
    @Select({
            SELECT_SQL_FOR_TAG_FAVORITE,
            FROM_TABLE, ",", TagCategoryDao.TABLE_AND_ALIAS,
            " where ",
            TagCategoryDao.EXCLUDE_CONDITION_SQL,
            " and c.tag_ctgy_id = f.tag_ctgy_id",
            " and f.user_id = #{userId} "
    } )
    List<Integer>       getFavoriteTagIDsByUser(@Param("userId") int userId);




    @One
    @Select({ "select count(1)   as user_count ",
            FROM_TABLE,
            " where ",
            " f.tag_ctgy_id = #{tagCategoryId}"
    })
    Integer       getNumOfUsersWhoLoveTag(@Param("tagCategoryId") int tagCategoryId);



    @Insert("insert into h62_user_tag_favorite (user_id, tag_ctgy_id) values (#{userId}, #{tagCategoryId})")
    @Options(useCache = false, flushCache = true)
    void          addOneTagFavoriteByUserAndTag(@Param("userId") int userId, @Param("tagCategoryId") int tagCategoryId);



    @Delete("delete  from h62_user_tag_favorite  where user_id = #{userId}")
    @Options(useCache = false, flushCache = true)
    void          removeAllMyFavoriteByUser(@Param("userId") int userId);


    @Delete("delete  from h62_user_tag_favorite  where user_id = #{userId} and tag_ctgy_id = #{tagCategoryId}")
    @Options(useCache = false, flushCache = true)
    void          removeOneTagFavoriteByUserAndTag(@Param("userId") int userId, @Param("tagCategoryId") int tagCategoryId);


    @One
    @Select("select  " +
            "count(1)   as result " +
            " from h62_user_tag_favorite f where f.user_id = #{userId} and f.tag_ctgy_id = #{tagCategoryId}" )
    boolean          isTagFavoriteByUserAndTag(@Param("userId") int userId, @Param("tagCategoryId") int tagCategoryId);



    /**
     *
     * @param userId
     * @return  list of Tag Category info, Object list of user's Favorite's tagCategory
     */
    @Many
    @Select({
            TagCategoryDao.SELECT_SQL_FOR_CATEGORY,
            FROM_TABLE, ",", TagCategoryDao.TABLE_AND_ALIAS,
            " where ",
            TagCategoryDao.EXCLUDE_CONDITION_SQL,
            " and c.tag_ctgy_id = f.tag_ctgy_id",
            " and f.user_id = #{userId} "
    } )
    List<TagCategoryLastLevel>        getFavoriteTagsByUser(@Param("userId") int userId);



    /**
     *
     * @param userId
     * @return  list of Tag Category info, Object list of user's Favorite's tagCategory
     */
    @Many
    @Select({
            TagCategoryDao.SELECT_SQL_FOR_CATEGORY,
            FROM_TABLE, ",", TagCategoryDao.TABLE_AND_ALIAS,
            " where ",
            TagCategoryDao.EXCLUDE_CONDITION_SQL,
            " and c.tag_ctgy_id = f.tag_ctgy_id",
            " and f.user_id = #{userId} ",
            " and ( c.tag_ctgy_id like #{searchFilter} or c.tag_ctgy_nm like #{searchFilter} or c.tag_desc like #{searchFilter} ) "
    } )
    @Options(useCache = false)
    List<TagCategoryLastLevel>        getFavoriteTagsByUserWithSearchFilter(@Param("userId") int userId, @Param("searchFilter") String searchFilter);

}
