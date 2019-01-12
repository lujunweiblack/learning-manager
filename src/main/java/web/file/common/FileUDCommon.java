package web.file.common;

import java.text.DecimalFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import web.file.entity.FileT;
import web.file.service.FileService;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.beans.Transient;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author: lujunwei
 * @time: 2018/7/12
 * @des:
 */
@Controller
@RequestMapping("fileUD")
public class FileUDCommon {
    private final static Logger logger = LoggerFactory.getLogger(FileUDCommon.class);

    @Autowired
    private FileService fileService;

    @PostMapping("/upload")
    @ResponseBody
    @Transient
    public Map<String, Object> uploadPhoto(@RequestParam("file") MultipartFile file) {
        Map<String, Object> returnData = new HashMap<>();
        String path = "file/";
        String fileName = "";
        if (!file.isEmpty()) {
            BufferedOutputStream out = null;
            File fileSourcePath = new File(path);
            if (!fileSourcePath.exists()) {
                fileSourcePath.mkdirs();
            }

            fileName = file.getOriginalFilename();
            //查询是否存在此文件名
            if (fileService.findByFileName(fileName) != null) {
                returnData.put("state", 1);
            } else {

                try {
                    File file1 = new File(fileSourcePath, fileName);
                    out = new BufferedOutputStream(
                            new FileOutputStream(file1));
                    try {
                        out.write(file.getBytes());
                        out.flush();
                        FileT fileT = new FileT();
                        Date date = new Date();
                        fileT.setFileName(fileName);
                        fileT.setFilePath(path + fileName);
                        fileT.setFileType(FileTools.getSuffix(fileName));
                        fileT.setFileCode(UUID.randomUUID().toString().replaceAll("-", "").substring(0, 10));
                        fileT.setCreateDate(date);
                        fileT.setFileSize(FileTools.GetFileSize(fileT.getFilePath()));
                        fileService.insertFileInfo(fileT);
                        logger.info("=================================文件信息开始保存=================================");
                        logger.info("=======================上传的文件名称为：" + fileT.getFileName() + "=======================");
                        logger.info("=======================上传的文件类型为：" + fileT.getFileType() + "=======================");
                        logger.info("=======================上传的文件路径为：" + fileT.getFilePath() + "=======================");
                        logger.info("=======================上传的文件编码为：" + fileT.getFileCode() + "=======================");
                        logger.info("=================================文件信息保存成功=================================");
                        returnData.put("state", "0");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    logger.info("=================================文件信息保存失败=================================");
                    returnData.put("state", -1);
                }
            }
        }else {
            returnData.put("state", 2);
        }
        return returnData;
    }


    @GetMapping("/down")
    @Transient
    public void displayPhoto(String id, HttpServletResponse response) {
        ServletOutputStream out = null;
        FileInputStream ips = null;
        try {

            FileT file = fileService.findById(id);

            //获取图片存放路径
            ips = new FileInputStream(new File(file.getFilePath()));
            response.setContentType("multipart/form-data");
            response.addHeader("Content-Disposition", "attachment; filename=" + new String(file.getFileName().getBytes("UTF-8"), "iso-8859-1"));
            out = response.getOutputStream();
            //读取文件流
            int len = 0;
            byte[] buffer = new byte[1024 * 10];
            while ((len = ips.read(buffer)) != -1) {
                //out.write(buffer, 0, len);
                response.getOutputStream().write(buffer, 0, len);
            }
            logger.info("=================================文件信息开始下载=================================");
            logger.info("=======================上传的文件信息为：" + file.getFilePath() + "=======================");
            logger.info("=================================文件信息下载成功=================================");
            //out.flush();
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("=================================文件信息下载失败=================================");
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
                logger.info("=================================关闭资源失败=================================");
            }
            try {
                ips.close();
            } catch (IOException e) {
                e.printStackTrace();
                logger.info("=================================文件信息下载失败=================================");
            }
        }
    }
}
