package com.seabox.tagsys.usertags.action.job.engine.stage0;

import com.seabox.tagsys.usertags.action.job.engine.ProcessStep;
import com.seabox.tagsys.usertags.hbase.CampaignActionStore;
import com.seabox.tagsys.usertags.hbase.CampaignActionStoreHBaseImpl;
import com.seabox.tagsys.usertags.hbase.HBaseConnectionMgr;
import com.seabox.tagsys.usertags.logicquery.TagCondition;
import com.seabox.tagsys.usertags.utils.SingletonBean;
import com.seabox.tagsys.usertags.utils.UserTagUtils;
import org.apache.hadoop.hbase.client.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

/**
 * Created by wuchh on 3/10/16.
 */
@SingletonBean
public class StepBProcess_SaveCampInfoToHBase implements ProcessStep<StepAOutput, StepBOutput> {

    private static final Logger logger = LoggerFactory.getLogger(StepBProcess_SaveCampInfoToHBase.class);

    @Autowired
    private HBaseConnectionMgr connectionMgr;

    @Override
    public void beforeProcess() {

    }

    @Override
    public StepBOutput process(StepAOutput input) {

        logger.info("process() start  for campId: {}", input.getCampId() );

        String  campId = input.getCampId();

        try (Connection hBaseCon = connectionMgr.getConnection()) {

            try (CampaignActionStore campaignActionStore = CampaignActionStoreHBaseImpl.createInstance(campId, hBaseCon)){

                if(campaignActionStore == null) {
                    logger.error("error on hBase access while try to commitAndSaveCacheForCampaign() for campId: {}", campId);
                    return null;
                }

                TagCondition condition = input.getStepAInput().getCondition();
                String tagConditionJson = UserTagUtils.objectToJson(condition);

                long numOfUsers = input.getNumsGUIDInKeyForCamp();

                campaignActionStore.saveCampaignInfo( input.getRedisBitSetInBytes(), tagConditionJson, numOfUsers);

                StepBOutput stepBOutput = new StepBOutput();
                stepBOutput.setCampId( campId );


                logger.info("process() done  for campId: {}", campId );

                return stepBOutput;
            } catch (Throwable e) {
                logger.error("process() 1 failed with Exception  for campId: " + campId, e);
            }


        } catch (IOException e) {

            logger.error("process() 2 failed with Exception  for campId: " + campId, e);

        }
        return null;
    }

    @Override
    public void afterProcess() {

    }


}
