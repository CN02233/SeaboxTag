package com.seabox.tagsys.persongroup.service;

import com.seabox.tagsys.usertags.hbase.HBaseConnectionMgr;
import com.seabox.test.base.BaseTestClass;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Table;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;

import java.io.IOException;
import java.util.*;

/**
 * 造数到hbase 包含用户的信息表和用户与标签对应表
 * 用户总数为MaxThreadVal*MaxUser_EachThread
 * Created by SongChaoqun on 2016/12/13.
 */

public class CreateUserTagInfo2Hbase extends BaseTestClass{

    Logger logger = LoggerFactory.getLogger(CreateUserTagInfo2Hbase.class);

    Random random = new Random();

    @Autowired
    private HBaseConnectionMgr connectionMgr;

    @Autowired
    private JedisPool jedisPool;

    int MaxThreadVal = 10;

    int MaxUser_EachThread = 200000;


    List<Map<String, Object>> tagList ;

    @Test
    public void removeFromRedis(){
        Jedis resource = jedisPool.getResource();
        Set<String> allTags = resource.keys("t:*");
        for(String tag:allTags){
            logger.debug("del :"+tag);
            resource.del(tag);
        }
    }

//    @Test
    public void getVal() throws IOException {
        Table userTable = connectionMgr.getConnection().getTable(TableName.valueOf("h62_indv_info"));
        Get getObj = new Get("0".getBytes());
        Result result = userTable.get(getObj);
        byte[] val = result.getValue("cust_info".getBytes(), "user_age".getBytes());
        System.out.println(new String(val));
    }

    @Test
    public void mainMethod() throws IOException, InterruptedException {
        userToHbase();
//        tagToRedis();
        logger.debug("main method has end.create user has done");
    }

    private void tagToRedis() throws InterruptedException {

        tagList = getTagList();

        List<Thread> threadTmps = new ArrayList<>();
        for(int i=0;i<MaxThreadVal;i++){
            CreateUserThread createThread = new CreateUserThread(i,null,"R");
            createThread.start();
            threadTmps.add(createThread);
        }

        for(Thread thread:threadTmps){
            thread.join();
        }
    }


    private boolean randomTrueOrFalse(){
        if(random.nextInt(2)==1){
            return true;
        }
        return false;
    }

    private void userToHbase() throws IOException, InterruptedException {

        List<Thread> threadTmps = new ArrayList<>();
        for(int i=0;i<MaxThreadVal;i++){
            Table userTable = connectionMgr.getConnection().getTable(TableName.valueOf("h62_indv_info"));
            CreateUserThread createThread = new CreateUserThread(i,userTable,null);
            createThread.start();
            threadTmps.add(createThread);
        }

        for(Thread thread:threadTmps){
            thread.join();
        }

        logger.debug("main method has end.create user has done");
    }

    private List<Map<String,Object>> getTagList(){
        List<Map<String,Object>> tagResults = mybatisTemplate.selectList("prnScreening.listTags");
        return tagResults;
    }

    private int randomSex(){
        return random.nextInt(2);
    }

    private int randomAge(){
        int randomAge = random.nextInt(100);
        while(randomAge<10){
            randomAge  = random.nextInt(100);
        }

        return randomAge;
    }

    private String randomYesOrNo(){
        if(random.nextInt(2)==1){
            return "Y";
        }
        return "N";
    }

    private int randomTagCont(){
        int randomTagCont = random.nextInt(20);
        while(randomTagCont<6){
            randomTagCont  = random.nextInt(20);
        }
        return randomTagCont;
    }

    private int randomTagPlace(List<Integer> hasCheckPlace,int tagListSize){
        int randomTagPlace = random.nextInt(tagListSize);
        while (hasCheckPlace.contains(randomTagPlace)){
            randomTagPlace = random.nextInt(tagListSize);
        }
        return randomTagPlace;
    }

