package com.seabox.tagsys.usertags.service;

import com.seabox.tagsys.usertags.msg.MsgCallResultRequest;

import java.io.IOException;

/**
 * @author Changhua, Wu
 *         Created on: 5/19/16,3:21 PM
 */
public interface IndvService {

    void saveCallResponse(MsgCallResultRequest msgCallResultRequest) throws IOException;

}
