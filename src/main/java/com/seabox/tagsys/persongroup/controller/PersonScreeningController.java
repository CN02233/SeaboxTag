package com.seabox.tagsys.persongroup.controller;

import com.seabox.tagsys.base.controller.BaseController;
import com.seabox.tagsys.persongroup.service.PersonScreenServiceImp;
import com.seabox.tagsys.sys.entity.User;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by SongChaoqun on 2016/11/11.
 * 筛选人群
 */
@Controller
@RequestMapping("screen/sum")
public class PersonScreeningController extends BaseController{

    Logger logger = LoggerFactory.getLogger(PersonScreeningController.class);

    @Autowired
    private PersonScreenServiceImp personScreenService;

    @RequestMapping("lstSnCdion")
    public String listScreenCondition(HttpServletRequest request){

        return "persongroup/sum/lstSnCdion";
    }

    @RequestMapping("listMainData")
    @ResponseBody
    public String listMainData(HttpServletRequest request,HttpServletResponse response){
        User user = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
        int userId = user.getUser_id();
        Map<String,Object> paramMap = initRequestParam(request);
        paramMap.put("user_id", userId);
        String resultJson = personScreenService.listMainData(paramMap);
//        String resultJson = null;
        Map<String,Object> resultMap = new HashMap<String,Object>();
        resultMap.put("success", "true");
        resultMap.put("resultJson", resultJson);
        return resultJson;
    }


    @RequestMapping("listTag")
    @ResponseBody
    public Map<String, Map<String, List<Map<String, Object>>>> listTag(HttpServletRequest request){

        Map<String, List<Map<String, Object>>> marketTags = personScreenService.listTagData(null);

        Map<String, Map<String, List<Map<String, Object>>>> jsonMap = new HashMap<>();
        jsonMap.put("tagMarket",marketTags);

        return jsonMap;
    }

    @RequestMapping("sumPCnt")
    @ResponseBody
    public long sumPersonCount(HttpServletRequest request){
        Map<String,Object> paramMap = initRequestParam(request);
        Map<String,Object> pramMap = (Map<String, Object>) paramMap.get("json_data");
        long resultCont = personScreenService.sumPersonCount(pramMap);
        Map<String,Object> resultMap = new HashMap<String,Object>();
        resultMap.put("resultCont",resultCont);
        return resultCont;
    }

    @RequestMapping("crtSnCdion")
    public String createScreeningCondition(HttpServletRequest request){
        return "persongroup/sum/crtSnCdion";
    }

    @RequestMapping("editSnCdion")
    public String editScreeningCondition(HttpServletRequest request){
        Map<String,Object> paramMap = initRequestParam(request);

        request.getSession().setAttribute("screeningId",paramMap.get("screeningId"));
        return "persongroup/sum/editSnCdion";
    }

    @RequestMapping("viewSnCdion")
    public String viewScreeningCondition(HttpServletRequest request){
        Map<String,Object> paramMap = initRequestParam(request);

        request.getSession().setAttribute("screeningId",paramMap.get("screeningId"));
        return "persongroup/sum/viewSnCdion";
    }

    @RequestMapping("getScreeningAndTags")
    @ResponseBody
    public Map<String,Object> getScreeningAndTags(HttpServletRequest request){
        Map<String,Object> paramMap = initRequestParam(request);
        Map<String, Object> dataStr = personScreenService.getScreeningAndTags(paramMap);
        return dataStr;
    }


    @RequestMapping("saveScreening")
    @ResponseBody
    public Map<String, Map<String, List<Map<String, Object>>>> saveScreening(HttpServletRequest request){
        Map<String,Object> paramMap = initRequestParam(request);
        User user = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
        int userId = user.getUser_id();

        Map<String,Object> json_data = (Map)paramMap.get("json_data");
        json_data.put("user_id", userId);
        Map<String, List<Map<String, Object>>> marketTags = personScreenService.saveScreening(json_data);

        Map<String, Map<String, List<Map<String, Object>>>> jsonMap = new HashMap<>();
        jsonMap.put("tagMarket",marketTags);

        return jsonMap;
    }

    @RequestMapping("updateScreening")
    @ResponseBody
    public Map<String, Object> updateScreening(HttpServletRequest request){
        Map<String,Object> paramMap = initRequestParam(request);
        User user = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
        int userId = user.getUser_id();

        Map<String,Object> json_data = (Map)paramMap.get("json_data");
        json_data.put("user_id", userId);
        personScreenService.updateScreening(json_data);

        Map<String, Object> jsonMap = new HashMap<>();

        return jsonMap;
    }

    @RequestMapping("delScreeningData")
    @ResponseBody
    public Map<String, Object> delScreeningData(HttpServletRequest request){

        Map<String,Object> paramMap = initRequestParam(request);

        Map<String,Object> json_data = (Map)paramMap.get("json_data");

        personScreenService.delScreeningData(json_data);

        Map<String, Object> jsonMap = new HashMap<>();

        return jsonMap;
    }

//

}