    private Put createUser(int userRowNum){

        Put userPut = new Put(new Integer(userRowNum).toString().getBytes());
        int sex = randomSex();
        int age = randomAge();
        String hasMarried = randomYesOrNo();
//        logger.debug("now user row is |"+sex+"|"+age+"|"+hasMarried+"|");
        userPut.addColumn("cust_info".getBytes(),"user_nm".getBytes(), new StringBuffer().append("User-").append(userRowNum).toString().getBytes());
        userPut.addColumn("cust_info".getBytes(),"user_sex".getBytes(), new Integer(sex).toString().getBytes());
        userPut.addColumn("cust_info".getBytes(),"user_age".getBytes(), new Integer(age).toString().getBytes());
        userPut.addColumn("cust_info".getBytes(),"is_married".getBytes(), hasMarried.getBytes());
        return userPut;
    }

    private List<String> createUserTags(){
        List<String> userTagList = new ArrayList<>();
        int tagCount = randomTagCont();
        List<Integer> hasCheckPlace = new ArrayList<>();
        for(int i=0;i<tagCount;i++){
            int randomTagPlace = randomTagPlace(hasCheckPlace, tagList.size());
            Integer tagId = (Integer) tagList.get(randomTagPlace).get("tagId");
            String realTagId = "t:"+tagId.toString();
            userTagList.add(realTagId);
        }
        return userTagList;
    }

    class CreateUserThread extends Thread{
        private int ThreadNum;

        private Table userTable;

        private String runType = "H";

        public CreateUserThread(int ThreadNum,Table userTable,String runType){
            this.ThreadNum = ThreadNum;
            this.userTable = userTable;
            this.runType = runType;
        }

        @Override
        public void run(){
            if("R".equals(runType)){
                logger.debug("now thread is working for ### Redis ####.......");
//                List<String> test = createUserTags();
//                logger.debug("test val is "+test);
                work4Redis();
            }else{
                logger.debug("now thread is working for ### hbase ####.......");
                work4Hbase();
            }

        }

        private void work4Redis(){
            Jedis resource = jedisPool.getResource();

            Pipeline pipelined = resource.pipelined();
            int startNum = ThreadNum*MaxUser_EachThread;
            int forTime = 0;

            logger.debug("thread will running,thread name is "+Thread.currentThread()+",and start number is "+startNum+",end number is "+(startNum+MaxUser_EachThread-1));

            for(int i=startNum;i<(startNum+MaxUser_EachThread-1);i++){
                List<String> tagList = createUserTags();//每个用户循环调用这个方法,随机出来对应的标签
                for(String tagStr :tagList){
                    pipelined.setbit(tagStr,i,true);
                    forTime++;
                    if(forTime==10000){
                        logger.debug("thread :"+Thread.currentThread().getName()+" has check ,redis data will be commit.");
                        pipelined.sync();
                        logger.debug("thread :"+Thread.currentThread().getName()+" redis data commit done.");
                        pipelined = resource.pipelined();
                        logger.debug("thread :"+Thread.currentThread().getName()+" create new pipelined has done.");
                        forTime = 0;
                    }
                }
            }
            pipelined.sync();
            logger.debug("thread :"+Thread.currentThread().getName()+" has done");
        }

        private void work4Hbase(){
            int startNum = ThreadNum*MaxUser_EachThread;
            logger.debug("now start thread " +Thread.currentThread().getName()+"-->start space number is "+startNum+" and end number is "+(startNum+MaxUser_EachThread));
            List<Put> userList = new ArrayList<>();
            try {
                for(int i=startNum;i<(startNum+MaxUser_EachThread);i++){
                    Put userObj = createUser(i);
                    userList.add(userObj);
                    if(userList.size()==1000){

                        userTable.put(userList);

                        userList = new ArrayList<>();
                    }
                }
                userTable.put(userList);
            } catch (IOException e) {
                e.printStackTrace();
            }
            logger.debug("thread " +Thread.currentThread().getName()+" has done");
        }

    }


    @Test
    public void createUserTagsTest(){
        tagList = getTagList();

        for(int i=0;i<10;i++){
            List<String> result = createUserTags();
            System.out.println(result);
        }
    }

}


