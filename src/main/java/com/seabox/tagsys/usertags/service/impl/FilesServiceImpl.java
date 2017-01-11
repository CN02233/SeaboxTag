package com.seabox.tagsys.usertags.service.impl;

import com.seabox.tagsys.usertags.action.CampaignActionStatus;
import com.seabox.tagsys.usertags.action.WriteToRelationDb;
import com.seabox.tagsys.usertags.entity.ImportFile;
import com.seabox.tagsys.usertags.hbase.HBaseConnectionMgr;
import com.seabox.tagsys.usertags.hbase.entity.TCampInfo;
import com.seabox.tagsys.usertags.msg.MissingCampException;
import com.seabox.tagsys.usertags.mybatis.dao.ImportFileDao;
import com.seabox.tagsys.usertags.mybatis.dao.ImportUserDao;
import com.seabox.tagsys.usertags.mybatis.dao.ImportUserTempDao;
import com.seabox.tagsys.usertags.service.BigTaskBreakUtil;
import com.seabox.tagsys.usertags.service.FilesService;
import org.apache.commons.lang3.EnumUtils;
import org.apache.hadoop.hbase.client.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.BitSet;
import java.util.List;

/**
 * @author Changhua, Wu
 *         Created on: 5/3/16,10:23 AM
 */
@Service
public class FilesServiceImpl implements FilesService {

    private static final Logger logger = LoggerFactory.getLogger(FilesServiceImpl.class);


    @Autowired
        private HBaseConnectionMgr connectionMgr;

    @Autowired
    private ImportFileDao  importFileDao;


    @Autowired
    private ImportUserDao importUserDao;

    @Autowired
    private ImportUserTempDao importUserTempDao;


    public ImportFileDao getImportFileDao() {
        return importFileDao;
    }

    public void setImportFileDao(ImportFileDao importFileDao) {
        this.importFileDao = importFileDao;
    }

    public ImportUserDao getImportUserDao() {
        return importUserDao;
    }

    public void setImportUserDao(ImportUserDao importUserDao) {
        this.importUserDao = importUserDao;
    }

    public ImportUserTempDao getImportUserTempDao() {
        return importUserTempDao;
    }

    public void setImportUserTempDao(ImportUserTempDao importUserTempDao) {
        this.importUserTempDao = importUserTempDao;
    }


    /**
     * as we use temp table to accelerate import process, import only support single one by one without in-parallel
     *
     * @param importUsersFile
     * @param share
     * @return
     * @throws FileImportException
     */
    @Override
    public synchronized boolean importUsers(MultipartFile importUsersFile, int userId, boolean share) throws FileImportException{

        String fileName = importUsersFile.getOriginalFilename();

        if(fileName==null || fileName.isEmpty()) { //compatible for Unit Test
            logger.error("empty fileName");
            return false;
        }
        String alias = fileName;

        try {

            byte[] content = importUsersFile.getBytes();

            String fid = loadFile(userId, fileName, share, alias, content);

            loadTempUsers(content);

            loadUsersForFile(fid);


        } catch (Exception e) {
            logger.error("failed to get content from imported file: ", e);
            return false;
        }

        logger.info("importUsers() alias={}, fileName={}", alias, fileName);

        return true;

    }

    @Override
    public List<ImportFile> listAllByUser(int user_id) {
        List<ImportFile> result = importFileDao.listAllByUser(user_id);
        return result;
    }


    @Override
    public int exportCampUsers(String campId, final OutputStream outputStream) throws IOException {
        return this.exportCampUsers(campId,outputStream,null);
    }

