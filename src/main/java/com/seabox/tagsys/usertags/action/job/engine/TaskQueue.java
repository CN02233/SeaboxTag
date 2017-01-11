package com.seabox.tagsys.usertags.action.job.engine;

import com.seabox.tagsys.usertags.entity.CampInfo;
import com.seabox.tagsys.usertags.utils.SingletonBean;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @author Changhua, Wu
 *         Created on: 2/17/16,11:07 PM
 */

public class TaskQueue<T> {


    BlockingQueue<T> queue = new ArrayBlockingQueue<>(1000);

    public void  produce(T item) {
        queue.offer( item );
    }

    public T blockingConsume() throws InterruptedException{
        return queue.take();
    }

}
