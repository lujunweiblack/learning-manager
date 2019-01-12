package web.file.common;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import web.file.entity.FileT;
import web.file.service.FileService;

import java.io.ByteArrayInputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@RestController
public class QiNiuFileUPCommon {

    private final static Logger logger = LoggerFactory.getLogger(QiNiuFileUPCommon.class);


    @Value("${qiniu.cloud.accessKey}")
    private String accessKey;
    @Value("${qiniu.cloud.secretKey}")
    private String secretKey;
    @Value("${qiniu.cloud.bucket}")
    private String bucket;

    @Autowired
    private FileService fileService;

    @PostMapping("/qiniu/upload")
    public Map<String, Object> qiNiuUpload(@RequestParam("file") MultipartFile file) {

        Map<String, Object> returnData = new HashMap<>();

        if (file.isEmpty()) {
            returnData.put("state", 2);
            return returnData;
        }

        String fileName = file.getOriginalFilename();

        //查询是否存在此文件名
        if (fileService.findByFileName(fileName) != null) {
            returnData.put("state", 1);
            return returnData;
        }


        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone0());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = fileName;
        try {
            byte[] uploadBytes = file.getBytes();

            ByteArrayInputStream byteInputStream = new ByteArrayInputStream(uploadBytes);
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);
            Response response = uploadManager.put(byteInputStream, key, upToken, null, null);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);

            System.out.println(putRet.key);
            System.out.println(putRet.hash);

            FileT fileT = new FileT();
            Date date = new Date();
            fileT.setFileName(putRet.key);
            fileT.setFilePath(fileName);
            fileT.setFileType(FileTools.getSuffix(fileName));
            fileT.setFileCode(putRet.hash.length() >27 ? putRet.hash.substring(0,20)+"...":putRet.hash);
            fileT.setCreateDate(date);
            fileT.setFileSize(FileTools.getSize(Integer.valueOf(Long.valueOf(file.getSize()).toString())));
            fileService.insertFileInfo(fileT);
            logger.info("=================================文件信息开始保存=================================");
            logger.info("=======================上传的文件名称为：" + fileT.getFileName() + "=======================");
            logger.info("=======================上传的文件类型为：" + fileT.getFileType() + "=======================");
            logger.info("=======================上传的文件路径为：" + fileT.getFilePath() + "=======================");
            logger.info("=======================上传的文件编码为：" + fileT.getFileCode() + "=======================");
            logger.info("=================================文件信息保存成功=================================");
            returnData.put("state", "0");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return returnData;
    }


}
