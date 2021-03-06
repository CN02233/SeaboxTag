package com.seabox.tagsys.usertags.action.job.engine;

import com.seabox.tagsys.usertags.utils.SingletonBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.Calendar;

/**
 * Created by wuchh on 3/11/16.
 */
@SingletonBean
public class SmsTaskManager implements SmsTaskControl{

    private static final Logger logger = LoggerFactory.getLogger(SmsTaskManager.class);

    private boolean   working = true;


    @PostConstruct
    public void init() {
        Calendar now = Calendar.getInstance();
        int  hour = now.get(Calendar.HOUR_OF_DAY);
        // 20:00 ~ 08:00
        if( hour>=20 || hour <8) {
            working = false;
        } else {
            working = true;
        }

    }



    public boolean isWorking() {
        return working;
    }



    public void pauseExecTask() {
        synchronized(this) {
            working = false;
        }
        logger.info("=========== pauseExecTask() ===========");
    }


    public void resumeExecTask() {
        logger.info("=========== resumeExecTask() ===========");
        synchronized(this) {
            working = true;
            this.notifyAll(); // allow all paused task goes back to work
        }
    }

    public void waitForResume(Object taskObj) throws InterruptedException {
        logger.info("=========== {} is waitForResume() ===========", taskObj );
        synchronized(this) {
            this.wait();
        }

    }

}
