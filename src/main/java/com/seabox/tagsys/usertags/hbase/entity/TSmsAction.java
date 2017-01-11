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
 * @author Changhua, Wu
 *         Created on: 2/16/16,3:32 PM
 */
public final class TSmsAction {

    private static final Logger logger = LoggerFactory.getLogger(TSmsAction.class);

    public final static TableName tableName() {
        return TableName.valueOf("h62_campaign_sms");
    }

    public final static byte[] ColFamily() {
        return "cf".getBytes();
    }

    /*
         *  Table2:  h62_campaign_sms
     *  rowKey = seqNo,
     *
     *  phoneNumber,  GUID1,  GUID2, GUIDxx ...,    sent, recv, ack
     *
     *  enCoding SeqNo as Base64(campID) + ":" + Base64(PhoneNumber)
     *
     */


    /**
     *  HBase
     *
     *  Table1:  h62_campaign_indv
     *  campID: tagCondition, numOfUsers, status(PREPARE -> READY -> ACTION -> DONE), GUID1, GUID2, ...
     *
     *  Table2:  h62_campaign_sms
     *  seqNo, phoneNumber,  GUID1,  GUID2, GUIDxx ...,    sent, recv, ack
     *
     *  enCoding SeqNo as Base64(campID) + ":" + Base64(PhoneNumber)
     *
     *
     *
     *
     */


    public final static class _User {

        public final static byte[] ColFamily() {
            return "u".getBytes();
        }

        public final static byte[] col_campId() { return  "campId".getBytes(); }

        public final static byte[] col_guid() { return  "guid".getBytes(); }
        public final static byte[] col_name() { return  "name".getBytes(); }
        public final static byte[] col_mobile() { return  "mobile".getBytes(); }
        public final static byte[] col_springPoint() { return  "springPoint".getBytes(); }

    }


    public final static class _Sms {

        public final static byte[] ColFamily() {
            return "sms".getBytes();
        }

        public final static byte[] col_send() { return  "send".getBytes(); }
        public final static byte[] col_sendFail() { return  "sendFail".getBytes(); }
        public final static byte[] col_recv() { return  "recv".getBytes(); }
        public final static byte[] col_ack() { return  "ack".getBytes(); }
        public final static byte[] col_recvCount() { return  "recvCount".getBytes(); }

    }




    public final static  void createOneNewRecord(BufferedMutator smsActionBufferedMutator, String sequenceNum, String campId, String userGUID, TUserInfo userInfoRecord) throws IOException{

        Put putSms = new Put( sequenceNum.getBytes() );

        putSms.addColumn(_User.ColFamily(), _User.col_campId(), campId.getBytes());
        putSms.addColumn(_User.ColFamily(), _User.col_guid(), userGUID.getBytes());
        putSms.addColumn(_User.ColFamily(), _User.col_name(), HBaseUtil.emptyStringBytesIfNull(userInfoRecord.getUserName()));
        putSms.addColumn(_User.ColFamily(), _User.col_mobile(), HBaseUtil.emptyStringBytesIfNull(userInfoRecord.getMobileNo()));
        putSms.addColumn(_User.ColFamily(), _User.col_springPoint(), HBaseUtil.emptyStringBytesIfNull(userInfoRecord.getSpringPoint()));

        putSms.addColumn(_Sms.ColFamily(), _Sms.col_recvCount(), HBaseUtil.ZERO_64BIT_INIT_VALUE);

        smsActionBufferedMutator.mutate(putSms);
    }



    public static Table createTableIfMissing(Connection connection) throws IOException {

        List<byte[]> allColumnFamilies = new ArrayList<>();
        allColumnFamilies.add( ColFamily() );
        allColumnFamilies.add( _User.ColFamily() );
        allColumnFamilies.add( _Sms.ColFamily() );

        return HBaseUtil.createOrUpgradeTable(connection, tableName(), allColumnFamilies);

    }

}
