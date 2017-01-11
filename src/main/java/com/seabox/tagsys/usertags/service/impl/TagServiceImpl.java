package com.seabox.tagsys.usertags.service.impl;

import com.seabox.tagsys.sys.entity.User;
import com.seabox.tagsys.usertags.entity.*;
import com.seabox.tagsys.usertags.mybatis.dao.*;
import com.seabox.tagsys.usertags.service.CacheService;
import com.seabox.tagsys.usertags.service.TagService;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Changhua, Wu
 *         Created on: 1/22/16,11:54 AM
 */
@Service
public class TagServiceImpl implements TagService {

    /**
     * user  getXXX api for  all injected Dao, with Safe Cache update audit check.
     */
    @Autowired
    TagInfoDao   tagInfoDao;

    @Autowired
    TagCategoryDao  tagCategoryDao;

    @Autowired
    TagFavoriteDao  tagFavoriteDao;

    @Autowired
    TagCoverageDao     tagCoverageDao;


    @Autowired
    CacheService       cacheService;

    private long       auditVersion = 0L;

    public final int COVERAGE_PERCENT_VOLUME = 100;


    private static final Logger logger = LoggerFactory.getLogger(TagServiceImpl.class);


    @Override
    public int getCurrentUserId() {
        User currentUser = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
        return currentUser.getUser_id();
    }


    @Override
    public void  openSafeCacheUpdateTransaction() {

        Long version = tagInfoDao.getAuditVersion(); // for simple, use only one version for all related Tag tables

        if( version!=null
                && !version.equals(auditVersion )) {

            tagCategoryDao.forceFlushCache();
            tagCoverageDao.forceFlushCache();
            tagFavoriteDao.forceFlushCache();
            tagInfoDao.forceFlushCache();

            logger.info("####force to Flush Cache, new version={}, auditVersion={}", version, auditVersion);
            auditVersion = version;
        }

    }


    @Override
    public List<TagCategoryBase> getAllTagCategories() {

        List<TagCategoryBase> results = tagCategoryDao.listAll();

        setChildRecursive(results, true);

        return results;
    }



    private void setCoverageForTag(TagInfo tag) {
        TagCoverage tagCoverage = tagCoverageDao.getCoverageByTag(tag.getTag_id());
        if(tagCoverage != null) {
            tag.setCovered_count( tagCoverage.getIndv_sum() );
            tag.setCovered_rate( tagCoverage.getIndv_pcnt() );
        } else {
            tag.setCovered_count( null );
            tag.setCovered_rate( null );
            logger.warn("No Coverage for Tag(id:{}, name:{}), force to set null Coverage", tag.getTag_id(), tag.getTag_nm());
        }

    }


    private void setCoverageAndFavorInfoForTagCategory(TagCategoryLastLevel category) {

        TagCoverage tagCoverage = tagCoverageDao.getUnKnownCoverageByCategory(category.getTag_ctgy_id());

        boolean favor = tagFavoriteDao.isTagFavoriteByUserAndTag(getCurrentUserId(), category.getTag_ctgy_id());
        category.setFavorite_by_user( favor );

        //TODO:  align Coverage point in DB, use  1, or  100 present for 100%
        if(tagCoverage != null) {
            double allOthersCover = COVERAGE_PERCENT_VOLUME - tagCoverage.getIndv_pcnt() ; // 100% - Unknown-Rate
            BigDecimal bg = new BigDecimal(allOthersCover);
            double allOthersCover2 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            category.setCovered_rate( allOthersCover2 );
        } else {
            category.setCovered_rate( null ); // force to set to null, cause GUI display "undefined"
            logger.warn("No Coverage for Unknown Tag by tagCategory(id:{}, name:{}), force to set null Coverage", category.getTag_ctgy_id(), category.getTag_ctgy_nm());
        }

    }


    private void setChildRecursive(TagCategoryBase  tagCategory, boolean getCoverRate) {

        if( tagCategory instanceof TagCategoryLastLevel) {
            TagCategoryLastLevel categoryLastLevel = (TagCategoryLastLevel)tagCategory;
            List<TagInfo> tags = tagInfoDao.findAllTagsByCategory(categoryLastLevel.getTag_ctgy_id());
            categoryLastLevel.setChildren( tags );

            for(TagInfo tag: tags) {
                tag.setParent( categoryLastLevel );


                if(getCoverRate) {
                    setCoverageForTag(tag);
                }
            }
            if(getCoverRate ) {
                setCoverageAndFavorInfoForTagCategory(categoryLastLevel);
            }

        } else { //it's  TagCategoryHighLevel
            List<TagCategoryBase> children = tagCategoryDao.findAllSubGroupsByCategory(tagCategory.getTag_ctgy_id());
            tagCategory.setChildren(children);

            for(TagCategoryBase child: children) {
                 child.setParent( tagCategory );

                setChildRecursive(child, getCoverRate);
            }
        }
    }


    private void setChildRecursive(Collection<? extends TagCategoryBase> tagCategories, boolean getCoverRate) {
        for(TagCategoryBase tagCategory: tagCategories) {
            setChildRecursive(tagCategory, getCoverRate);
        }
    }


