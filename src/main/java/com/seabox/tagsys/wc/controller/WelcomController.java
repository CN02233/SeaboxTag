package com.seabox.tagsys.wc.controller;

import com.seabox.tagsys.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("base/wc")
public class WelcomController extends BaseController {
	
	@RequestMapping("welcome")
	public String welcomePage(){
		return viewPage("welcome");
	}
}
