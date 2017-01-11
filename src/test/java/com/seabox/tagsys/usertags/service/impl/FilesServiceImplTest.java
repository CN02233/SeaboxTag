package com.seabox.tagsys.usertags.service.impl;

import com.seabox.tagsys.base.db.RelationDbTemplate;
import com.seabox.tagsys.usertags.action.WriteToRelationDb;
import com.seabox.tagsys.usertags.service.FilesService;
import com.seabox.test.base.BaseTestClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.junit.Assert.*;

/**
 * Created by pc on 2016/12/8.
 */
public class FilesServiceImplTest extends BaseTestClass{

    @Autowired
    private FilesService filesService;

    @Autowired
    private RelationDbTemplate relatioDbTemplate;

    @Test
    public void exportCampUsers() throws Exception {
        String campId = "135";

        WriteToRelationDb writeToRelationDb = new WriteToRelationDb(true,"h62_campaign_person_"+campId,relatioDbTemplate);

        filesService.exportCampUsers(campId,null,writeToRelationDb);
    }

}