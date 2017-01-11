package com.seabox.tagsys.usertags.hbase.entity;

import com.seabox.tagsys.usertags.hbase.HBaseUtil;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

/**
 * @author Changhua, Wu
 *         Created on: 4/29/16,4:58 PM
 */
public class TUsersImport {


    private static final Logger logger = LoggerFactory.getLogger(TUsersImport.class);

    private String mobile;
    private String   index;
    private Set<String> importedTags;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public Set<String> getImportedTags() {
        return importedTags;
    }

    public void setImportedTags(Set<String> importedTags) {
        importedTags = importedTags;
    }




    public final static TableName tableName() {
        return TableName.valueOf("h62_users_import");
    }


    public final static byte[] ColFamily() {
        return "cf".getBytes();
    }

    public final static byte[] col_mobile() { return "mobile".getBytes(); }
    public final static byte[] col_index() { return  "index".getBytes(); }



    public final static class _ImportTags {

        public final static byte[] ColFamily() {
            return "importTags".getBytes();
        }

        // columns are each importTags (file Ids).

    }


    public static Table createTableIfMissing(Connection connection) throws IOException {

        List<byte[]> allColumnFamilies = new ArrayList<>();
        allColumnFamilies.add(_ImportTags.ColFamily());

        return HBaseUtil.createOrUpgradeTable(connection, tableName(), allColumnFamilies);

    }


    public static TUsersImport findById(Table table, String mobile) {

        TUsersImport findObj = null;

        Get get = new Get( mobile.getBytes() );
        get.addFamily(ColFamily());
        get.addFamily(_ImportTags.ColFamily());

        try {
            Result result = table.get(get);
            if( HBaseUtil.isAnValidResult(result ) ) {
                findObj = new TUsersImport();
                findObj.setMobile( mobile );
                String index = HBaseUtil.getValueAsString(result, ColFamily(), col_index());
                findObj.setIndex(index);

            }
        } catch (IOException e) {
            logger.error("failed to find record ", e);
        }

        return findObj;

    }




    /*

    public final static void createOneNewRecord(Table table, String mobile, String importFilename, String alias, byte[] fileContent) throws IOException{

        logger.info("createOneNewRecord() start  for fileId:{} importFilename: {}, id:{}, fileContent:{}", fileId, importFilename, alias, fileContent.length);

        Put put = new Put( fileId.getBytes() );

        put.addColumn(ColFamily(),  col_id(), fileId.getBytes());
        put.addColumn(ColFamily(),  col_alias(),         alias.getBytes());
        put.addColumn(ColFamily(),  col_filename(),      importFilename.getBytes());
        put.addColumn(ColFamily(),  col_importTime(), new Date().toString().getBytes() );
        put.addColumn(_File.ColFamily(),  _File.col_content(),        fileContent);

        table.put( put );

        table.close();

    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("TUsersImport id:").append(alias)
                .append(", importFilename:").append(importFilename);
        return  sb.toString();
    }
    */


}
