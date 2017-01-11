package com.seabox.tagsys.usertags.controller;

import com.seabox.tagsys.base.controller.BaseController;
import com.seabox.tagsys.sys.entity.PageBean;
import com.seabox.tagsys.usertags.entity.TagCategoryBase;
import com.seabox.tagsys.usertags.entity.TagCategoryHighLevel;
import com.seabox.tagsys.usertags.entity.TagCategoryLastLevel;
import com.seabox.tagsys.usertags.entity.TagInfo;
import com.seabox.tagsys.usertags.service.TagService;
import com.seabox.tagsys.usertags.utils.BeanSorter;
import com.seabox.tagsys.usertags.utils.UserTagUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Changhua, Wu
 *         Created on: 1/22/16,11:18 AM
 */

@Controller
@RequestMapping("usertags/tag")
public class TagController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(TagController.class);

    @Autowired
    private TagService tagService;


    public static final String DEFAULT_PAGE_ROW_SIZE="9";
    public static final String DEFAULT_CURRENT_PAGE="1";

    private TagService  getTagServiceWithSafeCacheUpdate() {
        tagService.openSafeCacheUpdateTransaction();
        return tagService;
    }

    /**
     * Description 标签管理页面
     *
     * @param
     * @return String
     * @throws
     */
    @RequestMapping("main")
    public String tagMarket(HttpServletRequest request) {
        String strCurrPage = request.getParameter("currPage");
        int currPage = 1;
        if(strCurrPage != null){
            currPage = Integer.parseInt(strCurrPage);
        }
        request.setAttribute("currPage", currPage);

        return "usertags/tag/tagMain";
    }



    @RequestMapping("tagListFavor")
    public String tagListFavor(HttpServletRequest request) {
        return "usertags/tag/userFavoriteTagsView";
    }

    @RequestMapping("tagViewByCat")
    public String tagViewByCat(HttpServletRequest request) {
        Map<String,Object> paramMap = initRequestParam(request);
        return  "usertags/tag/tagViewByCat";
    }



    @RequestMapping("category/list-all")
    @ResponseBody
    public String getTagListByPage(@RequestParam(required = false, defaultValue=DEFAULT_CURRENT_PAGE) int currPage,
                                   @RequestParam(required = false, defaultValue=DEFAULT_PAGE_ROW_SIZE) int pageSize) {

        List<TagCategoryBase> results = getTagServiceWithSafeCacheUpdate().getAllTagCategories();

        PageBean<TagCategoryBase> bean = new PageBean<>();
        bean.setCurrPage(currPage);
        bean.setPageSize(pageSize);
        bean.setTotalNum(1);

        bean.setList(results);
        String json = UserTagUtils.objectToJson(bean);
        return json;
    }


    @RequestMapping(value="category/list-tops")
    @ResponseBody
    public String getLevelOneTagCategories() {

        List<TagCategoryHighLevel> results = getTagServiceWithSafeCacheUpdate().getLevelOneTagCategories(true, false);

        String json = UserTagUtils.objectToJson(results);
        return json;
    }


    @RequestMapping(value="category/get-subs/{categoryId}")
    @ResponseBody
    public String getSubCategories(@PathVariable int categoryId,
                                   @RequestParam(required = false) Integer focus_ctgy_id,
                                   @RequestParam(required = false, defaultValue=DEFAULT_CURRENT_PAGE) int currPage,
                                   @RequestParam(required = false, defaultValue=DEFAULT_PAGE_ROW_SIZE) int pageSize,
                                   @RequestParam(required = false) String search_context,
                                   @RequestParam(required = false) String order_id,
                                   @RequestParam(required = false) String order_type
    ) {
        if( (search_context != null) && (!search_context.isEmpty()) ) {
            search_context = "%" + search_context + "%";
        }

        //TODO:  only  care about last level categories in View List
        List<TagCategoryLastLevel> results = getTagServiceWithSafeCacheUpdate().getLastLevelCategoriesByParentId(categoryId, true, search_context);

        if(order_id!=null && !order_id.isEmpty()) {
            boolean isAsc = true;
            if(order_type!=null && order_type.equals("desc")) {
                isAsc = false;
            }
            BeanSorter<TagCategoryLastLevel> tagCategorySorter = BeanSorter.createASorter(order_id, isAsc, TagCategoryLastLevel.class);
            if(tagCategorySorter != null) {
                Collections.sort(results, tagCategorySorter);
            }


        }
        String json = renderResultListsByPage(currPage, pageSize, results);
        return json;
    }

    @RequestMapping(value="category/get-subs/{categoryId}/{focusOnSubId}")
    @ResponseBody
    public String getSubCategoriesWithFocus(@PathVariable int categoryId,
                                            @PathVariable int focusOnSubId,
                                            @RequestParam(required = false, defaultValue=DEFAULT_CURRENT_PAGE) int currPage,
                                            @RequestParam(required = false, defaultValue=DEFAULT_PAGE_ROW_SIZE) int pageSize
    ) {

        String searchFilter = null;
        List<TagCategoryLastLevel> results = getTagServiceWithSafeCacheUpdate().getLastLevelCategoriesByParentId(categoryId, true, searchFilter);

        String json = renderResultListsByPage(currPage, pageSize, results);
        return json;
    }

    @RequestMapping(value="category/get-tags/{categoryId}")
    @ResponseBody
    public String getTagsForCategory(@PathVariable int categoryId,
                                     @RequestParam(required = false, defaultValue=DEFAULT_CURRENT_PAGE) int currPage,
                                     @RequestParam(required = false, defaultValue=DEFAULT_PAGE_ROW_SIZE) int pageSize
                                     ) {

        List<TagInfo> results = getTagServiceWithSafeCacheUpdate().getTagItemsByCategoryId(categoryId, true);

        String json = UserTagUtils.objectToJson(results);
        return json;
    }


    @RequestMapping(value="favors/list")
    @ResponseBody
    public String getUserFavoriteTags(@RequestParam(required = false, defaultValue=DEFAULT_CURRENT_PAGE) int currPage,
                                      @RequestParam(required = false, defaultValue=DEFAULT_PAGE_ROW_SIZE) int pageSize,
                                      @RequestParam(required = false) String search_context,
                                      @RequestParam(required = false) String order_id,
                                      @RequestParam(required = false) String order_type) {

        // all records are already cached, so perform search/ordering within resultSets.

        if( (search_context != null) && (!search_context.isEmpty()) ) {
            try {
                search_context =  java.net.URLDecoder.decode(search_context,"UTF-8");
            } catch (UnsupportedEncodingException e) {
                logger.error("####getUserFavoriteTags(), unsupported Encoding, details:", e);
            }
            logger.info("####getUserFavoriteTags(), search_context={}", search_context);
            search_context = "%" + search_context + "%";
        }
        List<TagCategoryLastLevel> results = getTagServiceWithSafeCacheUpdate().getUserFavoriteTagsByUserId( tagService.getCurrentUserId() , true, true, search_context);


        if(order_id!=null && !order_id.isEmpty()) {
            boolean isAsc = true;
            if(order_type!=null && order_type.equals("desc")) {
                isAsc = false;
            }
            BeanSorter<TagCategoryLastLevel> tagCategorySorter = BeanSorter.createASorter(order_id, isAsc, TagCategoryLastLevel.class);
            if(tagCategorySorter != null) {
                Collections.sort(results, tagCategorySorter);
            }

        }

        String json = renderResultListsByPage(currPage, pageSize, results);
        return json;
    }


    public static   <E> String renderResultListsByPage(int currPage,
                                                int pageSize,
                                                List<E> cachedResultLists) {

        Integer startCurr = (currPage - 1) * pageSize;

        PageBean<E> bean = new PageBean<E>();
        bean.setCurrPage(currPage);
        bean.setPageSize(pageSize);
        bean.setTotalNum( cachedResultLists.size() );

        int toIndex = startCurr + pageSize; //toIndex  is Exclude  itself
        if(toIndex > cachedResultLists.size() ) {
            toIndex = cachedResultLists.size();
        }
        List<E>  pagedList = cachedResultLists.subList(startCurr,  toIndex);
        bean.setList(pagedList);

        String json = UserTagUtils.objectToJson(bean);
        return json;
    }



    @RequestMapping(value="favors/cleanup", method= RequestMethod.DELETE)
    @ResponseBody
    public String removeAllMyUserTagFavorite() {

        try {
            getTagServiceWithSafeCacheUpdate().removeAllMyFavoriteByUser(tagService.getCurrentUserId());
            return "success";

        } catch (Throwable e) {
            logger.error("Error on perform removeAllMyUserTagFavorite, details: ", e);

        }
        return "error";

    }


    @RequestMapping(value="favors/remove/{tag_ctgy_id}", method= RequestMethod.DELETE)
    @ResponseBody
    public String removeUserTagFavorite(@PathVariable int tag_ctgy_id) {

        try {
            getTagServiceWithSafeCacheUpdate().removeOneTagFavoriteByUserAndTag(tagService.getCurrentUserId(), tag_ctgy_id);
            return "success";
        } catch (Throwable e) {
            logger.error("Error on perform removeUserTagFavorite, details: ", e);
        }

        return "error";

    }

    @RequestMapping(value="favors/include/{tag_ctgy_id}")
    @ResponseBody
    public String addUserTagFavorite(@PathVariable int tag_ctgy_id) {

        try {
            getTagServiceWithSafeCacheUpdate().addOneTagFavoriteByUserAndTag(tagService.getCurrentUserId(), tag_ctgy_id);
            return "success";
        } catch (Throwable e) {
            logger.error("Error on perform addUserTagFavorite, details: ", e);
        }

        return "error";
    }


}
