package web.file.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;

/**
 * @author: lujunwei
 * @time: 2018/7/12
 * @des:
 */
public class FileTools {
    private final static Logger logger = LoggerFactory.getLogger(FileTools.class);

    /**
     * @author: lujunwei
     * @time: 2018/7/12
     * @des: 获取文件的后缀
     */
    public static String getSuffix(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    /**
     * @author: lujunwei
     * @time: 2018/7/12
     * @des: 获取文件的名称不带后缀
     */
    public static String getFileName(String fileName) {
        return fileName.substring(0, fileName.lastIndexOf(".") + 1);
    }

    public static String GetFileSize(String filePath) {
        long fileSize = 0l;
        FileChannel fc = null;
        try {
            File f = new File(filePath);
            if (f.exists() && f.isFile()) {
                FileInputStream fis = new FileInputStream(f);
                fc = fis.getChannel();
                fileSize = fc.size() / 1024 + 1;
            } else {
                logger.debug("file doesn't exist or is not a file");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != fc) {
                try {
                    fc.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return String.valueOf(fileSize + "KB");
    }

    public static String getSize(int size) {
        //获取到的size为：1705230
        int GB = 1024 * 1024 * 1024;//定义GB的计算常量
        int MB = 1024 * 1024;//定义MB的计算常量
        int KB = 1024;//定义KB的计算常量
        DecimalFormat df = new DecimalFormat("0.00");//格式化小数
        String resultSize = "";
        if (size / GB >= 1) {
            //如果当前Byte的值大于等于1GB
            resultSize = df.format(size / (float) GB) + "GB   ";
        } else if (size / MB >= 1) {
            //如果当前Byte的值大于等于1MB
            resultSize = df.format(size / (float) MB) + "MB   ";
        } else if (size / KB >= 1) {
            //如果当前Byte的值大于等于1KB
            resultSize = df.format(size / (float) KB) + "KB   ";
        } else {
            resultSize = size + "B   ";
        }
        return resultSize;
    }


}
