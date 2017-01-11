package com.seabox.tagsys.usertags.service.impl;

import com.seabox.tagsys.usertags.entity.PersonData;
import com.seabox.tagsys.usertags.hbase.entity.TUserInfo;
import com.seabox.tagsys.usertags.action.WriteToRelationDb;
import com.seabox.tagsys.usertags.service.TaskBreakExecutor;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Changhua, Wu
 *         Created on: 5/13/16,10:46 AM
 */
public class ExportCampUsersExecutor extends TaskBreakExecutor {


    private static final Logger logger = LoggerFactory.getLogger(ExportCampUsersExecutor.class);

    private String campId;
    private BitSet localBitSet;

    private Table userInfoTable;
    private OutputStream outputStream;
    private AtomicLong length = new AtomicLong(0);
    private Object writeOutputSync = new Object();

    private WriteToRelationDb writeToRelationDb;//add by SongChaoqun 20161208

    public long getExportFileLength() {
        return length.get();
    }



    private static final int  MAX_THREADS_FOR_SUB = 10;



    public ExportCampUsersExecutor(String campId, BitSet localBitSet, Table userInfoTable, OutputStream outputStream) {

        super("export Camp users :campId=" + campId, MAX_THREADS_FOR_SUB, localBitSet.length(), true);

        this.campId = campId;
        this.localBitSet = localBitSet;
        this.outputStream = outputStream;
        try {
            outputStream.write( "#guid,username,mobileNo\n".getBytes() );
        } catch (IOException e) {
            logger.error("failed to write csv header lines,", e);
        }
        this.userInfoTable = userInfoTable;
    }

    /**
     * add by SongChaoqun 2016-12-08
     * not write to file but write to mysql or the other relation database
     * @param campId
     * @param localBitSet
     * @param userInfoTable
     * @param outputStream
     * @param writeToRelationDb
     */
    public ExportCampUsersExecutor(String campId, BitSet localBitSet, Table userInfoTable, OutputStream outputStream, WriteToRelationDb writeToRelationDb){
        super("export Camp users :campId=" + campId, MAX_THREADS_FOR_SUB, localBitSet.length(), true);
        if(writeToRelationDb!=null)
            this.writeToRelationDb = writeToRelationDb;

        new ExportCampUsersExecutor(campId,localBitSet,userInfoTable,outputStream);
    }



    final int BATCH_GET_RECORDS_HBASE = 9000;

    @Override
    public void execOne(int taskId, long index) {
        // dummy, we use  execRange
    }



    public void  exportUsersInfoToStream( int taskId, List<Integer> guidList) {
        try {

            List<Get>  gets = new ArrayList<>( guidList.size() );
            for(Integer key : guidList) {
                Get get = new Get( key.toString().getBytes() );//用户行号
                get.addFamily(TUserInfo._CustInfo.ColFamily());//"cust_info"
                gets.add(get);
            }

            Result[] results = userInfoTable.get( gets );

            StringBuffer sb = new StringBuffer();

            PersonData personData = new PersonData();

            int nLoop = 0;
            for(Integer guid : guidList) {
                if(nLoop< results.length) {
                    TUserInfo userInfo = TUserInfo.buildFromResult( results[nLoop] );

                    personData.setUserNm(userInfo.getUserName());
//                    personData.setUserNm(userInfo.getMobileNo());
//                    personData.setUserNm(userInfo.getSpringPoint());

                    if(null == userInfo || userInfo.getMobileNo() == null
                            || userInfo.getMobileNo().isEmpty() ) {
                        countError( taskId);
                    } else {
                        sb.append(guid);
                        sb.append(",");
                        if(null != userInfo.getUserName()) {
                            sb.append( userInfo.getUserName() );
                        }
                        sb.append(",");
                        sb.append( userInfo.getMobileNo() );
                        sb.append("\n");
                        countComplete( taskId );
                    }

                } else {
                    countError( taskId );
                    logger.error("hbase result not matching with request:  guidList.size()={}, results.length={}", guidList.size(), results.length);
                    break;
                }

                ++nLoop;
            }

            // writing output must sync
            if(outputStream!=null){
                synchronized (writeOutputSync) {
                    String writeBuf = sb.toString();
                    // logger.debug(" taskId:{} write buffer to export: {}", taskId, writeBuf);
                    outputStream.write( writeBuf.getBytes() );
                    length.getAndAdd( writeBuf.length() );
                }
            }

            if(writeToRelationDb!=null&&writeToRelationDb.isWrite()){
                writeToRelationDb.writeToDb(personData);
            }

        } catch (IOException e) {
            logger.error("error on retrieve userInfo: ", e);
        }
    }




    @Override
    public void execRange(int taskId, long start, long end) {

        int nBatch = 0;

        List<Integer> guidList = new ArrayList<>();
        for(long l=start; l<end;){
            countStart( taskId );
            int setIndex = localBitSet.nextSetBit( (int)l );
            if(setIndex>=end) {
                break;
            }

            ++nBatch;

            guidList.add(setIndex);

            if(nBatch>= BATCH_GET_RECORDS_HBASE) {
                nBatch = 0; //reset

                // load from hbase
                exportUsersInfoToStream(taskId, guidList);
                guidList = new ArrayList<>();
            }

            l = setIndex+1;
        }

        if(nBatch> 0) { // there's remaining items tobe flush
            nBatch = 0; //reset

            // load from hbase
            exportUsersInfoToStream( taskId, guidList);
        }

    }

    @Override
    protected void init() {

    }

    @Override
    public void onComplete(int taskId) {

    }

}

