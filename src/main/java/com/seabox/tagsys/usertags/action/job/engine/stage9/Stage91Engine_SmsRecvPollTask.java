package com.seabox.tagsys.usertags.action.job.engine.stage9;

import com.seabox.tagsys.usertags.action.job.ControllableThreadBase;
//import com.seabox.tagsys.usertags.action.sms.RespMsgSmsRecv;
//import com.seabox.tagsys.usertags.action.sms.SmsApiWrapService;
//import com.seabox.tagsys.usertags.action.sms.SmsResp;
import com.seabox.tagsys.usertags.hbase.HBaseConnectionMgr;
import com.seabox.tagsys.usertags.hbase.HBaseUtil;
import com.seabox.tagsys.usertags.hbase.entity.TCampInfo;
import com.seabox.tagsys.usertags.hbase.entity.TSmsAction;
import com.seabox.tagsys.usertags.utils.SingletonBean;
//import com.seabox.sms.entity.Report;
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
 * @author Changhua, Wu
 *         Created on: 2/19/16,2:23 PM
 */
//@SingletonBean
public class Stage91Engine_SmsRecvPollTask extends ControllableThreadBase {

    private static final Logger logger = LoggerFactory.getLogger(Stage91Engine_SmsRecvPollTask.class);

//    @Autowired
//    private SmsApiWrapService smsApiWrapService;

    private Table smsActionTable;
    private Table campInfoTable;
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



    public Stage91Engine_SmsRecvPollTask() {

    }

    public synchronized void lazyInit() {
        if(hBaseCon == null) {
            if(connectionMgr!=null) {
                logger.info(" -----------  lazyInit() start ---------------- ");

                try {
                    hBaseCon = connectionMgr.getConnection();
                    smsActionTable = hBaseCon.getTable(  TSmsAction.tableName() );
                    campInfoTable = hBaseCon.getTable(  TCampInfo.tableName() );
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
//            RespMsgSmsRecv  respMsgSmsRecv = smsApiWrapService.querySmsRecvMsg();
//            if(respMsgSmsRecv != null) {
//
//                if(respMsgSmsRecv.isSuccess()) {
//                    countSmsAPIReturnOK.getAndIncrement();
//                } else {
//                    countSmsAPIReturnNG.getAndIncrement();
//                }
//
//                List<Report>  reportList = respMsgSmsRecv.getResultList();
//                if(reportList == null || reportList.isEmpty()) {
//                   // logger.warn("empty  result got from SMS APIs, sleep {} seconds!", smsApiWrapService.getIdleIntervalMilliSeconds()/1000 );
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
//                countSmsReportItem.getAndAdd( reportList.size() );
//
//                for(Report report: reportList) {
//                    String seqNum = report.getSerial_no();
//                    String result = report.getResult();
//                    result = SmsResp.getNoneNullValue( result );
//
//                    if(smsActionTable != null) {
//                        Get getSms = new Get( seqNum.getBytes() );
//                        getSms.addFamily( TSmsAction._User.ColFamily() );
//
//                        try {
//                            Result resultSms = smsActionTable.get(getSms);
//                            if( HBaseUtil.isAnValidResult(resultSms )) {
//                                Append append = new Append(seqNum.getBytes() );
//                                Date  now = new Date();
//                                lastTimeValidSmsItem = now;
//                                StringBuffer reportDump = new StringBuffer();
//                                reportDump.append(" [result=")
//                                        .append(result)
//                                        .append(", mobile=")
//                                        .append(report.getMobile_no())
//                                        .append(", time=")
//                                        .append(now.toString())
//                                        .append("] ");
//                                append.add( TSmsAction._Sms.ColFamily(), TSmsAction._Sms.col_recv(), reportDump.toString().getBytes() );
//                                smsActionTable.append( append );
//
//                                byte[]  campIdByte = resultSms.getValue(TSmsAction._User.ColFamily(),  TSmsAction._User.col_campId());
//                                if(campIdByte != null && campInfoTable != null) {
//
//                                    long recvCount = HBaseUtil.increaseCounter(smsActionTable, seqNum.getBytes(), TSmsAction._Sms.ColFamily(),  TSmsAction._Sms.col_recvCount(), 1);
//
//                                    if(1==recvCount) { // Only count Once for Recv status update for a single User
//                                        if( SmsResp.isSuccessReturnStr( result ) ) {
//                                            countSmsRecvOK.getAndIncrement();
//                                            HBaseUtil.increaseCounter(campInfoTable, campIdByte, TCampInfo.ColFamily(), TCampInfo.col_smsRecvCount(), 1);
//                                        } else {
//                                            countSmsRecvNG.getAndIncrement();
//                                            HBaseUtil.increaseCounter(campInfoTable, campIdByte, TCampInfo.ColFamily(), TCampInfo.col_smsRecvFailCount(), 1 );
//                                        }
//                                    }
//
//                                }
//
//
//                            } else {
//                                logger.warn("I don't have such SeqNum send out, SMS API gives me un-interesting items seqNum={}", seqNum);
//                            }
//                        } catch (IOException e) {
//                            logger.error("failed to access hBase smsActionTable !", e);
//                        }
//
//                    }
//
//                }
//
//            }
//
//        } catch (Throwable e) {
//
//            logger.error("failed on doWork() with exception: ", e);
//
//        }


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
