package com.seabox.tagsys.usertags.hbase;

import com.seabox.tagsys.usertags.action.CampaignActionStatus;
import com.seabox.tagsys.usertags.action.job.HealthStatsInstance;
import com.seabox.tagsys.usertags.action.job.engine.SmsTaskControl;
import com.seabox.tagsys.usertags.hbase.entity.*;
//import com.seabox.tagsys.usertags.action.sms.RespMsgSmsSend;
//import com.seabox.tagsys.usertags.action.sms.SmsApiWrapService;
//import com.seabox.tagsys.usertags.action.sms.TemplatePara;
import com.seabox.tagsys.usertags.entity.CampInfo;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellScanner;
import org.apache.hadoop.hbase.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.*;


/**
 * @author Changhua, Wu
 *         Created on: 2/15/16,4:49 PM
 */



//hBase :
        /*
        campId,
        n-2-GUID  mapping
        result bitSet  ==>  n of  each Users.
        get GUID
        get Mobile-No
        Store the condition as Json.


        Table:n-2-GUID-mapping
        version,         n-2-GUID-mapping

        Table:Campaign-result
        campID, tagCondition, userBitSet,  n-2-GUID-mapping-version

        campID, tagCondition, numOfUsers, status(SAVE -> ACTION -> DONE),   +  GUID-01 : mobile, status;  GUID-02 : mobile, status

        //MultipleColumnPrefixFilter




campID + Seq,


guid:1121


guid:2q23

seq:0123


campID1:mobileID:  todo:0, send:1, recv:1, ack:0

         */


public class CampaignActionStoreHBaseImpl implements CampaignActionStore, HealthStatsInstance {


    private static final Logger logger = LoggerFactory.getLogger(CampaignActionStoreHBaseImpl.class);


    private String  campID;

    private Table  userInfoTable;
    private Table  smsActionTable;
    private BufferedMutator  smsActionBufferedMutator;
    private Table  campInfoTable;
    private BufferedMutator  campInfoBufferedMutator;
    private Table  smsReplyTrackTable;

    private Jedis optionalJedis;


    public static CampaignActionStoreHBaseImpl createInstance(String  campID, Connection   hBaseCon) {

        CampaignActionStoreHBaseImpl instance = null;
        try {
            instance = new CampaignActionStoreHBaseImpl(campID, hBaseCon);
        } catch (IOException e) {
            logger.error("failed to open related hBase tables, detail exception: ", e);
        }

        return instance;
    }

    public static CampaignActionStoreHBaseImpl createInstance(String  campID, Connection   hBaseCon,  Jedis jedis) {

        CampaignActionStoreHBaseImpl instance = null;
        try {
            instance = new CampaignActionStoreHBaseImpl(campID, hBaseCon);
            instance.optionalJedis = jedis;
        } catch (IOException e) {
            logger.error("failed to open related hBase tables, detail exception: ", e);
        }

        return instance;
    }

    /**
     * Constructor
     * @param campID
     */
    public CampaignActionStoreHBaseImpl(String  campID, Connection   hBaseCon) throws IOException{
        this.campID = campID;

        campInfoTable = hBaseCon.getTable(  TCampInfo.tableName() );
        BufferedMutatorParams  params = new BufferedMutatorParams( TCampInfo.tableName() );
        params.writeBufferSize(1024*1024);
        campInfoBufferedMutator = hBaseCon.getBufferedMutator( params );

        userInfoTable =  hBaseCon.getTable(  TUserInfo.tableName() );

        smsActionTable = hBaseCon.getTable(  TSmsAction.tableName() );
        BufferedMutatorParams  paramsSms = new BufferedMutatorParams( TSmsAction.tableName() );
        paramsSms.writeBufferSize(1024*1024);
        smsActionBufferedMutator = hBaseCon.getBufferedMutator( paramsSms );

        smsReplyTrackTable = hBaseCon.getTable( TSmsReplyTrack.tableName() );

    }






    @Override
    public void close()  {
        flushDB();
    }

