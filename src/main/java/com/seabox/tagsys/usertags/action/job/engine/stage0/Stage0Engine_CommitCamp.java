package com.seabox.tagsys.usertags.action.job.engine.stage0;

import com.seabox.tagsys.usertags.action.job.engine.ProcessStage;
import com.seabox.tagsys.usertags.utils.SingletonBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by wuchh on 3/9/16.
 */
@SingletonBean
public class Stage0Engine_CommitCamp implements ProcessStage<StepAInput>{

    private static final Logger logger = LoggerFactory.getLogger(Stage0Engine_CommitCamp.class);

    @Autowired
    StepAProcess_SaveGuidsToRedis stepAProcess;

    @Autowired
    StepBProcess_SaveCampInfoToHBase stepBProcess;


    public void processStage(StepAInput input) {

        //用户redis bit是在这里产生的-->redisBitSetInBytes
        //每个标签下存放的是用户的位图,例如标签:t:123对应的值是011101010010101010,1和0分别代表是否命中了排序中这个位置的用户
        StepAOutput stepAOutput = stepAProcess.process(input);

        if(null!= stepAOutput) {
            StepBOutput stepBOutput = stepBProcess.process(stepAOutput);
        }

    }


}
