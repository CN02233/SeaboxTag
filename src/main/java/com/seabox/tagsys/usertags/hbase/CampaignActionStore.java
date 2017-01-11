package com.seabox.tagsys.usertags.hbase;

import com.seabox.tagsys.usertags.action.CampaignActionStatus;
import com.seabox.tagsys.usertags.action.job.engine.SmsTaskControl;
//import com.seabox.tagsys.usertags.action.sms.SmsApiWrapService;
import com.seabox.tagsys.usertags.entity.CampInfo;
import com.seabox.tagsys.usertags.hbase.entity.CampSmsActionStaticsImpl;
import com.seabox.tagsys.usertags.hbase.entity.TCampInfo;

import java.util.Set;

/**
 * @author Changhua, Wu
 *         Created on: 2/15/16,3:47 PM
 */
public interface CampaignActionStore extends AutoCloseable {
    //        campID, tagCondition, userBitSet,  n-2-GUID-mapping-version
//    campID, tagCondition, numOfUsers,  GUID-01 : mobile, GUID-02 : mobile


    void   saveCampaignInfo(byte[] redisBitSet, String tagConditionJson, long numOfUsers);

    /*
     *  rowId =  campID + mobileID (or  sequenceID)
     *  column-families: "action"
     *       "todo" :1
     *       "send" :1
     *       "recv" :1
     *       "ack"  :0
     *
     */
    boolean   addUserToCampaignAction( String userGUID, final Set<String> excludeUsers);


    TCampInfo findOneCampInfoRecordWithoutUsersBitSet(String campId, boolean loadUsersBitSet);


    void updateCampActionDetailStatus(CampaignActionStatus actionStatus);


//    void   performAction(CampInfo campInfo, SmsApiWrapService smsApi, SmsTaskControl smsTaskControl);
    void   performAction(CampInfo campInfo, Object smsApi, SmsTaskControl smsTaskControl);


    CampSmsActionStaticsImpl getCampSmsActionStatics();


    @Override
    void close();

}
