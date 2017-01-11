package com.seabox.tagsys.usertags.action.job.engine.stage1;

import com.seabox.tagsys.usertags.action.CampaignActionStatus;
import com.seabox.tagsys.usertags.action.job.ControllableThreadBase;
import com.seabox.tagsys.usertags.action.job.engine.ProcessStage;
import com.seabox.tagsys.usertags.action.job.engine.TaskQueue_Scan_to_1;
import com.seabox.tagsys.usertags.entity.CampInfo;
import com.seabox.tagsys.usertags.hbase.CampaignActionStore;
import com.seabox.tagsys.usertags.hbase.CampaignActionStoreHBaseImpl;
import com.seabox.tagsys.usertags.hbase.HBaseConnectionMgr;
import com.seabox.tagsys.usertags.hbase.entity.TCampInfo;
import com.seabox.tagsys.usertags.mybatis.dao.CampInfoDao;
import com.seabox.tagsys.usertags.service.BigTaskBreakUtil;
import com.seabox.tagsys.usertags.service.TaskBreakExecutor;
import com.seabox.tagsys.usertags.service.impl.CacheServiceRedisImpl;
import com.seabox.tagsys.usertags.utils.SingletonBean;
import com.seabox.tagsys.usertags.utils.UserTagUtils;
import org.apache.hadoop.hbase.client.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.PreDestroy;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

/**
 *  prepare Stage:  Redis GUIDs for Camp --> Hbase
 *
 * Created by wuchh on 3/9/16.
 */
//@SingletonBean
public class Stage1Engine_PrepareCampTask extends ControllableThreadBase implements ProcessStage<CampInfo> {

    private static final Logger logger = LoggerFactory.getLogger(Stage1Engine_PrepareCampTask.class);

    @Autowired
    private TaskQueue_Scan_to_1 taskQueue;

//    @Autowired
    private HBaseConnectionMgr connectionMgr;

    @Autowired
    private CampInfoDao    campInfoDao;

    @Autowired
    private JedisPool jedisPool;

    private AtomicLong count_prepareAction = new AtomicLong(0);


    private ExecutorService stageExecutor = Executors.newFixedThreadPool(10); // maximum 10 in-parallel allowed

    public final static int MAX_THREADS_FOR_SUB = 30;


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
                prepareAction( campInfoToExec );
            }
        };

        stageExecutor.submit( runPrepareAction );

    }


    @Override
    public void doWork() {

        try {
            CampInfo campInfo = taskQueue.blockingConsume();

            processStage(campInfo);

        } catch (InterruptedException e) {
            logger.error("I was Interrupted : " + this, e);

            Thread myThread = Thread.currentThread();
            myThread.interrupt();
        }

    }


    @Override
    public Map<String, String> getStatsMap() {
        Map<String, String>  statsMap = super.getStatsMap();
        statsMap.put("prepareAction", count_prepareAction.toString());
        return statsMap;
    }



    public class CampPrepareUsersExecutor extends TaskBreakExecutor {


        private String campId;
        private BitSet localBitSet;
        private CampaignActionStore campaignActionStore;
        private  Set<String> excludeUsers;


        public CampPrepareUsersExecutor(String campId, BitSet localBitSet, CampaignActionStore campaignActionStore, Set<String> excludeUsers) {

            super("CampPrepareUsersExecutor prepareAction-subTasks :campId=" + campId, MAX_THREADS_FOR_SUB, localBitSet.length(), true);

            this.campId = campId;
            this.localBitSet = localBitSet;
            this.campaignActionStore = campaignActionStore;
            this.excludeUsers = excludeUsers;
        }


        @Override
        public void execOne(int taskId, long index) {
            // dummy, we use  execRange
        }


        @Override
        public void execRange(int taskId, long start, long end) {

            int nBatch = 0;

            for(long l=start; l<end;){
                countStart( taskId );
                int setIndex = localBitSet.nextSetBit( (int)l );
                if(setIndex>=end) {
                    break;
                }

                ++nBatch;

                String userGUID = String.valueOf(setIndex);
                boolean result = campaignActionStore.addUserToCampaignAction(userGUID, excludeUsers);
                if(result) {
                    countComplete(taskId);
                } else {
                    countError(taskId);
                }

                l = setIndex+1;
            }

        }

        @Override
        protected void init() {

        }

        @Override
        public void onComplete(int taskId) {

        }

    }



    public void  prepareAction(CampInfo campInfo) {

        String campId = campInfo.getCamp_id();

        count_prepareAction.getAndIncrement();


        logger.info("============= started prepareAction() for camp:{} =============", campId );


        String camp_channel = campInfo.getCamp_chnl_cd();
        String  camp_industry = campInfo.getCamp_inds_cd();
        Set<String> excludeUsersFromMysql = campInfoDao.getExcludeUserList(camp_channel, camp_industry);

        if(null == excludeUsersFromMysql) {
            excludeUsersFromMysql = new HashSet<>(); //default, Empty Set
        }

        final Set<String> excludeUsers = excludeUsersFromMysql;

        try ( Connection hBaseCon = connectionMgr.getConnection();
              CampaignActionStore campaignActionStore = CampaignActionStoreHBaseImpl.createInstance(campId, hBaseCon)) {

            // set true to load userBitSet from hbase
            TCampInfo campInfoRecord = campaignActionStore.findOneCampInfoRecordWithoutUsersBitSet(campId, true);

            if(campInfoRecord == null) {

                logger.error("============= prepareAction() Failed for camp:{} , can not find campInfoRecord in hbase  =============", campId );

            } else {

                final String campUsersKeyInRedis = UserTagUtils.getCampBitSetRedisKey(campId);


                try (Jedis jedis = jedisPool.getResource()) {

                    byte[] redisBitSet = campInfoRecord.getUsersBitSet();

                    final String campUsersKeyRedis = UserTagUtils.getCampBitSetRedisKey(campId);

                    if(null== redisBitSet && jedis.exists(campUsersKeyInRedis)) {
                        redisBitSet = jedis.get(campUsersKeyRedis.getBytes()); // load from redis if it's missing in hbase
                    }


                    if( null== redisBitSet) {

                        campaignActionStore.updateCampActionDetailStatus(CampaignActionStatus.PREPARE_FAILED );
                        logger.error("============= prepareAction() Failed for camp:{} , usersKeyRedis && usersBitSet(in bytes) does'nt exist =============", campId );

                    } else {

                        final BitSet localBitSet = CacheServiceRedisImpl.fromByteArrayReverse(redisBitSet);

                        CampPrepareUsersExecutor executor = new CampPrepareUsersExecutor(campId, localBitSet, campaignActionStore, excludeUsers);

                        BigTaskBreakUtil.breakExec( executor);

                        campaignActionStore.updateCampActionDetailStatus(CampaignActionStatus.READY );

                        logger.info("prepareAction() campId={} saved OK, it's READY to action", campId);


                    }

                }



            }

            logger.info("============= complete prepareAction() for camp:{} =============", campId );

        } catch (Throwable e) {
            logger.error("============= prepareAction() failed for camp:" + campId + " =============", e);
        }




    }

}
