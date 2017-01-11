package com.seabox.tagsys.usertags.action.job.schedule;

import com.alibaba.dubbo.common.utils.ConcurrentHashSet;
import com.seabox.tagsys.usertags.action.CampaignActionStatus;
import com.seabox.tagsys.usertags.action.job.HealthStatsInstance;
import com.seabox.tagsys.usertags.action.job.HealthStatsManager;
import com.seabox.tagsys.usertags.action.job.engine.TaskQueue_Scan_to_2;
import com.seabox.tagsys.usertags.action.job.engine.stage2.Stage2Engine_SmsActionTask;
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
public class Stage2ActionScanJob implements HealthStatsInstance {


    private static final Logger logger = LoggerFactory.getLogger(Stage2ActionScanJob.class);

    @Autowired
    private CampInfoDao campInfoDao;

    @Autowired
    private Stage2Engine_SmsActionTask stage2EngineSmsActionTask;


    @Autowired
    TaskQueue_Scan_to_2 taskQueueScanTo2;

//    @Autowired
    private HBaseConnectionMgr connectionMgr;

    @Autowired
    private HealthStatsManager healthStatsManager;

    private AtomicLong countDoScan = new AtomicLong(0);

    private AtomicLong countQueueItem = new AtomicLong(0);

    private final String statsTitle;


    private final Set<String> sessionTrack_campsHadSendToStage2_Action = new ConcurrentHashSet<>();


    public Stage2ActionScanJob() {
        statsTitle = this.getClass().getSimpleName() + "-" + this.hashCode();
        logger.info("------------ create new job {}", this.hashCode());
    }

    @PostConstruct
    public void init() {
        healthStatsManager.register(this);
    }



    public void scanReadyCampaignsNeedAction() {


        List<CampInfo>  campInfos = campInfoDao.getApprovedOrActionCampaignsNeedExec();

        for(CampInfo campInfo: campInfos) {

            if(logger.isTraceEnabled()) {
                logger.trace("---- now checking  campInfo: {}", campInfo );
            }

            if (sessionTrack_campsHadSendToStage2_Action.contains(campInfo.getCamp_id())) {
                // Skip as in my live session, I had already send this camp to Action queue.
                logger.info("---- camp: {} was already in my Action exec queue, not need add again!" , campInfo);

                continue;

            } else {
                try ( Connection hBaseCon = connectionMgr.getConnection();
                      CampaignActionStore campaignActionStore = CampaignActionStoreHBaseImpl.createInstance(campInfo.getCamp_id() , hBaseCon)) {

                    TCampInfo campInfoRecord = campaignActionStore.findOneCampInfoRecordWithoutUsersBitSet(campInfo.getCamp_id(), false);

                    boolean needDoAction = false;

                    if(campInfoRecord != null) {

                        if (campInfoRecord.getStatus().equals( CampaignActionStatus.READY.toString() ) // first time do Action
                                || campInfoRecord.getStatus().equals( CampaignActionStatus.ACTION.toString() ) // resume from last partial work from Shutdown
                                || campInfoRecord.getStatus().equals( CampaignActionStatus.ACTION_RESUME.toString() )) {

                            needDoAction = true;
                        }

                        logger.info("---- camp:{}   exec-status={} , needDoAction={}", campInfo, campInfoRecord.getStatus(), needDoAction );

                    } else {
                        logger.error("---- camp:{} , this camp Not found from hBase record!", campInfo);
                    }

                    if(needDoAction) {
                        List<String>  smsTemplateParas = campInfoDao.getCampSmsTemplateParameters(campInfo.getCamp_id());
                        campInfo.setTemplateParameters(smsTemplateParas);
                        taskQueueScanTo2.produce(campInfo);
                        sessionTrack_campsHadSendToStage2_Action.add(campInfo.getCamp_id());
                        countQueueItem.getAndIncrement();

                        stage2EngineSmsActionTask.beginWork();
                    }

                } catch (IOException e) {
                    logger.error("---- camp:" + campInfo + " encounter Exception: ", e );
                }

            }



        }

    }

    public void doScan() {
        countDoScan.getAndIncrement();
        logger.info("=============  doScan() started =============");

        try {

            scanReadyCampaignsNeedAction();

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
