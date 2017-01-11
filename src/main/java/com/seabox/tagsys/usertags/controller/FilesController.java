package com.seabox.tagsys.usertags.controller;

import com.seabox.tagsys.usertags.entity.ImportFile;
import com.seabox.tagsys.usertags.msg.MissingCampException;
import com.seabox.tagsys.usertags.msg.MsgExportCampUsers;
import com.seabox.tagsys.usertags.service.FilesService;
import com.seabox.tagsys.usertags.service.TagService;
import com.seabox.tagsys.usertags.service.impl.FileImportException;
import com.seabox.tagsys.usertags.utils.UserTagUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author Changhua, Wu
 *         Created on: 4/29/16,10:13 AM
 */
@Controller
@RequestMapping("files")
public class FilesController {


    private static Logger logger = LoggerFactory.getLogger(FilesController.class);

    @RequestMapping("import/main")
    public String tagMarket(HttpServletRequest request) {
        String strCurrPage = request.getParameter("currPage");
        int currPage = 1;
        if(strCurrPage != null){
            currPage = Integer.parseInt(strCurrPage);
        }
        request.setAttribute("currPage", currPage);

        return "files/import/importUsers";
    }


    @Autowired
    private FilesService filesService;


    @Autowired
    private TagService  tagService;

    @RequestMapping(value = "import/upload")
    @ResponseBody
    public String upload(@RequestParam(value="importUsersFile", required = true) MultipartFile importUsersFile,
                         @RequestParam(value="share", required=false, defaultValue ="true") boolean share,
                         HttpServletRequest request) {

        boolean result = false;
        try {
            int userId = tagService.getCurrentUserId();
            result = filesService.importUsers(importUsersFile, userId, share );
        } catch (FileImportException e) {
            logger.error("failed on store import file content:", e);
            return "failed: " + e;
        }

        return result ? "ok" : "failed";

    }


    @RequestMapping(value = "import/list")
    @ResponseBody
    public String list(@RequestParam(required = false, defaultValue=TagController.DEFAULT_CURRENT_PAGE) int currPage,
                       @RequestParam(required = false, defaultValue=TagController.DEFAULT_PAGE_ROW_SIZE) int pageSize,
                       @RequestParam(required = false) String search_context,
                       @RequestParam(required = false) String order_id,
                       @RequestParam(required = false) String order_type) {

        int userId = tagService.getCurrentUserId();
        List<ImportFile> results = filesService.listAllByUser( userId );

        String json = TagController.renderResultListsByPage(currPage, pageSize, results);
        return json;

    }


    @RequestMapping(value = "export/campUsers/{campId}")
    @ResponseBody
    public void exportBrandSort(@PathVariable int campId,
                                HttpServletRequest request,
                                HttpServletResponse response) {


        MsgExportCampUsers msgExportCampUsers  = new MsgExportCampUsers();
        msgExportCampUsers.setCampId( campId );

        response.setCharacterEncoding("UTF-8");

        boolean isSuccess = false;
        try {

            String filename = "campUsers_" + campId + ".csv";

            response.setContentType("application/zip");
            response.setHeader("Content-Disposition", "attachment;filename=" + filename + ".zip");

            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            int fileSize = filesService.exportCampUsers(String.valueOf(campId), bos);

            ZipOutputStream zipStream = new ZipOutputStream( response.getOutputStream() );
            zipStream.putNextEntry(new ZipEntry( filename ));

            zipStream.write( bos.toByteArray() );
            zipStream.finish();
            zipStream.close();

            response.setContentLength( fileSize );

            isSuccess = true;

        } catch (MissingCampException e) {
            msgExportCampUsers.setResultCode( 1 );
            msgExportCampUsers.setMsg("can not find such camp:" + campId);
        } catch (Throwable e) {
            logger.error("error occurred on  exportBrandSort: {}, exception: ", campId,  e);
            msgExportCampUsers.setResultCode( 2 );
            msgExportCampUsers.setMsg("internal error");

        }

        if( !isSuccess ) {
            response.setContentType("application/json");
            response.setHeader("Content-Disposition", "");

            String json = UserTagUtils.objectToJson( msgExportCampUsers );
            try {
                response.getOutputStream().write( json.getBytes() );
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

}
