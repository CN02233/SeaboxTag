package com.seabox.tagsys.usertags.action.job.engine.stage9;

import com.seabox.tagsys.usertags.action.job.ControllableThreadBase;
//import com.seabox.tagsys.usertags.action.sms.RespMsgSmsReply;
//import com.seabox.tagsys.usertags.action.sms.SmsApiWrapService;
import com.seabox.tagsys.usertags.hbase.HBaseConnectionMgr;
import com.seabox.tagsys.usertags.hbase.HBaseUtil;
import com.seabox.tagsys.usertags.hbase.entity.TCampInfo;
import com.seabox.tagsys.usertags.hbase.entity.TSmsReplyTrack;
import com.seabox.tagsys.usertags.utils.SingletonBean;
//import com.seabox.sms.entity.Reply;
import org.apache.hadoop.hbase.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by wuchh on 3/9/16.
 */

//@SingletonBean
public class Stage92Engine_SmsReplyPollTask extends ControllableThreadBase {


    private static final Logger logger = LoggerFactory.getLogger(Stage92Engine_SmsReplyPollTask.class);

//    @Autowired
//    private SmsApiWrapService smsApiWrapService;


    private Table campInfoTable;
    private Table  smsReplyTrackTable;

    private Connection hBaseCon;

    private AtomicLong   countSmsRecvOK = new AtomicLong(0);
    private AtomicLong   countSmsRecvNG = new AtomicLong(0);
    private AtomicLong   countSmsReportItem = new AtomicLong(0);
    private AtomicLong   countSmsAPIRequest = new AtomicLong(0);
    private AtomicLong   countSmsAPIReturnOK = new AtomicLong(0);
    private AtomicLong   countSmsAPIReturnNG = new AtomicLong(0);
    private Date         lastTimeValidSmsItem;


//    @Autowired
    private HBaseConnectionMgr connectionMgr;


    @Override
    public Map<String, String> getStatsMap() {
        Map<String, String>  statsMap = super.getStatsMap();
        statsMap.put("recvOK", countSmsRecvOK.toString());
        statsMap.put("recvNG", countSmsRecvNG.toString());
        statsMap.put("reportItem", countSmsReportItem.toString());
        statsMap.put("apiRequest", countSmsAPIRequest.toString());
        statsMap.put("apiReturnOK", countSmsAPIReturnOK.toString());
        statsMap.put("apiReturnNG", countSmsAPIReturnNG.toString());
        statsMap.put("lastTimeReport", (lastTimeValidSmsItem==null) ? "null" : lastTimeValidSmsItem.toString());
        return statsMap;
    }



    public Stage92Engine_SmsReplyPollTask() {

    }

    public synchronized void lazyInit() {
        if(hBaseCon == null) {
            if(connectionMgr!=null) {
                logger.info(" -----------  lazyInit() start ---------------- ");

                try {
                    hBaseCon = connectionMgr.getConnection();
                    campInfoTable = hBaseCon.getTable(  TCampInfo.tableName() );
                    smsReplyTrackTable = hBaseCon.getTable( TSmsReplyTrack.tableName() );
                    logger.info(" -----------  lazyInit() end ---------------- ");
                } catch (IOException e) {
                    logger.error("-----------  lazyInit() failed to access hBase error !" , e );
                }
            }

        }

    }



    @Override
    public void doWork() {

//        try {
//
//            lazyInit();
//
//            countSmsAPIRequest.getAndIncrement();
//            RespMsgSmsReply respMsgSmsReply = smsApiWrapService.querySmsReplyMsg();
//            if(respMsgSmsReply != null) {
//
//                if(respMsgSmsReply.isSuccess()) {
//                    countSmsAPIReturnOK.getAndIncrement();
//                } else {
//                    countSmsAPIReturnNG.getAndIncrement();
//                }
//
//                String extendaccessnum = respMsgSmsReply.getExtendaccessnum();
//
//                List<Reply>  replyList = respMsgSmsReply.getResultList();
//                if(replyList == null || replyList.isEmpty()) {
//                    // logger.warn("empty  result got from SMS APIs, sleep {} seconds!", smsApiWrapService.getIdleIntervalMilliSeconds()/1000 );
//                    try {
//                        Thread.sleep( smsApiWrapService.getIdleIntervalMilliSeconds() ); // There's no stream / flag for when to pause from current SMS API
//                    } catch (InterruptedException e) {
//                        logger.error("I was Interrupted : " + this , e);
//
//                        Thread myThread = Thread.currentThread();
//                        myThread.interrupt();
//                    }
//                    return;
//                }
//
//                Date  now =new Date();
//                lastTimeValidSmsItem = now;
//
//                countSmsReportItem.getAndAdd( replyList.size() );
//
//                TSmsReplyTrack  findResult = TSmsReplyTrack.findById(smsReplyTrackTable, extendaccessnum);
//
//                if(campInfoTable != null && findResult != null && findResult.getCurrent_campId() != null) {
//
//                    countSmsRecvOK.getAndIncrement();
//
//                    String campIdFound = findResult.getCurrent_campId();
//
//                    HBaseUtil.increaseCounter(campInfoTable, campIdFound.getBytes(), TCampInfo.ColFamily(), TCampInfo.col_smsAckCount(), replyList.size() );
//                    HBaseUtil.increaseCounter(smsReplyTrackTable, extendaccessnum.getBytes(), TSmsReplyTrack._Camps.ColFamily(), campIdFound.getBytes(), replyList.size() );
//                }
//
//            }
//
//        } catch (Throwable e) {
//
//            logger.error("failed on doWork() with exception: ", e);
//
//        }
//
//
//        try {
//            int busySleep = smsApiWrapService.getBusyWorkIntervalMilliSeconds();
//            if(busySleep>0) {
//                Thread.sleep( busySleep ); // There's no stream / flag for when to pause from current SMS API
//            }
//
//        } catch (InterruptedException e) {
//            logger.error("I was Interrupted : " + this , e);
//
//            Thread myThread = Thread.currentThread();
//            myThread.interrupt();
//        }

    }


}