    public void flushDB() {
        try {
            campInfoBufferedMutator.close();
            smsActionBufferedMutator.close();
            userInfoTable.close();
            smsActionTable.close();
            campInfoTable.close();
            smsReplyTrackTable.close();
        } catch (IOException e) {
            logger.error("close hbase tables failed, detail exception: ", e);
        }
    }


    public CampSmsActionStaticsImpl getCampSmsActionStatics() {

        return TCampInfo.getCampSmsActionStatics( campInfoTable, campID );

    }


    @Override
    public void saveCampaignInfo(byte[] redisBitSet, String tagConditionJson, long numOfUsers) {

        logger.info("saveCampaignInfo() start  for campId: {}", campID);

        try {
            //向HBASE中创建一个活动信息,scq注释
            TCampInfo.createOneNewRecord(campInfoTable, campID, redisBitSet, tagConditionJson, numOfUsers);

            updateCampActionDetailStatus( CampaignActionStatus.PREPARE );
            logger.info("saveCampaignInfo() complete  for campId: {}", campID);

        } catch (IOException e) {
            logger.error("saveCampaignInfo() Failed  for campId: " + campID + " detail exception:", e);
        }

    }

    @Override
    public boolean addUserToCampaignAction(String userGUID, final Set<String> excludeUsers) {

        try {
            TUserInfo userInfo = TUserInfo.wrapperFindById(userInfoTable, userGUID);
            TCampInfo.addUser(campInfoTable, campInfoBufferedMutator, userInfo, smsActionBufferedMutator, campID, userGUID, excludeUsers);
            return true;
        } catch (IOException e) {
            return false;
        }

    }

    @Override
    public TCampInfo findOneCampInfoRecordWithoutUsersBitSet(String campId, boolean  loadUsersBitSet) {
        TCampInfo campInfoRecord = TCampInfo.findById( campInfoTable, campId, loadUsersBitSet);
        return campInfoRecord;
    }


    @Override
    public void updateCampActionDetailStatus(CampaignActionStatus actionStatus) {

        try {

            HBaseUtil.addColumnWithValue(campInfoTable,
                    campID,
                    TCampInfo.ColFamily(),
                    TCampInfo.col_status(),
                    actionStatus.toString()
                    );

            logger.info("updateCampActionDetailStatus( actionStatus:{} ) success  for campId: {}", actionStatus, campID);

        } catch (IOException e) {
            logger.error("flush failed, detail exception: ", e);
        }

    }

    @Override
    public void performAction(CampInfo campInfo, Object smsApi, SmsTaskControl smsTaskControl) {

    }


