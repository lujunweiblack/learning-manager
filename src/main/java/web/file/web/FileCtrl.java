package web.file.web;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import web.file.common.FileUDCommon;
import web.file.entity.FileT;
import web.file.service.FileService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("file")
public class FileCtrl {

    private final static Logger logger = LoggerFactory.getLogger(FileUDCommon.class);

    @Autowired
    private FileService fileService;

    @GetMapping("find")
    public Map<String, Object> findFile(Integer pageNum,Integer pageSize,FileT fileT) {
        Map<String, Object> res = new HashMap<>();
        PageHelper.startPage(pageNum,pageSize);
        List<FileT> fileList = fileService.find(fileT);
        res.put("state","0");
        res.put("data",new PageInfo<FileT>(fileList));
        return res;
    }

    @GetMapping("del")
    public Map<String, Object> findDel(FileT fileT) {
        Map<String, Object> res = new HashMap<>();
        try {
            fileService.findDel(fileT);
            res.put("state","0");
            logger.debug("=================================文件信息删除成功=================================");
        }catch (Exception e){
            e.printStackTrace();
            logger.debug("=================================文件信息删除失败=================================");
        }
        return res;
    }


    @GetMapping("findById")
    public Map<String, Object> findById(String id) {
        Map<String, Object> res = new HashMap<>();
        FileT file = fileService.findById(id);
        res.put("state","0");
        res.put("filePath",file.getFilePath());
        return res;
    }
}
