package com.seabox.tagsys.usertags.service.impl;

import com.seabox.tagsys.usertags.hbase.HBaseConnectionMgr;
import com.seabox.tagsys.usertags.hbase.entity.TCallAction;
import com.seabox.tagsys.usertags.msg.IndvCallResult;
import com.seabox.tagsys.usertags.msg.MsgCallResultRequest;
import com.seabox.tagsys.usertags.service.IndvService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * @author Changhua, Wu
 *         Created on: 5/19/16,3:12 PM
 */
@Service
public class IndvServiceImpl implements IndvService {

    private static final Logger logger = LoggerFactory.getLogger(IndvServiceImpl.class);

//    @Autowired
    private HBaseConnectionMgr   hBaseConnectionMgr;


    public void saveCallResponse(MsgCallResultRequest msgCallResultRequest) throws IOException {

        List<IndvCallResult> callResults = msgCallResultRequest.getCallResultList();
        for (IndvCallResult callResult : callResults) {
            TCallAction.createOneNewRecord(hBaseConnectionMgr.getBufMutTCallAction(), callResult.getCampId(), callResult.getUserId(), callResult.getMobile(), callResult.getCallResult());
        }

        hBaseConnectionMgr.getBufMutTCallAction().flush();
        hBaseConnectionMgr.getBufMutTCallAction().close();
        logger.info("saveCallResponse done with records: {}", callResults.size() );


    }


}
