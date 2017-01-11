package com.seabox.tagsys.persongroup.controller;

import com.seabox.tagsys.base.controller.BaseController;
import com.seabox.tagsys.persongroup.service.PortraitServiceImp;
import com.seabox.tagsys.persongroup.utils.TagHelperBean;
import com.seabox.tagsys.persongroup.utils.TagHelperUtil;
import com.seabox.tagsys.sys.entity.PageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pc on 2016/12/6.
 */

@Controller
@RequestMapping("person")
public class PortraitController extends BaseController{

    private final int TOP_N = 10;

    @Autowired
    private PortraitServiceImp portraitServiceImp;

    @RequestMapping("microPt/microPtMain")
    public String microPtMain(HttpServletRequest request){
        request.getSession().setAttribute("pageType","microPt");
        return mainPageUrl();

    }

    @RequestMapping("macroPt/macroPtMain")
    public String macroPtMain(HttpServletRequest request){
        request.getSession().setAttribute("pageType","macroPt");
        return mainPageUrl();
    }


    @RequestMapping("personPage")
    public String personPage(HttpServletRequest request,String screeningId,String screeningNm){
        request.setAttribute("screeningId",screeningId);
        request.setAttribute("screeningNm",screeningNm);
        return "persongroup/micAndMac/personPage";
    }

    @RequestMapping("macroPt/macroPtDetail")
    public String macroPtDetail(String screeningId,HttpServletRequest request){

        request.getSession().setAttribute("screeningId",screeningId);
        return "persongroup/macroPt/macroPtDetail";
    }

    private String mainPageUrl(){
        return "persongroup/micAndMac/ptMainPage";
    }

    @RequestMapping("macroPtDetail/{screeningId}")
    @ResponseBody
    public PageBean<Object> listUser4Screening(@PathVariable String screeningId,HttpServletRequest request){
        Map<String, Object> paramMap = initRequestParam(request);
        paramMap.put("screeningId",screeningId);

        PageBean<Object> pageResult = portraitServiceImp.listUser4Screening(paramMap);

        return pageResult;
    }

    @RequestMapping(value="macroPtDetail/listAllTag")
    @ResponseBody
    public List<Map<String, Object>> getUserAllTag(String userGuid,String userNm,HttpServletRequest request){
        List<Map<String, Object>> resultList = portraitServiceImp.getUserAllTag(userGuid);

        return resultList;
    }

    @RequestMapping(value="macroPtDetail/top10Tags")
    @ResponseBody
    public Map<String, Object> getTop10Tags(String screeningId, String topN){
        int topNInt = TOP_N;

        if(topN!=null)
            topNInt = new Integer(topN);

        List<TagHelperBean> allTagList = portraitServiceImp.getAllTags(TagHelperUtil.TagOrder.ORDER_DESC);

        List<Map<String, Object>> topN_tags = portraitServiceImp.getTopNTags(allTagList,topNInt);

        Map<String, List<Map<String, Object>>> screening_tags = portraitServiceImp.getTagsForScreeningOrder(screeningId, allTagList);

        Map<String,Object> resultMap = new HashMap<>();

        resultMap.put("topN_tags",topN_tags);
        resultMap.put("screening_tags",screening_tags);

        return resultMap;
    }

}
