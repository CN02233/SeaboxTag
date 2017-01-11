package com.seabox.tagsys.usertags.action.job.schedule;

import com.alibaba.dubbo.common.utils.ConcurrentHashSet;
import com.seabox.tagsys.usertags.action.CampaignActionStatus;
import com.seabox.tagsys.usertags.action.job.HealthStatsInstance;
import com.seabox.tagsys.usertags.action.job.HealthStatsManager;
import com.seabox.tagsys.usertags.action.job.engine.TaskQueue_Scan_to_1;
import com.seabox.tagsys.usertags.action.job.engine.stage1.Stage1Engine_PrepareCampTask;
import com.seabox.tagsys.usertags.action.job.engine.stage9.Stage91Engine_SmsRecvPollTask;
import com.seabox.tagsys.usertags.action.job.engine.stage9.Stage92Engine_SmsReplyPollTask;
import com.seabox.tagsys.usertags.entity.CampInfo;
import com.seabox.tagsys.usertags.hbase.CampaignActionStore;
import com.seabox.tagsys.usertags.hbase.CampaignActionStoreHBaseImpl;
import com.seabox.tagsys.usertags.hbase.HBaseConnectionMgr;
import com.seabox.tagsys.usertags.hbase.entity.TCampInfo;
import com.seabox.tagsys.usertags.mybatis.dao.CampInfoDao;
import com.seabox.tagsys.usertags.utils.SingletonBean;
import org.apache.hadoop.hbase.client.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Changhua, Wu
 *         Created on: 2/17/16,8:27 PM
 */
//@SingletonBean
public class Stage1PrepareScanJob implements HealthStatsInstance {


    private static final Logger logger = LoggerFactory.getLogger(Stage1PrepareScanJob.class);

    @Autowired
    TaskQueue_Scan_to_1 taskQueueScanTo1;

    @Autowired
    Stage1Engine_PrepareCampTask stage1EnginePrepareCampTask;

    @Autowired
    private CampInfoDao campInfoDao;

    @Autowired
    private Stage91Engine_SmsRecvPollTask stage91EngineSmsRecvPollTask;

    @Autowired
    private Stage92Engine_SmsReplyPollTask stage92EngineSmsReplyPollTask;


//    @Autowired
    private HBaseConnectionMgr connectionMgr;

    @Autowired
    private HealthStatsManager healthStatsManager;

    private AtomicLong countDoScan = new AtomicLong(0);

    private AtomicLong countQueueItem = new AtomicLong(0);

    private final String statsTitle;


    private final Set<String> sessionTrack_campsHadSendToStage1_Prepare = new ConcurrentHashSet<>();



    public Stage1PrepareScanJob() {
        statsTitle = this.getClass().getSimpleName() + "-" + this.hashCode();
        logger.info("------------ create new job {}", this.hashCode());
    }

    @PostConstruct
    public void init() {
        healthStatsManager.register(this);
    }


    public void scanCommitCampaignsNeedDoPrepare() {

        List<CampInfo>  campInfos = campInfoDao.getCommitOrApprovedCampaigns();

        for(CampInfo campInfo: campInfos) {

            if(sessionTrack_campsHadSendToStage1_Prepare.contains(campInfo.getCamp_id())) {
                // Skip as in my live session, I had already send this camp to Prepare queue.
                continue;

            } else {

                try ( Connection hBaseCon = connectionMgr.getConnection();
                      CampaignActionStore campaignActionStore = CampaignActionStoreHBaseImpl.createInstance(campInfo.getCamp_id() , hBaseCon)) {

                    TCampInfo campInfoRecord = campaignActionStore.findOneCampInfoRecordWithoutUsersBitSet(campInfo.getCamp_id(), false);


                    boolean needDoPrepare = false;
                    if(campInfoRecord == null) {
                        needDoPrepare = true;
                    } else {
                        if ( CampaignActionStatus.PREPARE.toString().equals(campInfoRecord.getStatus()) ) {
                            needDoPrepare = true;
                        }
                    }

                    if(needDoPrepare) {

                        taskQueueScanTo1.produce( campInfo );
                        sessionTrack_campsHadSendToStage1_Prepare.add(campInfo.getCamp_id());

                        stage1EnginePrepareCampTask.beginWork();

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }



        }

    }



    public void doScan() {
        countDoScan.getAndIncrement();
        logger.info("=============  doScan() started =============");

        try {

            scanCommitCampaignsNeedDoPrepare();

            stage91EngineSmsRecvPollTask.beginWork();

            stage92EngineSmsReplyPollTask.beginWork();

        } catch (Throwable e) {

            logger.info("============= {} " +this + "doScan() started failed with Exception:", e);

        }


        logger.info("=============  doScan() end =============");
    }

    @Override
    public String getStatsTitle() {
        return statsTitle;
    }

    @Override
    public Map<String, String> getStatsMap() {
        Map<String, String> statsMap = new HashMap<>();
        statsMap.put("doScan", countDoScan.toString());
        statsMap.put("queueItem", countQueueItem.toString());
        return statsMap;
    }
}
