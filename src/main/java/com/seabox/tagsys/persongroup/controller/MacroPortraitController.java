package com.seabox.tagsys.persongroup.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by pc on 2016/11/25.
 * 群内宏观画像
 */
//@Controller
//@RequestMapping("person/macroPt")
public class MacroPortraitController {
    @RequestMapping("macroPtMain")
    public String macroPtMain(HttpServletRequest request){
//        System.out.println("here is running");
        request.getSession().setAttribute("pageType","macroPt");

        return "persongroup/micAndMac/ptMainPage";
    }



    @RequestMapping("macroPtDetail")
    public String macroPtDetail(){
        return "persongroup/macroPt/macroPtDetail";
    }

    @RequestMapping("macroPtDetail/{campId}")
    @ResponseBody
    public void listUser4Screening(@PathVariable int campId){

    }

}
