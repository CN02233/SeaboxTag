package com.seabox.tagsys.usertags.action.job.engine.stage2;

import com.seabox.tagsys.usertags.action.job.ControllableThreadBase;
import com.seabox.tagsys.usertags.action.job.engine.ProcessStage;
import com.seabox.tagsys.usertags.action.job.engine.SmsTaskControl;
import com.seabox.tagsys.usertags.action.job.engine.TaskQueue_Scan_to_2;
//import com.seabox.tagsys.usertags.action.sms.SmsApiWrapService;
import com.seabox.tagsys.usertags.entity.CampInfo;
import com.seabox.tagsys.usertags.hbase.CampaignActionStoreHBaseImpl;
import com.seabox.tagsys.usertags.hbase.HBaseConnectionMgr;
import com.seabox.tagsys.usertags.mybatis.dao.CampInfoDao;
import com.seabox.tagsys.usertags.service.impl.CacheServiceRedisImpl;
import com.seabox.tagsys.usertags.utils.SingletonBean;
import com.seabox.tagsys.usertags.utils.UserTagUtils;
import org.apache.hadoop.hbase.client.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.BitOP;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;


/**
 * @author Changhua, Wu
 *         Created on: 2/17/16,10:59 PM
 */
//@SingletonBean
public class Stage2Engine_SmsActionTask extends ControllableThreadBase implements ProcessStage<CampInfo> {

    private static final Logger  logger = LoggerFactory.getLogger(Stage2Engine_SmsActionTask.class);

    @Autowired
    private TaskQueue_Scan_to_2 queue;


    private AtomicLong countProcessedQueueItem = new AtomicLong(0);

    @Autowired
    private CampInfoDao campInfoDao;

//    @Autowired
    private HBaseConnectionMgr   connectionMgr;

//    @Autowired
//    private SmsApiWrapService smsApiWrapService;


    @Autowired
    private SmsTaskControl smsTaskControl;


    @Autowired
    private JedisPool jedisPool;

    private ExecutorService stageExecutor = Executors.newFixedThreadPool(10); // maximum 10 in-parallel allowed


    public Stage2Engine_SmsActionTask() {

    }


    @PreDestroy
    public void shutdown() {

        stageExecutor.shutdownNow();

    }

    @Override
    public void processStage(CampInfo campInfo) {

        final CampInfo campInfoToExec = campInfo;

        Runnable runPrepareAction = new Runnable() {
            @Override
            public void run() {
                execSmsSendAction(campInfoToExec );
            }
        };

        stageExecutor.submit( runPrepareAction );

    }


    @Override
    public void doWork() {

        try {
            CampInfo campInfo = queue.blockingConsume();

            processStage(campInfo);

        } catch (InterruptedException e) {
            logger.error("I was Interrupted : " + this, e);

            Thread myThread = Thread.currentThread();
            myThread.interrupt();
        }

    }


    public void execSmsSendAction( CampInfo campInfo ) {

        countProcessedQueueItem.getAndIncrement();

        logger.info("============= get a new task for camp start:{} =============", campInfo );

        try ( Connection hBaseCon = connectionMgr.getConnection() ){
            logger.info("now try to update status for camp: {}  , new camp_status_cd = {}", campInfo, "04");
            campInfoDao.updateCampExecutionStatus(campInfo.getCamp_id(), "04"); // | 04             | 执行中
            CampInfo newEntry = campInfoDao.getCampaignInfo(campInfo.getCamp_id());
            logger.info("dump camp after update status to exec-ongoing for camp: {}", newEntry);

            Jedis jedis = jedisPool.getResource();
            if( jedis != null) {

                String campBitMapRedisKey = UserTagUtils.getCampBitSetRedisKey(campInfo.getCamp_id());

                if(CacheServiceRedisImpl.ENABLE_GLOBAL_FILTER) {
                    // when camp is action, update the global exclude List
                    jedis.bitop(BitOP.OR, UserTagUtils.GLOBAL_EXCLUDE_USERS_KEY, campBitMapRedisKey, UserTagUtils.GLOBAL_EXCLUDE_USERS_KEY);
                }


            }

            // action Stage: send SMS
            try( CampaignActionStoreHBaseImpl campaignActionStore = CampaignActionStoreHBaseImpl.createInstance( String.valueOf(campInfo.getCamp_id()), hBaseCon , jedis ) ) {
                campaignActionStore.performAction(campInfo, null , smsTaskControl);
//                campaignActionStore.performAction(campInfo, smsApiWrapService , smsTaskControl);
            }

            logger.info("now try to update status for camp: {}  , new camp_status_cd = {}", campInfo, "05");
            campInfoDao.updateCampExecutionStatus(campInfo.getCamp_id(), "05" ); //| 05             | 已完成
            newEntry = campInfoDao.getCampaignInfo(campInfo.getCamp_id());
            logger.info("dump camp after update status to Complete for camp: {}", newEntry);

        } catch (IOException e) {

            logger.info("============= camp Action " + campInfo + " encounter hbase access Exception:", e);

        } catch (Throwable e) {

            logger.info("============= camp Action " + campInfo + " failed with Exception:", e);

        }


        logger.info("============= camp Action Done: {} =============", campInfo );

    }

    @Override
    public Map<String, String>  getStatsMap() {
        Map<String, String>  statsMap = super.getStatsMap();
        statsMap.put("queueItem", countProcessedQueueItem.toString());
        return statsMap;
    }


}