    @Override
    public int exportCampUsers(String campId, final OutputStream outputStream, WriteToRelationDb writeToRelationDb) throws IOException {

        final Table campInfoTable = connectionMgr.getTableTCampInfo();
        final Table userInfoTable = connectionMgr.getTableTUserInfo();
        TCampInfo campInfoObj = TCampInfo.findById(campInfoTable, campId, true);

        if(null == campInfoObj) {
            throw new MissingCampException("can not find camp:" + campId + " from hbase");
        }

        byte[] usersBitSet = campInfoObj.getUsersBitSet();

        int fileSize = 0;

        if(null!= usersBitSet) {
            final BitSet localBitSet = CacheServiceRedisImpl.fromByteArrayReverse( usersBitSet );

            int numsLocalBitSet = localBitSet.cardinality();

            logger.info("exportCampUsers() progress(hbase -> local BitSet) on-going  for campId: {},  numsLocalBitSet={} ", campId, numsLocalBitSet);

            //根据最终and和or的标签匹配到的用户从hbase中捞取这些用户的数据
            ExportCampUsersExecutor breakExecutor = new ExportCampUsersExecutor( campId, localBitSet, userInfoTable, outputStream,writeToRelationDb)  ;

            BigTaskBreakUtil.breakExec(breakExecutor);


            fileSize = (int)breakExecutor.getExportFileLength();

        } else {

            CampaignActionStatus actionStatus = EnumUtils.getEnum(CampaignActionStatus.class, campInfoObj.getStatus());

            if(null != actionStatus) {
                switch (actionStatus) {
                    case PREPARE:
                    case PREPARE_FAILED:
                        logger.error("camp:{} is not ready for export yet!", campId);
                        break;

                    default:

                        final Table smsActionTable = connectionMgr.getTableTSmsAction();

                        ExportCampUsersExecutorOldGUID breakExecutor = ExportCampUsersExecutorOldGUID.create(campId, campInfoTable, smsActionTable, outputStream)  ;

                        if(null != breakExecutor) {
                            BigTaskBreakUtil.breakExec( breakExecutor );
                        }


                        fileSize = (int)breakExecutor.getExportFileLength();

                        break;
                }
            } else {
                logger.error("userBit for camp:{} does'nt exist in hbase!", campId);
            }



        }

        return fileSize;
    }

    @Override
    public boolean exportImportedUsers() {

        return true;
    }


    private final static int MAX_INSERT_BATCH_LIST = 3000;


    /**
     *
     * @param fileName
     * @param alias
     * @param content
     * @return   fid  new added in Import-File table
     * @throws FileImportException
     */

    private String loadFile(int userId, String fileName, boolean share, String alias, byte[] content) throws FileImportException{

        String fid = importFileDao.getIdByAlias(alias);
        if(null != fid) {
            throw  new FileImportException.FileAlreadyExistsException();
        } else {
            importFileDao.saveImportFile(userId, share ? 1 : 0, fileName, alias,  content);
            fid = importFileDao.getIdByAlias( alias );

            return fid;
        }

    }



    private void loadTempUsers(byte[] content) throws IOException {

        StringBuffer sb = new StringBuffer();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(content);
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
        String line;


        //always truncate this temp table
        importUserTempDao.cleanup();

        boolean isFirst = true;
        long lineCount=0;
        while ((line = in.readLine()) != null) {
            if(line.isEmpty()
                    || !line.matches("\\d+")) {
                // ignore this line
            } else {
                if(isFirst) {
                    sb.append("('");
                    isFirst = false;
                } else {
                    sb.append(",('");
                }
                sb.append( line );
                sb.append("')");

                if(++lineCount % MAX_INSERT_BATCH_LIST ==0) {
                    String valueLists = sb.toString();
                    importUserTempDao.addTempUserMobiles(valueLists);
                    sb = new StringBuffer();
                    isFirst= true;
                }
            }
        }

        String valueLists = sb.toString();
        if(valueLists.length()>0) {
            importUserTempDao.addTempUserMobiles(valueLists);
        }

        Long total = importUserTempDao.getTotalCount();

        logger.debug("lineCount={} total = {}", lineCount, total);

    }


    private void loadUsersForFile(String fid) {

        importUserDao.createColumnForImportedFile( fid );

        importUserDao.addAllUsersFromTempImport();
        importUserDao.updateUsersToImportFile( fid );

        importFileDao.updateImportUserCount( fid );

        importUserDao.createIndexForImportedFile( fid );

    }

}
