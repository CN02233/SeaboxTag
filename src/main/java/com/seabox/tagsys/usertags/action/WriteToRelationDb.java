package com.seabox.tagsys.usertags.action;

import com.seabox.tagsys.base.db.RelationDbTemplate;
import com.seabox.tagsys.usertags.entity.PersonData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Song Chaoqun on 2016/12/8.
 */
public class WriteToRelationDb {

    private static final Logger logger = LoggerFactory.getLogger(WriteToRelationDb.class);

    private boolean isWrite;

    private String tableName;

    private RelationDbTemplate relationDbTemplate;

    public WriteToRelationDb(boolean isWrite,String tableName,RelationDbTemplate relationDbTemplate){
        this.isWrite = isWrite;
        this.tableName = tableName;
        this.relationDbTemplate = relationDbTemplate;
    }

    public void writeToDb(PersonData personData){
        logger.info("start write person data to relation database --->"+personData.toString());

        relationDbTemplate.insert("",personData);

    }


    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public boolean isWrite() {
        return isWrite;
    }

    public void setWrite(boolean write) {
        isWrite = write;
    }

    public RelationDbTemplate getRelationDbTemplate() {
        return relationDbTemplate;
    }

    public void setRelationDbTemplate(RelationDbTemplate relationDbTemplate) {
        this.relationDbTemplate = relationDbTemplate;
    }
}
