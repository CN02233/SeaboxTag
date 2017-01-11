package com.seabox.tagsys.usertags.service;

import com.seabox.tagsys.usertags.entity.TagCategoryBase;
import com.seabox.tagsys.usertags.entity.TagCategoryHighLevel;
import com.seabox.tagsys.usertags.entity.TagCategoryLastLevel;
import com.seabox.tagsys.usertags.entity.TagInfo;

import java.util.List;

/**
 * @author Changhua, Wu
 *         Created on: 1/22/16,11:23 AM
 */
public interface TagService {

    int getCurrentUserId();

    List<TagCategoryBase> getAllTagCategories( );

    List<TagCategoryHighLevel> getLevelOneTagCategories(boolean recursive, boolean getCoverRate);


    List<TagCategoryBase> getSubCategoriesByParentId(int categoryId, boolean recursive, boolean getCoverRate, String searchFilter);


    List<TagCategoryLastLevel>   getLastLevelCategoriesByParentId(int categoryId, boolean getCoverRate, String searchFilter);


    List<TagInfo>           getTagItemsByCategoryId(int categoryId, boolean getCoverRate );

    List<TagCategoryLastLevel> getUserFavoriteTagsByUserId(int userId, boolean recursive, boolean getCoverRate, String searchFilter);

    TagInfo                 getTagById(int tagId, boolean getCoverRate );

    TagCategoryBase getTagCategoryById(int categoryId, boolean getCoverRate);


    void  addOneTagFavoriteByUserAndTag(int userId, int tagCategoryId);

    void  removeAllMyFavoriteByUser(int userId);

    void  removeOneTagFavoriteByUserAndTag(int userId, int tagCategoryId);

    void  openSafeCacheUpdateTransaction();

}
