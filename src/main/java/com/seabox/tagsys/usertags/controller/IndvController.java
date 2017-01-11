package com.seabox.tagsys.usertags.controller;

import com.seabox.tagsys.usertags.msg.MsgCallResultRequest;
import com.seabox.tagsys.usertags.msg.MsgCallResultResponse;
import com.seabox.tagsys.usertags.service.IndvService;
import com.seabox.tagsys.usertags.utils.UserTagUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Changhua, Wu
 *         Created on: 5/18/16,11:31 AM
 */
@Controller
@RequestMapping("indv")
public class IndvController {


    private static Logger logger = LoggerFactory.getLogger(IndvController.class);


    @Autowired
    private IndvService indvService;


    @RequestMapping("info")
    public String tagMarket(HttpServletRequest request) {
        return "indv/info/showIndvInfo";
    }


    @RequestMapping(value = "saveResp/call", method = RequestMethod.POST)
    @ResponseBody
    public String saveCallResponse(HttpServletRequest request, @RequestBody String jsonMsgBody ) {

        MsgCallResultResponse msgCallResultResponse = new MsgCallResultResponse();

        try {

            MsgCallResultRequest msgCallResultRequest = UserTagUtils.jsonToObject( jsonMsgBody, MsgCallResultRequest.class );

            logger.info("msgCallResultRequest: {} " , msgCallResultRequest);

            try {
                indvService.saveCallResponse( msgCallResultRequest );
                msgCallResultResponse.buildSucessMsg();

            } catch (Throwable e) {
                logger.error("failed to saveCallResponse", e);
                msgCallResultResponse.setResultCode(2);
                msgCallResultResponse.setMsg("failed to saveCallResponse due to internal error");
            }

        } catch (Throwable e) {
            logger.error("failed to decode json object", e);
            msgCallResultResponse.setResultCode(1);
            msgCallResultResponse.setMsg("failed to decode json msg:"+ jsonMsgBody);
        }

        String json = UserTagUtils.objectToJson(msgCallResultResponse);
        return json;
    }



}
