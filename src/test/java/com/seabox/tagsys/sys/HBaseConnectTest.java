package com.seabox.tagsys.sys;

import com.seabox.tagsys.usertags.hbase.HBaseUtil;
import com.seabox.tagsys.usertags.hbase.entity.TCallAction;
import com.seabox.tagsys.usertags.hbase.entity.TUserInfo;
import com.seabox.test.base.BaseTestClass;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Hash;
import org.junit.Test;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by pc on 2016/12/9.
 */
public class HBaseConnectTest extends BaseTestClass{

    private final int maxThread = 1;
    private final int maxPutEachThread = 30000;

    private Map<String,AtomicInteger> threadMap = new HashMap<>();

    private List<String> threadList = new ArrayList<>();

    private static final Condition condition;

    private static final Lock lock;

    static {
        lock = new ReentrantLock();
        condition = lock.newCondition();
    }


    public String getData() throws IOException {

        Configuration configuration = HBaseConfiguration.create();
        Connection connection = ConnectionFactory.createConnection(configuration);

        Get getObj = new Get("46".getBytes());
        getObj.addColumn("cust_info".getBytes(),"has_married".getBytes());
        Table table = connection.getTable(TableName.valueOf("h62_indv_info"));
        Result result = table.get(getObj);
//        byte[] resVal = result.getValue("cust_info".getBytes(), "mov_tel".getBytes());
//        String restStr = String.valueOf(resVal);
        byte[] valur = result.value();
        System.out.println(new String(valur));

//        String mobile = HBaseUtil.getValueAsString(result, TUserInfo._CustInfo.ColFamily(), TUserInfo._CustInfo.col_mobile());
//        findObj.setMobileNo( mobile );

        return null;
    }


    @Test
    public void testCreateTable() throws IOException, InterruptedException {
//        createTableIfMissing
        System.setProperty("hadoop.home.dir", "D:\\installer\\work\\hadoop\\");
        Configuration configuration = HBaseConfiguration.create();
        Connection connection = ConnectionFactory.createConnection(configuration);

        TableName tableNAME = TableName.valueOf("h62_indv_info");


//        Table tableFrmHbase = connection.getTable(tableNAME);
//        TCallAction.createTableIfMissing(connection);
//
////        TUserInfo.createTableIfMissing(connection);
//        TUserInfo userIfo = TUserInfo.DataForDevTest.findById_createIfMissing(tableFrmHbase, "123456");
//        userIfo.getMobileNo();
//        userIfo.getUserName();

        testSetValueToUser(connection);


//        getData(connection);
//        System.out.println(userIfo);
    }


    private void testSetValueToUser(Connection connection) throws IOException {
        try(final Table tableInfo = connection.getTable(TableName.valueOf("h62_indv_info"))){

            class InsertThread implements Runnable{

                private final int threadCnt;

                public InsertThread(int threadCnt ){
                    this.threadCnt = threadCnt;
                }

                @Override
                public void run() {
                    System.out.println("开始运行线程:"+Thread.currentThread().getName());

                    List<Put> list = new ArrayList<>();
                    int putNum = 0;
                    for(int i=0;i<maxPutEachThread;i++){
                        StringBuffer userNameSb = new StringBuffer();
                        userNameSb.append("User-");
                        userNameSb.append(threadCnt);
                        userNameSb.append("-");
                        userNameSb.append(i);
                        String userNm = userNameSb.toString();
                        Random random = new Random();
                        int randomAge = random.nextInt(100);

                        Put putObj = new Put(userNm.getBytes());
                        putObj.addColumn("cust_info".getBytes(),"mov_tel".getBytes(),"13412341234".getBytes());
//                    putObj.addColumn("cust_info".getBytes(),"user_nm".getBytes(),"13412341234".getBytes());
                        putObj.addColumn("cust_info".getBytes(),"user_sex".getBytes(),random.nextInt(2)==0?"1".getBytes():"0".getBytes());
                        putObj.addColumn("cust_info".getBytes(),"user_age".getBytes(),new Integer(randomAge).toString().getBytes());
                        putObj.addColumn("cust_info".getBytes(),"has_married".getBytes(),
                                random.nextInt(2)==0?"Y".getBytes():"N".getBytes());

                        list.add(putObj);

                        putNum++;
                        if(putNum==1000){
                            putNum = 0;
                            try {
                                tableInfo.put(list);
                                System.out.println(Thread.currentThread().getName()+"开始插入数据,数据数量"+list.size()+".当前I值:"+i);

                                list =  new ArrayList<>();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                    }

                    try {
                        tableInfo.put(list);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    System.out.println(Thread.currentThread().getName()+"运行完成");
                    System.out.println("清除线程缓存:"+threadCnt);
                    threadMap.remove(new Integer(threadCnt).toString());
                    System.out.println("清除完成.....");
                    if(threadMap.size()==0){
                        condition.notifyAll();
                        System.out.println("唤醒主线程完成.....");
                    }

                }
            }

            for(int i=0;i<maxThread;i++){
                InsertThread thread = new InsertThread(i);
                Thread threadObj = new Thread(thread);
                threadObj.setName("thread "+i);
                threadObj.start();
                threadObj.join();

                threadMap.put(new Integer(i).toString(),new AtomicInteger(0));
            }
        }catch (IOException e){
            e.printStackTrace();
            throw e;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }




    /**
     * 创建表
     * @param tableName
     */
    public static void createTable(String tableName) {
        System.out.println("start create table ......");
        try {
            Configuration configuration = HBaseConfiguration.create();
            HBaseAdmin hBaseAdmin = new HBaseAdmin(configuration);
            if (hBaseAdmin.tableExists(tableName)) {// 如果存在要创建的表，那么先删除，再创建
                hBaseAdmin.disableTable(tableName);
                hBaseAdmin.deleteTable(tableName);
                System.out.println(tableName + " is exist,detele....");
            }
            HTableDescriptor tableDescriptor = new HTableDescriptor(tableName);
            tableDescriptor.addFamily(new HColumnDescriptor("corse"));
            hBaseAdmin.createTable(tableDescriptor);
        } catch (MasterNotRunningException e) {
            e.printStackTrace();
        } catch (ZooKeeperConnectionException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("end create table ......");
    }

}
