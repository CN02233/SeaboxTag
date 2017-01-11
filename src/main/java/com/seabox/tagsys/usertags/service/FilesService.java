package com.seabox.tagsys.usertags.service;

import com.seabox.tagsys.usertags.action.WriteToRelationDb;
import com.seabox.tagsys.usertags.entity.ImportFile;
import com.seabox.tagsys.usertags.service.impl.FileImportException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * @author Changhua, Wu
 *         Created on: 5/3/16,10:25 AM
 */
public interface FilesService {

    boolean importUsers(MultipartFile importUsersFile, int userId, boolean share) throws FileImportException;

    List<ImportFile> listAllByUser(int user_id);

    int exportCampUsers(String campId, OutputStream outputStream) throws IOException;
    int exportCampUsers(String campId, OutputStream outputStream,  WriteToRelationDb writeToRelationDb) throws IOException;

    boolean exportImportedUsers();

}
