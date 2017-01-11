package com.seabox.tagsys.usertags.hbase.entity;

import com.seabox.tagsys.usertags.action.call.CallSequenceGenerator;
import com.seabox.tagsys.usertags.hbase.HBaseUtil;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Changhua, Wu
 *         Created on: 5/19/16,11:22 AM
 */
public class TCallAction {


    private String campId;
    private String guid;
    private String mobile;
    private String callResult;



    private static final Logger logger = LoggerFactory.getLogger(TCallAction.class);

    public final static TableName tableName() {
        return TableName.valueOf("h62_camp_call_action");
    }

    public final static byte[] ColFamily() {
        return "cf".getBytes();
    }

    public final static byte[] col_campId() { return  "campId".getBytes(); }
    public final static byte[] col_guid() { return  "guid".getBytes(); }
    public final static byte[] col_mobile() { return  "mobile".getBytes(); }



    public final static class _Call {

        public final static byte[] ColFamily() {
            return "call".getBytes();
        }

        public final static byte[] col_result() { return  "result".getBytes(); }

    }



    public static TCallAction findById(Table callActTable, String campId, String mobileNo) {

        String rowKey = CallSequenceGenerator.getRowKey(campId, mobileNo);

        Get get = new Get( rowKey.getBytes() );
        get.addFamily( ColFamily() );
        get.addFamily( _Call.ColFamily() );

        try {
            Result result = callActTable.get(get);

            TCallAction findObj = buildFromResult( result );

            return findObj;

        } catch (IOException e) {
            logger.error("failed to find record ", e);
        }

        return null;

    }


    public  static TCallAction buildFromResult( Result result ) {

        TCallAction findObj = new TCallAction();

        String strVal = HBaseUtil.getValueAsString(result, ColFamily(), col_campId());
        findObj.setCampId(strVal);

        strVal = HBaseUtil.getValueAsString(result, ColFamily(), col_guid());
        findObj.setGuid(strVal);

        strVal = HBaseUtil.getValueAsString(result, ColFamily(), col_mobile());
        findObj.setMobile(strVal);

        strVal = HBaseUtil.getValueAsString(result, ColFamily(), _Call.col_result());
        findObj.setCallResult(strVal);

        return findObj;
    }


    public final static  void createOneNewRecord(BufferedMutator bufferedMutator, int campId, long userGUID, String mobileNo,  @Nullable String callResult) throws IOException {

        String rowKey = CallSequenceGenerator.getRowKey(campId, mobileNo);
        Put putSms = new Put( rowKey.getBytes() );

        putSms.addColumn(ColFamily(), col_campId(), String.valueOf(campId).getBytes());
        putSms.addColumn(ColFamily(), col_guid(), String.valueOf(userGUID).getBytes());
        putSms.addColumn(ColFamily(), col_mobile(), mobileNo.getBytes() );
        if(null != callResult ) {
            putSms.addColumn(_Call.ColFamily(), _Call.col_result(), callResult.getBytes() );
        }

        bufferedMutator.mutate(putSms);
    }



    public static Table createTableIfMissing(Connection connection) throws IOException {

        List<byte[]> allColumnFamilies = new ArrayList<>();
        allColumnFamilies.add( ColFamily() );
        allColumnFamilies.add( _Call.ColFamily() );

        return HBaseUtil.createOrUpgradeTable(connection, tableName(), allColumnFamilies);

    }


    public String getCampId() {
        return campId;
    }

    public void setCampId(String campId) {
        this.campId = campId;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCallResult() {
        return callResult;
    }

    public void setCallResult(String callResult) {
        this.callResult = callResult;
    }
}
