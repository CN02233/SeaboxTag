package com.seabox.tagsys.usertags.service.impl;

import com.seabox.tagsys.usertags.hbase.HBaseUtil;
import com.seabox.tagsys.usertags.hbase.entity.TCampInfo;
import com.seabox.tagsys.usertags.hbase.entity.TSmsAction;
import com.seabox.tagsys.usertags.service.TaskBreakExecutor;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellScanner;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Changhua, Wu
 *         Created on: 5/13/16,11:18 AM
 */
public class ExportCampUsersExecutorOldGUID extends TaskBreakExecutor {



    private static final Logger logger = LoggerFactory.getLogger(ExportCampUsersExecutorOldGUID.class);

    private String campId;
    private String[] smsKeyArrays;

    private Table smsActionTable;
    private Table campInfoTable;
    private OutputStream outputStream;
    private AtomicLong length = new AtomicLong(0);
    private Object writeOutputSync = new Object();

    public long getExportFileLength() {
        return length.get();
    }



    private static final int  MAX_THREADS_FOR_SUB = 10;



    public static ExportCampUsersExecutorOldGUID  create(String campId,  Table campInfoTable, Table smsActionTable, OutputStream outputStream) {

        Get get = new Get( campId.getBytes() );
        get.addFamily(TCampInfo._Users.ColFamily());
        get.addFamily(TCampInfo._SentUsers.ColFamily());

        try {
            Result result = campInfoTable.get( get );
            CellScanner cellScanner =  result.cellScanner();

            int count = 0;
            List<String>  smsKeys = new ArrayList<>();
            while ( cellScanner.advance() ) {

                Cell cell = cellScanner.current();

                String userGUID = new String(cell.getQualifierArray(), cell.getQualifierOffset(), cell.getQualifierLength());
                String valueOfRowKeySms = new String(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength());

                smsKeys.add( valueOfRowKeySms );

                ++count;
            }

            String[] smsKeyArrays = smsKeys.toArray(new String[0]);

            ExportCampUsersExecutorOldGUID executorOldGUID = new ExportCampUsersExecutorOldGUID(campId,  campInfoTable, smsActionTable, outputStream, smsKeyArrays);
            return executorOldGUID;

        } catch (IOException e) {
            logger.error("failed to scan users from TCampInfo, exception:", e);
        }

        return null;

    }


    private ExportCampUsersExecutorOldGUID(String campId, Table campInfoTable, Table smsActionTable, OutputStream outputStream, String[]  smsKeyArrays ) {

        super("export Camp users :campId=" + campId, MAX_THREADS_FOR_SUB, smsKeyArrays.length, true);

        this.campId = campId;
        this.outputStream = outputStream;
        this.smsKeyArrays = smsKeyArrays;
        this.smsActionTable = smsActionTable;
        this.campInfoTable = campInfoTable;
    }



    final int BATCH_GET_RECORDS_HBASE = 9000;

    @Override
    public void execOne(int taskId, long index) {
        // dummy, we use  execRange
    }



    public void  exportUsersInfoToStream( int taskId, List<Get> getsBySmsKey) {
        try {

            Result[] results = smsActionTable.get(getsBySmsKey);

            StringBuffer sb = new StringBuffer();
            for(Result result: results) {
                String mobile = HBaseUtil.getValueAsString( result, TSmsAction._User.ColFamily(), TSmsAction._User.col_mobile() );
                if(mobile !=null && mobile.length()> 0) {
                    sb.append(mobile);
                    sb.append("\n");
                    countComplete( taskId );
                }
            }

            // writing output must sync
            synchronized (writeOutputSync) {
                String writeBuf = sb.toString();
                // logger.debug(" taskId:{} write buffer to export: {}", taskId, writeBuf);
                outputStream.write( writeBuf.getBytes() );
                length.getAndAdd( writeBuf.length() );
            }

        } catch (IOException e) {
            logger.error("error on retrieve userInfo: ", e);
        }
    }




    @Override
    public void execRange(int taskId, long start, long end) {

        int nBatch = 0;

        List<Get>  getsBySmsKey = new ArrayList<>( );

        for(int l=(int)start; l<end; ++l){
            countStart( taskId );
            ++nBatch;

            Get get = new Get( smsKeyArrays[l].toString().getBytes() );
            get.addColumn(TSmsAction._User.ColFamily(), TSmsAction._User.col_mobile() );
            getsBySmsKey.add(get);

            if(nBatch>= BATCH_GET_RECORDS_HBASE) {
                nBatch = 0; //reset

                // load from hbase
                exportUsersInfoToStream(taskId, getsBySmsKey);
                getsBySmsKey = new ArrayList<>();
            }
        }

        if(nBatch> 0) { // there's remaining items tobe flush
            nBatch = 0; //reset

            // load from hbase
            exportUsersInfoToStream( taskId, getsBySmsKey);
        }

    }

    @Override
    protected void init() {

    }

    @Override
    public void onComplete(int taskId) {

    }

}
