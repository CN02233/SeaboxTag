package com.seabox.tagsys.usertags.hbase;

import com.seabox.tagsys.usertags.hbase.entity.*;
import com.seabox.tagsys.usertags.utils.SingletonBean;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.io.IOException;

/**
 * @author Changhua, Wu
 *         Created on: 2/18/16,12:48 PM
 */
@SingletonBean
public class HBaseConnectionMgr {

    private static final Logger logger = LoggerFactory.getLogger(HBaseConnectionMgr.class);

    private Configuration conf;

    public HBaseConnectionMgr() {

    }


    private Table  tableTCampInfo;
    private Table  tableTUserInfo;
    private Table  tableTSmsAction;
    private Table  tableTSmsReplyTrack;
    private Table  tableTCallAction;


    private BufferedMutator bufMutTCampInfo;
    private BufferedMutator bufMutTUserInfo;
    private BufferedMutator bufMutTSmsAction;
    private BufferedMutator bufMutTSmsReplyTrack;
    private BufferedMutator bufMutTCallAction;


    @PostConstruct
    public void init() throws IOException{
        System.setProperty("hadoop.home.dir", "D:\\installer\\work\\hadoop\\");

        conf = HBaseConfiguration.create();

        initWithConf(conf);

    }


    public void initWithConf(Configuration conf) throws IOException {

        Connection connection = ConnectionFactory.createConnection(conf);

        tableTCampInfo = TCampInfo.createTableIfMissing(connection);
        tableTUserInfo = TUserInfo.createTableIfMissing(connection);
//        tableTSmsAction = TSmsAction.createTableIfMissing(connection);
//        tableTSmsReplyTrack = TSmsReplyTrack.createTableIfMissing(connection);
        tableTCallAction = TCallAction.createTableIfMissing( connection );

        //...  hbase.client.write.buffer  need specified in hbase-site.xml
        bufMutTCampInfo = connection.getBufferedMutator( TCampInfo.tableName() );
        bufMutTUserInfo = connection.getBufferedMutator( TUserInfo.tableName() );
//        bufMutTSmsAction = connection.getBufferedMutator( TSmsAction.tableName() );
//        bufMutTSmsReplyTrack = connection.getBufferedMutator( TSmsReplyTrack.tableName() );
        bufMutTCallAction = connection.getBufferedMutator( TCallAction.tableName() );

    }


    public Connection   getConnection() throws IOException{
        Connection connection = ConnectionFactory.createConnection(conf);
        return connection;
    }


    public Table getTableTCampInfo() {
        return tableTCampInfo;
    }

    public Table getTableTUserInfo() {
        return tableTUserInfo;
    }

    public Table getTableTSmsAction() {
        return tableTSmsAction;
    }

    public Table getTableTSmsReplyTrack() {
        return tableTSmsReplyTrack;
    }


    public BufferedMutator getBufMutTCampInfo() {
        return bufMutTCampInfo;
    }

    public BufferedMutator getBufMutTUserInfo() {
        return bufMutTUserInfo;
    }

    public BufferedMutator getBufMutTSmsAction() {
        return bufMutTSmsAction;
    }

    public BufferedMutator getBufMutTSmsReplyTrack() {
        return bufMutTSmsReplyTrack;
    }

    public Table getTableTCallAction() {
        return tableTCallAction;
    }

    public BufferedMutator getBufMutTCallAction() {
        return bufMutTCallAction;
    }
}
