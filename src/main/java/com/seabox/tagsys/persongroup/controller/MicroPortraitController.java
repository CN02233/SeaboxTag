package com.seabox.tagsys.persongroup.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by SongChaoqun on 2016/11/23.
 * 群内微观画像
 */
//@Controller
//@RequestMapping("person/microPt")
public class MicroPortraitController {

    @RequestMapping("microPtMain")
    public String microPtMain(HttpServletRequest request){
//        System.out.println("here is running");
        request.getSession().setAttribute("pageType","microPt");
        return "persongroup/micAndMac/ptMainPage";

    }

    @RequestMapping("personPage")
    public String personPage(HttpServletRequest request){

        return "persongroup/micAndMac/personPage";
    }


    @RequestMapping("listScreennig")
    @ResponseBody
    public Map<String,Object> listScreennig(){
        return null;
    }

}