    public Map<String, String>  buildUserInfoMap( Result resultSms  ) {
        Map<String, String>  realValuesMap = new HashMap<>();
//        for(TemplatePara templatePara: TemplatePara.values()) {
//            byte[] valueByte = resultSms.getValue( templatePara.getColumnFamily(), templatePara.getColumnQualifier() );
//            String value = new String(valueByte);
//            realValuesMap.put( templatePara.getTemplateChar(), value);
//        }

        return realValuesMap;
    }



//    @Override
//    public void performAction(CampInfo campInfo, SmsApiWrapService smsApi, SmsTaskControl smsTaskControl) {
//
//        String campIdRowKey = campInfo.getCamp_id();
//
//        logger.info("============= started performAction() for camp:{} =============", campIdRowKey );
//
//
//        try {
//            String rowKey = String.valueOf(campID);
//
//            Get get_campStatus = new Get( rowKey.getBytes() );
//            get_campStatus.addColumn(TCampInfo.ColFamily(),  TCampInfo.col_status());
//            Result result_campStatus = campInfoTable.get(get_campStatus );
//            if( HBaseUtil.isAnValidResult(result_campStatus) ) {
//                String previousStatus = HBaseUtil.getValueAsString(result_campStatus, TCampInfo.ColFamily(), TCampInfo.col_status());
//                if(previousStatus != null ) {
//                    if(previousStatus.equals( CampaignActionStatus.ACTION.toString() )) {
//                        // this is resume ACTION from last Shutdown
//                        updateCampActionDetailStatus( CampaignActionStatus.ACTION_RESUME );
//                    } else {
//                        // first time READY->ACTION
//                        updateCampActionDetailStatus( CampaignActionStatus.ACTION );
//
//                    }
//
//                }
//            }
//
//
//            final int LIMIT_SMS_SEND_WITHIN_1_MINS = smsApi.getLimitSmsSendWithinOneHour() / 60 ; // limit of each 1min interval
//            final int GAP_1_MINS_IN_MILISECONDS = 1000 * 60;
//            int limitSmsSendCount = 0;
//            Calendar  lastSmsSendCheckTime = null;
//
//            Get get = new Get( campIdRowKey.getBytes() );
//            get.addFamily( TCampInfo._Users.ColFamily() );
//            Result result = campInfoTable.get( get );
//
//            final int totalUsersToAction = result.size();
//            logger.info("============= performAction() for camp:{}  remainUsersToAction:{} =============", campIdRowKey, totalUsersToAction );
//
//            CellScanner cellScanner =  result.cellScanner();
//            long sendCount = 0;
//            long sendFailCount = 0;
//            long userScanned = 0;
//            String extendaccessnum = null;
//            while ( cellScanner.advance() ) {
//
//                ++userScanned;
//                final long progress = 100* userScanned/totalUsersToAction;
//
//
//                if( !smsTaskControl.isWorking()) {
//
//                    logger.info(" smsTaskControl disabled working, waitForResume  with campId={},  progress={}%", campIdRowKey, progress);
//
//                    flushDB(); // force to flush DB.
//
//                    smsTaskControl.waitForResume( this ); // waiting until continue allowed
//                    logger.info(" smsTaskControl enabled working, got Resumed with campId={},  progress={}%", campIdRowKey, progress);
//                }
//
//                Cell cell = cellScanner.current();
//
//
//                String userGUID = new String(cell.getQualifierArray(), cell.getQualifierOffset(), cell.getQualifierLength());
//                String valueOfRowKeySms = new String(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength());
//
//                Get getSms = new Get( valueOfRowKeySms.getBytes() );
//                getSms.addFamily( TSmsAction._User.ColFamily() );
//
//                Result resultSms = smsActionTable.get(getSms);
//
//                boolean sendSuccess = false;
//                if(resultSms == null || resultSms.isEmpty()) {
//
//                    sendSuccess = false;
//
//                    logger.warn(" Error, rowKeySms not found in Sms_table, userGUID={}  rowKeySms={}", userGUID, valueOfRowKeySms);
//
//                } else {
//                    byte[] mobileNum = resultSms.getValue( TSmsAction._User.ColFamily(),  TSmsAction._User.col_mobile() );
//
//                    if(mobileNum == null) {
//
//                        sendSuccess = false;
//
//                        logger.warn(" performAction().sendMessage userGUID = {}, rowKeySms={} mobileNum is missing!", new String[]{
//                                userGUID,
//                                valueOfRowKeySms
//                        });
//
//                    } else {
//
//                        String mobileStr = new String(mobileNum);
//
//                        String seqNum = valueOfRowKeySms;
//
//                        Map<String, String> realValuesMap = buildUserInfoMap( resultSms );
//
//                        List<String> smsModelParameters = campInfo.buildRealSmsTemplateParaString( realValuesMap );
//
//                        if(logger.isTraceEnabled()) {
//                            logger.trace(" performAction().sendMessage userGUID = {}, rowKeySms={} mobileStr={} smsModelParameters={}",
//                                    userGUID,
//                                    valueOfRowKeySms,
//                                    mobileStr,
//                                    smsModelParameters
//                            );
//                        }
//
//                        if(extendaccessnum==null) {
//                            extendaccessnum = TSmsReplyTrack.registerExtendNumForCamp(smsReplyTrackTable, campID);
//                        }
//
//                        if(limitSmsSendCount >= LIMIT_SMS_SEND_WITHIN_1_MINS) {
//
//                            long duration = Calendar.getInstance().getTimeInMillis() - lastSmsSendCheckTime.getTimeInMillis(); // in milli-Seconds
//                            // 10 s'
//                            if(duration < GAP_1_MINS_IN_MILISECONDS ) {
//
//                                long gap = GAP_1_MINS_IN_MILISECONDS - duration;
//                                if(gap > 0) {
//                                    logger.info("campId={} encounter SMS send limit control, userScanned={}, limitSmsSendCount={}, progress={}%, let's sleep gap={} milli-seconds", campIdRowKey, userScanned, limitSmsSendCount, progress,  gap);
//                                    Thread.sleep(  gap );
//                                    logger.info("campId={} resume from SMS send limit control, userScanned={}, limitSmsSendCount={}, progress={}%", campIdRowKey, userScanned, limitSmsSendCount, progress);
//                                } else {
//                                    logger.info("campId={} is align with SMS send limit control, userScanned={}, limitSmsSendCount={}, progress={}%", campIdRowKey, userScanned, limitSmsSendCount, progress);
//                                }
//                            } else {
//                                logger.info("campId={} is slower then limit control, delay={} userScanned={}, limitSmsSendCount={}, progress={}%", campIdRowKey, duration - GAP_1_MINS_IN_MILISECONDS,  userScanned, limitSmsSendCount, progress);
//                            }
//
//                            limitSmsSendCount = 0;
//                            lastSmsSendCheckTime = Calendar.getInstance();
//
//                        }
//
//
//                        RespMsgSmsSend respSend =  smsApi.sendMessage(mobileStr, campInfo.getTemplt_id(), seqNum, smsModelParameters, extendaccessnum);
//
//                        ++limitSmsSendCount;
//
//                        // now Moving this user from  family _Users -> _SentUsers.
//                        // it allow us resume send action from pause by Hourly blocking(20:00~08:00)
//                        // it also made possible to resume from a stop situation from Tomcat shutdown.
//                        HBaseUtil.moveOrRenameAColumn(campInfoBufferedMutator,
//                                campIdRowKey.getBytes(), valueOfRowKeySms.getBytes(),
//                                TCampInfo._Users.ColFamily(), userGUID.getBytes(),
//                                TCampInfo._SentUsers.ColFamily(), userGUID.getBytes()
//                                );
//
//                        if(null == lastSmsSendCheckTime) {
//                            lastSmsSendCheckTime = Calendar.getInstance();
//                        }
//
//                        sendSuccess = respSend.isSuccess();
//                    }
//
//
//
//                }
//
//                Date now = new Date();
//                String currentTime = now.toString();
//
//                if(sendSuccess) {
//                    HBaseUtil.addColumnWithValueAsync(smsActionBufferedMutator,
//                            valueOfRowKeySms,
//                            TSmsAction._Sms.ColFamily(), TSmsAction._Sms.col_send(),
//                            currentTime);
//
//                    HBaseUtil.increaseCounter(campInfoTable, campIdRowKey.getBytes(), TCampInfo.ColFamily(),  TCampInfo.col_smsSendCount(), 1);
//                    ++sendCount;
//                } else {
//                    HBaseUtil.addColumnWithValueAsync(smsActionBufferedMutator,
//                            valueOfRowKeySms,
//                            TSmsAction._Sms.ColFamily(), TSmsAction._Sms.col_sendFail(),
//                            currentTime);
//
//                    HBaseUtil.increaseCounter(campInfoTable, campIdRowKey.getBytes(), TCampInfo.ColFamily(),  TCampInfo.col_smsSendFailCount(), 1);
//                    ++ sendFailCount;
//                }
//
//
//            }
//
//            // status -> Ready
//            updateCampActionDetailStatus( CampaignActionStatus.DONE );
//
//            logger.info("============= performAction()  Done for camp:{}, sendCount={}, sendFailCount={} =============", campInfo.getCamp_id(), sendCount, sendFailCount );
//
//        } catch (IOException e) {
//            logger.error("error on  performAction(), detail exception: ", e);
//        } catch (InterruptedException e) {
//            logger.error("I was Interrupted : " + this , e);
//
//            flushDB(); // force to flush DB.
//
//            Thread myThread = Thread.currentThread();
//            myThread.interrupt();
//        }
//
//    }


    @Override
    public String getStatsTitle() {
        return this.getClass().getCanonicalName() + "-campID:" + campID + "-objID:" + this.hashCode();
    }

    @Override
    public Map<String, String> getStatsMap() {
        return null;
    }
}
