package com.seabox.tagsys.usertags.action.job;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Changhua, Wu
 *         Created on: 2/19/16,2:17 PM
 */
public abstract class ControllableThreadBase extends Thread implements HealthStatsInstance {

    private volatile boolean     work = false;

    private ReentrantLock lock;
    private Condition startCondition;
    private Condition   stopCondition;

    @Autowired
    private HealthStatsManager healthStatsManager;
    private AtomicLong countDoWork = new AtomicLong(0);
    private AtomicLong countDoWorkException = new AtomicLong(0);

    private static final Logger logger = LoggerFactory.getLogger(ControllableThreadBase.class);

    public ControllableThreadBase () {
        lock = new ReentrantLock();
        startCondition = lock.newCondition();
        stopCondition = lock.newCondition();
    }

    @Override
    public String getStatsTitle() {
        return getName();
    }

    @Override
    public Map<String, String>  getStatsMap() {
        Map<String, String> statsMap = new HashMap<>();
        statsMap.put("doWork", countDoWork.toString());
        statsMap.put("doWorkError",  countDoWorkException.toString());
        return statsMap;
    }


    @PostConstruct
    public void init() {
        this.setName( this.getClass().getSimpleName() + "-" + this.hashCode() );
        healthStatsManager.register( this );
        logger.info("============== create new running task for: {}", getName());
        this.start();
    }


    public void beginWork() {

        try {
            lock.lock();
            if( !work) {
                work = true;
            }
            startCondition.signalAll();
        } finally {
            lock.unlock();
        }

    }

    @PreDestroy
    public void stopWork() {
        logger.info(" stopWork() invoked  for {} object={}", getName(), this);
        try {
            lock.lock();
            work = false;
            try {
                stopCondition.await(100, TimeUnit.MILLISECONDS);
                if(this.isAlive()) {
                    this.interrupt();
                }
            } catch (InterruptedException e) {
                if(lock.isHeldByCurrentThread()) {
                    lock.unlock();
                }
                logger.error("I was Interrupted : " + this , e);

                Thread myThread = Thread.currentThread();
                myThread.interrupt();
            }
        } finally {
            lock.unlock();
        }

    }




    public abstract void doWork();

    @Override
    public void run() {

        try {
            logger.info("============= {} run() initial {} =============" ,getName(), this);

            lock.lock();
            startCondition.await();
            lock.unlock();

            logger.info("============= {} begin to work {} =============", getName(), this );

            while (work) {
                try {
                    countDoWork.getAndIncrement();
                    doWork();
                } catch (Throwable e) {
                    countDoWorkException.getAndIncrement();
                }
            }

            lock.lock();
            stopCondition.signalAll();
            lock.unlock();

        } catch (InterruptedException e) {
            if(lock.isHeldByCurrentThread()) {
                lock.unlock();
            }

            logger.error("I was Interrupted : " + this , e);

            Thread myThread = Thread.currentThread();
            myThread.interrupt();

        }
    }
}