    private int calculateSubCategoriesDepth(TagCategoryBase category) {

        if(category instanceof TagCategoryLastLevel) {
            return 1;
        } else {

            if( category.getChildren() == null || category.getChildren().isEmpty() ) {
                return 1;
            } else {
                TagCategoryHighLevel categoryHighLevel = (TagCategoryHighLevel) category;
                TagCategoryBase child0 = categoryHighLevel.getChildren().get(0);
                return 1 + calculateSubCategoriesDepth(child0);
            }
        }

    }



    @Override
    public List<TagCategoryHighLevel> getLevelOneTagCategories(boolean recursive, boolean getCoverRate) {

        List<TagCategoryHighLevel> results = tagCategoryDao.findAllLevelOneTagCategory();
        if(recursive) {
            setChildRecursive(results, getCoverRate);
            for(TagCategoryHighLevel levelOne: results) {
                int  depth = calculateSubCategoriesDepth(levelOne);
                levelOne.setSub_ctgy_depth(depth);
            }
        }

        return results;
    }


    @Override
    public List<TagCategoryBase> getSubCategoriesByParentId(int categoryId, boolean recursive, boolean getCoverRate, String searchFilter) {

        List<TagCategoryBase> results;
        if( (searchFilter != null) && (!searchFilter.isEmpty()) ) {
            results = tagCategoryDao.findAllSubGroupsByCategoryWithSearchFilter(categoryId, searchFilter);
        } else {
            results = tagCategoryDao.findAllSubGroupsByCategory(categoryId);
        }

        if(recursive) {
            setChildRecursive(results, getCoverRate);
        }

        return results;
    }

    @Override
    public List<TagCategoryLastLevel> getLastLevelCategoriesByParentId(int categoryId, boolean getCoverRate, String searchFilter) {

        List<TagCategoryBase> rowResults = getSubCategoriesByParentId(categoryId, true, getCoverRate, searchFilter);

        List<TagCategoryLastLevel> results = new ArrayList<>();
        for(TagCategoryBase category: rowResults) {
            grepAllLastLevelTagCategories(results, category);
        }

        return results;
    }


    private void  grepAllLastLevelTagCategories(Collection<TagCategoryLastLevel> results, TagCategoryBase category) {
        if(category instanceof TagCategoryLastLevel) {
            results.add( (TagCategoryLastLevel)category);
        }else if(category instanceof TagCategoryHighLevel){
            TagCategoryHighLevel catVirt = (TagCategoryHighLevel)category;
            for(TagCategoryBase child: catVirt.getChildren()) {
                grepAllLastLevelTagCategories(results, child);
            }
        }
    }

    @Override
    public List<TagInfo> getTagItemsByCategoryId(int categoryId, boolean getCoverRate) {
        List<TagInfo> results = tagInfoDao.findAllTagsByCategory(categoryId);
        if(getCoverRate) {
            for(TagInfo tag: results) {
                if(getCoverRate) {
                    setCoverageForTag(tag);
                }
            }
        }

        return results;
    }



    @Override
    public List<TagCategoryLastLevel> getUserFavoriteTagsByUserId(int userId, boolean recursive, boolean getCoverRate, String searchFilter) {

        List<TagCategoryLastLevel>  results;
        if( (searchFilter != null) && (!searchFilter.isEmpty()) ) {
            results = tagFavoriteDao.getFavoriteTagsByUserWithSearchFilter(userId, searchFilter);
        } else {
            results = tagFavoriteDao.getFavoriteTagsByUser(userId);
        }

        if(getCoverRate) {
            for(TagCategoryLastLevel tagCategory: results) {
                setCoverageAndFavorInfoForTagCategory( tagCategory );
            }
        }

        if(recursive) {
            setChildRecursive(results, getCoverRate);
        }
        return results;
    }



    @Override
    public TagInfo getTagById(int tagId, boolean getCoverRate) {
        TagInfo result = tagInfoDao.getById(tagId);
        if(getCoverRate) {
            setCoverageForTag( result );
        }
        return result;
    }


    @Override
    public TagCategoryBase getTagCategoryById(int categoryId, boolean getCoverRate) {
        TagCategoryBase result = tagCategoryDao.getById(categoryId);
        if(getCoverRate) {
            setChildRecursive(result, getCoverRate);
        }
        return result;
    }


    @Override
    @Transactional
    public void  addOneTagFavoriteByUserAndTag(int userId, int tagCategoryId) {
        tagFavoriteDao.addOneTagFavoriteByUserAndTag(userId, tagCategoryId);
    }

    @Override
    @Transactional
    public void  removeAllMyFavoriteByUser(int userId) {
        tagFavoriteDao.removeAllMyFavoriteByUser(userId);
    }

    @Override
    @Transactional
    public void  removeOneTagFavoriteByUserAndTag(int userId, int tagCategoryId) {
        tagFavoriteDao.removeOneTagFavoriteByUserAndTag(userId, tagCategoryId);
    }


}
