package com.seabox.tagsys.usertags.hbase.entity;

import com.seabox.tagsys.usertags.hbase.HBaseUtil;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuchh on 3/10/16.
 */
public class TSmsReplyTrack {


    private static final Logger logger = LoggerFactory.getLogger(TSmsReplyTrack.class);

    private String current_campId;


    public final static TableName tableName() {
        return TableName.valueOf("h62_campaign_sms_reply");
    }

    public final static byte[] ColFamily() {
        return "cf".getBytes();
    }


    public final static byte[] FIXED_ROWKEY() {
        return "FIXED_ROWKEY".getBytes();
    }


    public final static byte[] col_currentExtendNum() {
        return "current_extend_num".getBytes();
    } // extendaccessnum


    public final static byte[] col_currentCampId() {
        return "current_campId".getBytes();
    }

    public String getCurrent_campId() {
        return current_campId;
    }


    public final static class _Camps {

        public final static byte[] ColFamily() {
            return "camps".getBytes();
        }

    }


    public static Table createTableIfMissing(Connection connection) throws IOException {

        List<byte[]> allColumnFamilies = new ArrayList<>();
        allColumnFamilies.add(ColFamily());
        allColumnFamilies.add(_Camps.ColFamily());

        return HBaseUtil.createOrUpgradeTable(connection, tableName(), allColumnFamilies);

    }


    public static TSmsReplyTrack findById(Table smsReplyTrackTable, String extendNum) {

        Get get = new Get(extendNum.getBytes());
        get.addFamily(ColFamily());

        try {
            Result result = smsReplyTrackTable.get(get);
            if ( HBaseUtil.isAnValidResult(result ) ) {
                TSmsReplyTrack findObj = new TSmsReplyTrack();

                String campId = HBaseUtil.getValueAsString(result, ColFamily(), col_currentCampId());
                findObj.current_campId = campId;
                return findObj;
            }
        } catch (IOException e) {
            logger.error("failed to find record ", e);
        }

        return null;

    }


    public static synchronized String registerExtendNumForCamp(Table smsReplyTrackTable, String campId) throws IOException {

        long current_extend_num = 1;

        Get get = new Get(FIXED_ROWKEY());
        get.addFamily(ColFamily());

        Result result = smsReplyTrackTable.get(get);
        boolean zero_init = true;
        if (HBaseUtil.isAnValidResult(result )) {

            //skip 0, let's use  1-99
            current_extend_num = HBaseUtil.increaseCounter(smsReplyTrackTable, FIXED_ROWKEY(), ColFamily(), col_currentExtendNum(), 1L);

            if (current_extend_num <= 99) {
                zero_init = false;
            } else {
                logger.info("extendNum  now roll back to zero!");
            }
        }

        if (zero_init) {
            Put put = new Put(FIXED_ROWKEY());
            put.addColumn(ColFamily(), col_currentExtendNum(), HBaseUtil.ONE_64BIT_INIT_VALUE);
            smsReplyTrackTable.put(put);

            current_extend_num = 1L;
        }

        String  extend_numStr = String.valueOf(current_extend_num);
        if(current_extend_num<10) {
            extend_numStr = '0' + extend_numStr;
        }

        Put put = new Put(extend_numStr.getBytes());
        put.addColumn(ColFamily(), col_currentCampId(), campId.getBytes());
        put.addColumn(_Camps.ColFamily(), campId.getBytes(), HBaseUtil.ZERO_64BIT_INIT_VALUE); //use campId as qualifier.
        smsReplyTrackTable.put(put);

        return extend_numStr;


    }


}