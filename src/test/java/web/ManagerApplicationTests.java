package web;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class ManagerApplicationTests {

    //    @Test
    public void contextLoads() {
    }


    public static void main(String[] args) throws UnsupportedEncodingException {
//        String accessKey = "haPHu0pQwFGoYparuOTc5SMepFqY4mIcRrDhXxEb";
//        String secretKey = "0CAzx1rNvI0ipHPAJsLHmesGC94EXU30gPPggjB0";
//        String bucket = "bucket name";
//        Auth auth = Auth.create(accessKey, secretKey);
//        String upToken = auth.uploadToken(bucket);
//        System.out.println(upToken);

        upload();
        //down();
    }

    public static void upload() {
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone0());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //...生成上传凭证，然后准备上传
        String accessKey = "haPHu0pQwFGoYparuOTc5SMepFqY4mIcRrDhXxEb";
        String secretKey = "0CAzx1rNvI0ipHPAJsLHmesGC94EXU30gPPggjB0";
        String bucket = "production";
        //如果是Windows情况下，格式是 D:\\qiniu\\test.png
        String localFilePath = "C:\\Users\\Administrator\\Desktop\\AppFile\\002.jpg";
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = "001.jpg";
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        try {
            Response response = uploadManager.put(localFilePath, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        }
    }


    public static void down() throws UnsupportedEncodingException {
        String fileName = "001";
        String domainOfBucket = "http://pcaooyn7y.bkt.clouddn.com";
        String encodedFileName = URLEncoder.encode(fileName, "utf-8");
        String finalUrl = String.format("%s/%s", domainOfBucket, encodedFileName);
        System.out.println(finalUrl);
    }

}
